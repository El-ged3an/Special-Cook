import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection manualConn;
    private List<Integer> cleanupUserIds;

    @BeforeAll
    void initDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
        // disable FK checks so we can delete whatever we inserted
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        }
        for (int id : cleanupUserIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM Users WHERE user_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
        }
    }

    // helper to look up the user_id by username
    private int findUserIdByUsername(String username) throws SQLException {
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT user_id FROM Users WHERE username = ?")) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new AssertionError("Could not find user with username: " + username);
    }

    @Test
    void testAddUserSuccessAndGetById() throws SQLException {
        String username = "testAddUser" + System.nanoTime();
        String password = "secret";
        String role     = "Customer";  // must be one of: Customer, Kitchen Manager, Admin, Chef

        User u = new User(0, username, password, role);
        String res = User.addUser(u);
        assertEquals("User added successfully!", res);

        int id = findUserIdByUsername(username);
        cleanupUserIds.add(id);

        User fetched = User.getUserById(id);
        assertNotNull(fetched);
        assertEquals(id,    fetched.userId);
        assertEquals(username, fetched.username);
        assertEquals(password, fetched.getPassword());
        assertEquals(role,     fetched.role);
    }

    @Test
    void testAddUserAlreadyExists() throws SQLException {
        String username = "testDupUser" + System.nanoTime();
        String password = "pwd";
        String role     = "Chef";

        // first insert
        User u1 = new User(0, username, password, role);
        String first = User.addUser(u1);
        assertEquals("User added successfully!", first);
        int id = findUserIdByUsername(username);
        cleanupUserIds.add(id);

        // second insert should detect duplicate
        User u2 = new User(0, username, "other", "Admin");
        String second = User.addUser(u2);
        assertEquals("User already exists!", second);

        // verify only one row exists
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT COUNT(*) FROM Users WHERE username = ?")) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                assertEquals(1, rs.getInt(1));
            }
        }
    }

    @Test
    void testUpdateUserSuccess() throws SQLException {
        // create a user first
        String originalName = "origUser" + System.nanoTime();
        User orig = new User(0, originalName, "origPass", "Admin");
        assertEquals("User added successfully!", User.addUser(orig));
        int id = findUserIdByUsername(originalName);
        cleanupUserIds.add(id);

        // now update username, password, role
        String newName = "updatedUser" + System.nanoTime();
        String newPass = "newPass";
        String newRole = "Kitchen Manager";
        User updated = new User(id, newName, newPass, newRole);
        String res = User.updateUser(updated);
        assertEquals("User updated successfully!", res);

        // fetch and verify
        User fetched = User.getUserById(id);
        assertNotNull(fetched);
        assertEquals(newName,   fetched.username);
        assertEquals(newPass,   fetched.getPassword());
        assertEquals(newRole,   fetched.role);
    }

    @Test
    void testUpdateUserNotFound() {
        User bogus = new User(-99999, "nope", "nopass", "Admin");
        String res = User.updateUser(bogus);
        assertEquals("User does not exist!", res);
    }

    @Test
    void testDeleteUserSuccess() throws SQLException {
        String username = "toDelete" + System.nanoTime();
        User u = new User(0, username, "p", "Chef");
        assertEquals("User added successfully!", User.addUser(u));
        int id = findUserIdByUsername(username);
        // delete it
        String res = User.deleteUser(id);
        assertEquals("User deleted successfully!", res);

        // should be gone
        assertNull(User.getUserById(id));
    }

    @Test
    void testDeleteUserNotFound() {
        String res = User.deleteUser(-888888);
        assertEquals("User does not exist!", res);
    }

    @Test
    void testGetUserByIdNotFound() {
        assertNull(User.getUserById(-123456));
    }

    @Test
    void testPasswordGetterSetter() {
        User u = new User(1, "u", "initial", "Customer");
        assertEquals("initial", u.getPassword());
        u.setPassword("changed");
        assertEquals("changed", u.getPassword());
    }
}
