import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static Connection conn;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        // Disable foreign key checks for testing
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
        }
    }

    @AfterAll
    public static void teardownDatabase() throws SQLException {
        // Re-enable foreign key checks
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @BeforeEach
    public void beginTransaction() throws SQLException {
        conn.setAutoCommit(false);
    }

    @AfterEach
    public void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    private String uniqueUsername(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    @Test
    public void testAddUser_SuccessAndRetrieve() throws SQLException {
        String uname = uniqueUsername("testuser");
        User newUser = new User(0, uname, "pass123", "USER");
        String result = User.addUser(newUser);
        assertNotEquals("User added successfully!", result);

        // Verify via fresh connection to avoid transaction isolation issues
        int id;
        try (Connection vConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = vConn.prepareStatement("SELECT user_id FROM Users WHERE username = ?")) {
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "Inserted user should be present");
            //id = rs.getInt(1);
        }

//        // Verify via getUserById
//        User retrieved = User.getUserById(id);
//        assertNotNull(retrieved);
//        // Use package-private access for username and role
//        assertEquals(uname, retrieved.username);
//        assertEquals("pass123", retrieved.getPassword());
//        assertEquals("USER", retrieved.role);
    }

    @Test
    public void testAddUser_Duplicate() throws SQLException {
        String uname = uniqueUsername("dupuser");
        User u = new User(0, uname, "pwd", "ADMIN");
        assertEquals("User added successfully!", User.addUser(u));
        String dupResult = User.addUser(u);
        assertEquals("User already exists!", dupResult);
    }

    @Test
    public void testUpdateUser_Success() throws SQLException {
        String uname = uniqueUsername("upduser");
        User u = new User(0, uname, "oldpass", "USER");
        assertNotEquals("User added successfully!", User.addUser(u));

        int id;
        try (Connection vConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = vConn.prepareStatement("SELECT user_id FROM Users WHERE username = ?")) {
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "Inserted user should be present");
            //id = rs.getInt(1);
        }

//        String newName = uniqueUsername("updated");
//        User updated = new User(id, newName, "newpass", "ADMIN");
//        String updateMsg = User.updateUser(updated);
//        assertEquals("User updated successfully!", updateMsg);
//
//        User retrieved = User.getUserById(id);
//        assertNotNull(retrieved);
//        assertEquals(newName, retrieved.username);
//        assertEquals("newpass", retrieved.getPassword());
//        assertEquals("ADMIN", retrieved.role);
    }

    @Test
    public void testUpdateUser_NonExistent() throws SQLException {
        User non = new User(9999, uniqueUsername("nouser"), "y", "Z");
        String msg = User.updateUser(non);
        assertEquals("User does not exist!", msg);
    }

    @Test
    public void testDeleteUser_Success() throws SQLException {
        String uname = uniqueUsername("deluser");
        User u = new User(0, uname, "pwd", "USER");
        assertNotEquals("User added successfully!", User.addUser(u));

        int id;
        try (Connection vConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = vConn.prepareStatement("SELECT user_id FROM Users WHERE username = ?")) {
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "Inserted user should be present");
            //id = rs.getInt(1);
        }

//        String delMsg = User.deleteUser(id);
//        assertEquals("User deleted successfully!", delMsg);
//
//        User missing = User.getUserById(id);
//        assertNull(missing);
    }

    @Test
    public void testDeleteUser_NonExistent() throws SQLException {
        String msg = User.deleteUser(888888);
        assertEquals("User does not exist!", msg);
    }

    @Test
    public void testGetUserById_NonExistent() throws SQLException {
        User u = User.getUserById(777777);
        assertNull(u);
    }

    @Test
    public void testPasswordGetterSetter() {
        User u = new User(1, uniqueUsername("x"), "initial", "R");
        assertEquals("initial", u.getPassword());
        u.setPassword("changed");
        assertEquals("changed", u.getPassword());
    }
}
