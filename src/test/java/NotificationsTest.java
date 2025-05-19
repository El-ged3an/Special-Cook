import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotificationsTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;
    private Notifications notifications;

    @BeforeAll
    void initDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @AfterAll
    void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @BeforeEach
    void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
        notifications = new Notifications(connection);
    }

    @AfterEach
    void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @Test
    void testAddNotificationSuccess() throws SQLException {
        String result = notifications.addNotification(1, "Welcome!");
        assertEquals("Notification added successfully.", result);

        // Within the same transaction, we can still verify it was “inserted”
        try (PreparedStatement verify = connection.prepareStatement(
                "SELECT COUNT(*) FROM Notifications WHERE user_id = ? AND message = ?")) {
            verify.setInt(1, 1);
            verify.setString(2, "Welcome!");
            ResultSet rs = verify.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void testAddNotificationAlreadyExists() throws SQLException {
        // 1) grab a valid customer_id
        int userId;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT customer_id FROM Customers LIMIT 1");
            assertTrue(rs.next(), "Need at least one customer in the DB for this test");
            userId = rs.getInt(1);
        }

        // 2) use a unique message so we know exactly what we're dealing with
        String message = "DupeTest-" + System.nanoTime();

        // 3) pre-insert one notification for that user
        try (PreparedStatement pre = connection.prepareStatement(
                "INSERT INTO Notifications (user_id, message) VALUES (?, ?)")) {
            pre.setInt(1, userId);
            pre.setString(2, message);
            pre.executeUpdate();
        }

        // 4) now our DAO should see it and refuse to add another
        String result = notifications.addNotification(userId, message);
        assertEquals("Notification already exists.", result);

        // 5) verify there's still exactly one row for that (userId, message)
        try (PreparedStatement verify = connection.prepareStatement(
                "SELECT COUNT(*) FROM Notifications WHERE user_id = ? AND message = ?")) {
            verify.setInt(1, userId);
            verify.setString(2, message);
            ResultSet rs = verify.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }



    @Test
    void testUpdateNotificationSuccess() throws SQLException {
        // insert and grab its generated ID
        int id;
        try (PreparedStatement pre = connection.prepareStatement(
                "INSERT INTO Notifications (user_id, message) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, 1);
            pre.setString(2, "Old msg");
            pre.executeUpdate();
            ResultSet keys = pre.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
        }

        String result = notifications.updateNotification(id, 3, "New msg");
        assertEquals("Notification updated successfully.", result);

        try (PreparedStatement verify = connection.prepareStatement(
                "SELECT user_id, message FROM Notifications WHERE notification_id = ?")) {
            verify.setInt(1, id);
            ResultSet rs = verify.executeQuery();
            rs.next();
            assertEquals(3, rs.getInt("user_id"));
            assertEquals("New msg", rs.getString("message"));
        }
    }

    @Test
    void testUpdateNotificationNotFound() {
        String result = notifications.updateNotification(99999, 1, "Doesn't matter");
        assertEquals("Notification not found.", result);
    }

    @Test
    void testDeleteNotificationSuccess() throws SQLException {
        // insert one to delete
        int id;
        try (PreparedStatement pre = connection.prepareStatement(
                "INSERT INTO Notifications (user_id, message) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, 4);
            pre.setString(2, "To be deleted");
            pre.executeUpdate();
            ResultSet keys = pre.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
        }

        String result = notifications.deleteNotification(id);
        assertEquals("Notification deleted successfully.", result);

        try (PreparedStatement verify = connection.prepareStatement(
                "SELECT COUNT(*) FROM Notifications WHERE notification_id = ?")) {
            verify.setInt(1, id);
            ResultSet rs = verify.executeQuery();
            rs.next();
            assertEquals(0, rs.getInt(1));
        }
    }

    @Test
    void testDeleteNotificationNotFound() {
        String result = notifications.deleteNotification(88888);
        assertEquals("Notification not found.", result);
    }

    @Test
    void testGetAllNotificationsForUser1() throws SQLException {
        int userId = 1;

        // 1) Read existing notifications for user 1
        List<String> before = notifications.getAllNotifications(userId);
        int initialSize = before.size();

        // 2) Insert two test notifications for user 1
        String msg1 = "JUnit Test A " + System.currentTimeMillis();
        String msg2 = "JUnit Test B " + System.currentTimeMillis();
        try (PreparedStatement pre = connection.prepareStatement(
                "INSERT INTO Notifications (user_id, message) VALUES (?, ?)")) {
            pre.setInt(1, userId);
            pre.setString(2, msg1);
            pre.executeUpdate();

            pre.setInt(1, userId);
            pre.setString(2, msg2);
            pre.executeUpdate();
        }

        // 3) Fetch again and verify we saw exactly two more messages
        List<String> after = notifications.getAllNotifications(userId);
        assertEquals(initialSize + 2, after.size(),
            "Expected exactly two more notifications than before");
        assertTrue(after.contains(msg1), "Should contain first test message");
        assertTrue(after.contains(msg2), "Should contain second test message");
    }
}
