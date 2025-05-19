import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
public class KitchenManagersTest {
    // note: lowercase schema name
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/specialcookdb";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection conn;
    private KitchenManagers dao;

    @BeforeAll
    static void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @BeforeEach
    void beginTransaction() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false);
        dao = new KitchenManagers(conn);
    }

    @AfterEach
    void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
        conn.close();
    }

    private String randomName() {
        return "TestMgr_" + UUID.randomUUID();
    }

    @Test
    void testAddManagerSuccess() {
        String name    = randomName();
        String contact = "a@example.com";

        String result = dao.addManager(name, contact);
        assertEquals("Manager added successfully.", result);
    }

    @Test
    void testAddManagerDuplicate() {
        String name    = randomName();
        String contact = "dup1@example.com";

        // first insert
        assertEquals("Manager added successfully.", dao.addManager(name, contact));

        // second insert should be detected as duplicate
        String result2 = dao.addManager(name, "dup2@example.com");
        assertEquals("Manager with this name already exists.", result2);
    }

    @Test
    void testUpdateManagerSuccess() throws SQLException {
        // seed a manager row
        String origName    = randomName();
        String origContact = "orig@example.com";
        int    id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, origName);
            p.setString(2, origContact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        String newName    = randomName();
        String newContact = "new@example.com";

        String result = dao.updateManager(id, newName, newContact);
        assertEquals("Manager updated successfully.", result);

        // verify via direct query (within same transaction)
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
        String result = dao.updateManager(-99999, "X", "Y");
        assertEquals("Manager not found.", result);
    }

    @Test
    void testDeleteManagerSuccess() throws SQLException {
        // seed
        String name    = randomName();
        String contact = "del@example.com";
        int    id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        String result = dao.deleteManager(id);
        assertEquals("Manager deleted successfully.", result);

        // verify gone
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
        String result = dao.deleteManager(-12345);
        assertEquals("Manager not found.", result);
    }

    @Test
    void testGetManagerFound() throws SQLException {
        // seed
        String name    = randomName();
        String contact = "get@example.com";
        int    id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
            }
        }

        String result = dao.getManager(id);
        assertEquals("Manager: " + name + ", Contact Info: " + contact, result);
    }

    @Test
    void testGetManagerNotFound() {
        String result = dao.getManager(-4242);
        assertEquals("Manager not found.", result);
    }
}
