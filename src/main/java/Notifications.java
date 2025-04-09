import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Notifications {
    private Connection connection;

    public Notifications(Connection connection) {
        this.connection = connection;
    }

    public String addNotification(int userId, String message) {
        String query = "INSERT INTO Notifications (user_id, message) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String checkQuery = "SELECT notification_id FROM Notifications WHERE user_id = ? AND message = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                checkStmt.setString(2, message);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return "Notification already exists.";
                }
            }

            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.executeUpdate();
            return "Notification added successfully.";
        } catch (SQLException e) {
            return "Error adding notification: " + e.getMessage();
        }
    }

    public String updateNotification(int notificationId, int userId, String message) {
        String query = "UPDATE Notifications SET user_id = ?, message = ? WHERE notification_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String checkQuery = "SELECT notification_id FROM Notifications WHERE notification_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, notificationId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Notification not found.";
                }
            }

            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.setInt(3, notificationId);
            stmt.executeUpdate();
            return "Notification updated successfully.";
        } catch (SQLException e) {
            return "Error updating notification: " + e.getMessage();
        }
    }

    public String deleteNotification(int notificationId) {
        String query = "DELETE FROM Notifications WHERE notification_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String checkQuery = "SELECT notification_id FROM Notifications WHERE notification_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, notificationId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Notification not found.";
                }
            }

            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
            return "Notification deleted successfully.";
        } catch (SQLException e) {
            return "Error deleting notification: " + e.getMessage();
        }
    }

    public List<String> getAllNotifications(int userId) {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT message FROM Notifications WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(rs.getString("message"));
            }
        } catch (SQLException e) {
            notifications.add("Error retrieving notifications: " + e.getMessage());
        }
        return notifications;
    }
}
