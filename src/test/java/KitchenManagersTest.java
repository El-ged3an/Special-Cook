import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KitchenManagersTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection conn;
    private KitchenManagers dao;

    @BeforeAll
    void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @BeforeEach
    void openTransaction() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false);
        dao = new KitchenManagers(conn);
    }

    @AfterEach
    void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /** Helper to look up a manager’s ID by name in this transaction */
    private int findManagerIdByName(String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT manager_id FROM KitchenManagers WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new AssertionError("Could not find manager named: " + name);
    }

    @Test
    void testAddManagerSuccess() throws SQLException {
        String name = "MgrSuccess" + System.nanoTime();
        String contact = "success@example.com";

        String result = dao.addManager(name, contact);
        assertEquals("Manager added successfully.", result);

        // verify it’s in the DB
        int id = findManagerIdByName(name);
        assertTrue(id > 0);
    }

    @Test
    void testAddManagerDuplicate() throws SQLException {
        String name = "MgrDup" + System.nanoTime();
        String contact1 = "first@example.com";
        String contact2 = "second@example.com";

        // first time => success
        assertEquals("Manager added successfully.", dao.addManager(name, contact1));

        // second time => duplicate
        String result2 = dao.addManager(name, contact2);
        assertEquals("Manager with this name already exists.", result2);
    }

    @Test
    void testUpdateManagerSuccess() throws SQLException {
        // insert one
        String origName = "OrigMgr" + System.nanoTime();
        String origContact = "orig@example.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, origName);
            p.setString(2, origContact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        // update it
        String newName = "NewMgr" + System.nanoTime();
        String newContact = "new@example.com";
        String upd = dao.updateManager(id, newName, newContact);
        assertEquals("Manager updated successfully.", upd);

        // verify
        try (PreparedStatement q = conn.prepareStatement(
                "SELECT name, contact_info FROM KitchenManagers WHERE manager_id = ?")) {
            q.setInt(1, id);
            try (ResultSet rs = q.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(newName, rs.getString("name"));
                assertEquals(newContact, rs.getString("contact_info"));
            }
        }
    }

    @Test
    void testUpdateManagerNotFound() {
        String res = dao.updateManager(-9999, "X", "Y");
        assertEquals("Manager not found.", res);
    }

    @Test
    void testDeleteManagerSuccess() throws SQLException {
        // insert one
        String name = "DelMgr" + System.nanoTime();
        String contact = "del@example.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        // delete via DAO
        String res = dao.deleteManager(id);
        assertEquals("Manager deleted successfully.", res);

        // verify it’s gone
        try (PreparedStatement q = conn.prepareStatement(
                "SELECT COUNT(*) FROM KitchenManagers WHERE manager_id = ?")) {
            q.setInt(1, id);
            try (ResultSet rs = q.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeleteManagerNotFound() {
        String res = dao.deleteManager(-8888);
        assertEquals("Manager not found.", res);
    }

    @Test
    void testGetManagerFound() throws SQLException {
        // insert one
        String name = "GetMgr" + System.nanoTime();
        String contact = "get@example.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        String out = dao.getManager(id);
        assertEquals("Manager: " + name + ", Contact Info: " + contact, out);
    }

    @Test
    void testGetManagerNotFound() {
        String out = dao.getManager(-12345);
        assertEquals("Manager not found.", out);
    }
}
