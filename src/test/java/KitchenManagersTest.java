import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KitchenManagersTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection manualConn;
    private KitchenManagers dao;
    private List<Integer> cleanupIds;

    @BeforeAll
    void setupDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        // disable FK checks in case of dependencies
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 0;");
        }
        dao = new KitchenManagers(manualConn);
    }

    @AfterAll
    void teardownDatabase() throws SQLException {
        // re-enable FK checks
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 1;");
        }
        manualConn.close();
    }

    @BeforeEach
    void beforeEach() {
        cleanupIds = new ArrayList<>();
    }

    @AfterEach
    void afterEach() throws SQLException {
        for (int id : cleanupIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM KitchenManagers WHERE manager_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
    }

    /**
     * Helper to look up manager_id by name.
     */
    private int findManagerIdByName(String name) throws SQLException {
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT manager_id FROM KitchenManagers WHERE name = ?")) {
            p.setString(1, name);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new AssertionError("Manager not found: " + name);
    }

    @Test
    void testAddManagerSuccess() throws SQLException {
        String name = "ChefHero" + System.nanoTime();
        String contact = "chef@example.com";

        String res = dao.addManager(name, contact);
        assertEquals("Manager added successfully.", res);

        int id = findManagerIdByName(name);
        cleanupIds.add(id);
    }

    @Test
    void testAddManagerDuplicate() throws SQLException {
        String name = "DupManager" + System.nanoTime();
        String contact = "dup@example.com";

        // first add
        assertEquals("Manager added successfully.", dao.addManager(name, contact));
        int id = findManagerIdByName(name);
        cleanupIds.add(id);

        // duplicate
        String res2 = dao.addManager(name, "other@example.com");
        assertEquals("Manager with this name already exists.", res2);
    }

    @Test
    void testUpdateManagerSuccess() throws SQLException {
        String originalName = "OrigManager" + System.nanoTime();
        String originalContact = "orig@example.com";
        // insert manually
        try (PreparedStatement p = manualConn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, originalName);
            p.setString(2, originalContact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                cleanupIds.add(keys.getInt(1));
            }
        }
        int id = findManagerIdByName(originalName);

        String newName = "UpdatedManager" + System.nanoTime();
        String newContact = "updated@example.com";
        String res = dao.updateManager(id, newName, newContact);
        assertEquals("Manager updated successfully.", res);

        // verify
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT name, contact_info FROM KitchenManagers WHERE manager_id = ?")) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(newName, rs.getString("name"));
                assertEquals(newContact, rs.getString("contact_info"));
            }
        }
    }

    @Test
    void testUpdateManagerNotFound() {
        String res = dao.updateManager(-99999, "X", "Y");
        assertEquals("Manager not found.", res);
    }

    @Test
    void testDeleteManagerSuccess() throws SQLException {
        String name = "DelManager" + System.nanoTime();
        String contact = "del@example.com";
        // insert manually
        try (PreparedStatement p = manualConn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                // delete via DAO
                String res = dao.deleteManager(id);
                assertEquals("Manager deleted successfully.", res);
                // verify
                try (PreparedStatement q = manualConn.prepareStatement(
                        "SELECT COUNT(*) FROM KitchenManagers WHERE manager_id = ?")) {
                    q.setInt(1, id);
                    try (ResultSet rs = q.executeQuery()) {
                        rs.next();
                        assertEquals(0, rs.getInt(1));
                    }
                }
            }
        }
    }

    @Test
    void testDeleteManagerNotFound() {
        String res = dao.deleteManager(-88888);
        assertEquals("Manager not found.", res);
    }

    @Test
    void testGetManagerFound() throws SQLException {
        String name = "GetManager" + System.nanoTime();
        String contact = "get@example.com";
        // insert manually
        try (PreparedStatement p = manualConn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                cleanupIds.add(id);
                String res = dao.getManager(id);
                assertEquals("Manager: " + name + ", Contact Info: " + contact, res);
            }
        }
    }

    @Test
    void testGetManagerNotFound() {
        String res = dao.getManager(-12345);
        assertEquals("Manager not found.", res);
    }
}
