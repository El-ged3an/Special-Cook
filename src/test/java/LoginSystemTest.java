import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginSystemTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection manualConn;
    private LoginSystem loginSystem;
    private List<Integer> cleanupUserIds;

    @BeforeAll
    void initDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        loginSystem = new LoginSystem(manualConn);
    }

    @AfterAll
    void closeDatabase() throws SQLException {
        if (manualConn != null && !manualConn.isClosed()) {
            manualConn.close();
        }
    }

    @BeforeEach
    void setUp() {
        cleanupUserIds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // disable FK checks to allow cleanup
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 0;");
        }
        for (int id : cleanupUserIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM Users WHERE user_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 1;");
        }
    }

    /** Helper to find a user's ID by username. */
    private int findUserIdByUsername(String username) throws SQLException {
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT user_id FROM Users WHERE username = ?")) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new AssertionError("Could not find user: " + username);
    }

    @Test
    void testAddUserSuccess() throws SQLException {
        String username = "addUser" + System.nanoTime();
        String password = "pass123";
        String role     = "Chef";  // one of Customer, Kitchen Manager, Admin, Chef

        String result = loginSystem.addUser(username, password, role);
        assertEquals("User added successfully!", result);

        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        // verify inserted row
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT password, role FROM Users WHERE user_id = ?")) {
            p.setInt(1, userId);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(password, rs.getString("password"));
                assertEquals(role,     rs.getString("role"));
            }
        }
    }

    @Test
    void testAddUserAlreadyExists() throws SQLException {
        String username = "dupUser" + System.nanoTime();
        String password = "pwd";
        String role     = "Admin";

        // first add
        assertEquals("User added successfully!", 
            loginSystem.addUser(username, password, role));

        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        // second add should detect duplicate
        String second = loginSystem.addUser(username, "other", "Customer");
        assertEquals("User already exists!", second);
    }

    @Test
    void testUpdateUserSuccess() throws SQLException {
        // create a user
        String username = "updUser" + System.nanoTime();
        String password = "orig";
        String role     = "Customer";
        assertEquals("User added successfully!", 
            loginSystem.addUser(username, password, role));
        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        // perform update
        String newUsername = "updNew" + System.nanoTime();
        String newPassword = "newpwd";
        String newRole     = "Kitchen Manager";
        String res = loginSystem.updateUser(userId, newUsername, newPassword, newRole);
        assertEquals("User updated successfully!", res);

        // verify update
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT username, password, role FROM Users WHERE user_id = ?")) {
            p.setInt(1, userId);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(newUsername, rs.getString("username"));
                assertEquals(newPassword, rs.getString("password"));
                assertEquals(newRole,     rs.getString("role"));
            }
        }
    }

    @Test
    void testUpdateUserNotFound() {
        String res = loginSystem.updateUser(-99999, "x", "x", "Chef");
        assertEquals("User does not exist!", res);
    }

    @Test
    void testDeleteUserSuccess() throws SQLException {
        String username = "delUser" + System.nanoTime();
        assertEquals("User added successfully!",
            loginSystem.addUser(username, "pw", "Admin"));
        int userId = findUserIdByUsername(username);

        String res = loginSystem.deleteUser(userId);
        assertEquals("User deleted successfully!", res);

        // verify gone
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT COUNT(*) FROM Users WHERE user_id = ?")) {
            p.setInt(1, userId);
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeleteUserNotFound() {
        String res = loginSystem.deleteUser(-888888);
        assertEquals("User does not exist!", res);
    }

    @Test
    void testGetUserFound() throws SQLException {
        String username = "getUser" + System.nanoTime();
        String password = "pw";
        String role     = "Customer";
        assertEquals("User added successfully!",
            loginSystem.addUser(username, password, role));
        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        String res = loginSystem.getUser(userId);
        assertTrue(res.contains("Username: " + username));
        assertTrue(res.contains("Role: " + role));
    }

    @Test
    void testGetUserNotFound() {
        String res = loginSystem.getUser(-123456);
        assertEquals("User not found!", res);
    }

    @Test
    void testLoginSuccess() throws SQLException {
        String username = "loginUser" + System.nanoTime();
        String password = "secret";
        String role     = "Chef";
        assertEquals("User added successfully!",
            loginSystem.addUser(username, password, role));
        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        String res = loginSystem.login(username, password);
        assertTrue(res.startsWith("Login successful. User ID: " + userId));
        assertTrue(res.contains(", Role: " + role));
    }

    @Test
    void testLoginInvalid() {
        String res = loginSystem.login("noSuch", "nopass");
        assertEquals("Invalid credentials.", res);
    }

    @Test
    void testCheckRoleFound() throws SQLException {
        String username = "roleUser" + System.nanoTime();
        String password = "p";
        String role     = "Admin";
        assertEquals("User added successfully!",
            loginSystem.addUser(username, password, role));
        int userId = findUserIdByUsername(username);
        cleanupUserIds.add(userId);

        String res = loginSystem.checkRole(userId);
        assertEquals("Role: " + role, res);
    }

    @Test
    void testCheckRoleNotFound() {
        String res = loginSystem.checkRole(-55555);
        assertEquals("User not found.", res);
    }

    @Test
    void testSetForeignKeyChecks() {
        assertEquals("Foreign key checks disabled.", loginSystem.setForeignKeyChecks(false));
        assertEquals("Foreign key checks enabled.",  loginSystem.setForeignKeyChecks(true));
    }
}
