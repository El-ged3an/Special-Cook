import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.UUID;

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
    void beginTx() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false);
        dao = new KitchenManagers(conn);
    }

    @AfterEach
    void rollbackTx() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /** Never-colliding random name */
    private String rndName() {
        return "Mgr_" + UUID.randomUUID();
    }

    /** Helper to look up manager_id by name */
    private int findId(String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT manager_id FROM KitchenManagers WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new AssertionError("Could not find manager named: " + name);
    }

    @Test
    void testAddManagerSuccess() throws SQLException {
        String name = rndName();
        String contact = "a@example.com";

        String res = dao.addManager(name, contact);
        assertEquals("Manager added successfully.", res);

        // verify it was inserted
        int id = findId(name);
        assertTrue(id > 0);
    }

    @Test
    void testAddManagerDuplicate() throws SQLException {
        String name = rndName();
        String c1 = "one@example.com", c2 = "two@example.com";

        // first insert
        assertEquals("Manager added successfully.", dao.addManager(name, c1));

        // second insert = duplicate
        String res2 = dao.addManager(name, c2);
        assertEquals("Manager with this name already exists.", res2);
    }

    @Test
    void testUpdateManagerSuccess() throws SQLException {
        // insert manually
        String orig = rndName(), origC = "orig@x.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name,contact_info) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, orig);
            p.setString(2, origC);
            p.executeUpdate();
            try (ResultSet k = p.getGeneratedKeys()) {
                k.next();
                id = k.getInt(1);
            }
        }

        String nw = rndName(), nwC = "new@x.com";
        String upd = dao.updateManager(id, nw, nwC);
        assertEquals("Manager updated successfully.", upd);

        // verify
        try (PreparedStatement q = conn.prepareStatement(
                "SELECT name,contact_info FROM KitchenManagers WHERE manager_id=?")) {
            q.setInt(1, id);
            try (ResultSet rs = q.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(nw, rs.getString("name"));
                assertEquals(nwC, rs.getString("contact_info"));
            }
        }
    }

    @Test
    void testUpdateManagerNotFound() {
        String res = dao.updateManager(-1, "X", "Y");
        assertEquals("Manager not found.", res);
    }

    @Test
    void testDeleteManagerSuccess() throws SQLException {
        // insert manually
        String name = rndName(), contact = "del@x.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name,contact_info) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet k = p.getGeneratedKeys()) {
                k.next();
                id = k.getInt(1);
            }
        }

        String res = dao.deleteManager(id);
        assertEquals("Manager deleted successfully.", res);

        // verify gone
        try (PreparedStatement q = conn.prepareStatement(
                "SELECT COUNT(*) FROM KitchenManagers WHERE manager_id=?")) {
            q.setInt(1, id);
            try (ResultSet rs = q.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeleteManagerNotFound() {
        String res = dao.deleteManager(-9_999);
        assertEquals("Manager not found.", res);
    }

    @Test
    void testGetManagerFound() throws SQLException {
        // insert manually
        String name = rndName(), contact = "get@x.com";
        int id;
        try (PreparedStatement p = conn.prepareStatement(
                "INSERT INTO KitchenManagers(name,contact_info) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, contact);
            p.executeUpdate();
            try (ResultSet k = p.getGeneratedKeys()) {
                k.next();
                id = k.getInt(1);
            }
        }

        String out = dao.getManager(id);
        assertEquals("Manager: " + name + ", Contact Info: " + contact, out);
    }

    @Test
    void testGetManagerNotFound() {
        String out = dao.getManager(-42);
        assertEquals("Manager not found.", out);
    }
}
