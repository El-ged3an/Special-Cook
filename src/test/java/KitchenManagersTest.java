import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KitchenManagersTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection conn;
    private KitchenManagers kitchenManagers;

    @BeforeAll
    void setup() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        kitchenManagers = new KitchenManagers(conn);
    }

    @BeforeEach
    void cleanUp() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM KitchenManagers WHERE name LIKE 'TestManager%'");
        }
    }

    @Test
    void testAddManager_successAndDuplicate() {
        String name = "TestManager1";
        String result1 = kitchenManagers.addManager(name, "1234567890");
        assertEquals("Manager added successfully.", result1);

        String result2 = kitchenManagers.addManager(name, "another contact");
        assertEquals("Manager with this name already exists.", result2);
    }

    @Test
    void testUpdateManager_successAndNotFound() throws SQLException {
        // Insert new manager
        String name = "TestManager2";
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, "initial@contact.com");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            assertTrue(rs.next());
            int id = rs.getInt(1);

            // Update
            String result1 = kitchenManagers.updateManager(id, "TestManager2Updated", "updated@contact.com");
            assertEquals("Manager updated successfully.", result1);

            // Update non-existent
            String result2 = kitchenManagers.updateManager(99999, "Ghost", "ghost@void.com");
            assertEquals("Manager not found.", result2);
        }
    }

    @Test
    void testDeleteManager_successAndNotFound() throws SQLException {
        String name = "TestManager3";
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, "delete@this.com");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            assertTrue(rs.next());
            int id = rs.getInt(1);

            String result1 = kitchenManagers.deleteManager(id);
            assertEquals("Manager deleted successfully.", result1);

            String result2 = kitchenManagers.deleteManager(99999);
            assertEquals("Manager not found.", result2);
        }
    }

    @Test
    void testGetManager_successAndNotFound() throws SQLException {
        String name = "TestManager4";
        String contact = "get@me.com";
        int id;

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            assertTrue(rs.next());
            id = rs.getInt(1);
        }

        String result1 = kitchenManagers.getManager(id);
        assertTrue(result1.contains(name));

        String result2 = kitchenManagers.getManager(99999);
        assertEquals("Manager not found.", result2);
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
