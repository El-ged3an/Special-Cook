import java.sql.*;

public class LoginSystem {

    private static Connection conn;

    public LoginSystem(Connection connection) {
        conn = connection;
    }

    public String addUser(String username, String password, String role) {
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return "User already exists!";
            }

            query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();

            return "User added successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding user.";
        }
    }

    public String updateUser(int userId, String username, String password, String role) {
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                return "User does not exist!";
            }

            query = "UPDATE Users SET username = ?, password = ?, role = ? WHERE user_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setInt(4, userId);
            stmt.executeUpdate();

            return "User updated successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating user.";
        }
    }

    public String deleteUser(int userId) {
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                return "User does not exist!";
            }

            query = "DELETE FROM Users WHERE user_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.executeUpdate();

            return "User deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting user.";
        }
    }

    public String getUser(int userId) {
        try {
            String query = "SELECT username, role FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "User not found!";
            }
            return "Username: " + rs.getString("username") + ", Role: " + rs.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving user.";
        }
    }

    public String login(String username, String password) {
        try {
            String query = "SELECT user_id, role FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "Invalid credentials.";
            }
            return "Login successful. User ID: " + rs.getInt("user_id") + ", Role: " + rs.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error during login.";
        }
    }

    public String checkRole(int userId) {
        try {
            String query = "SELECT role FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "User not found.";
            }
            return "Role: " + rs.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error checking role.";
        }
    }

    public String setForeignKeyChecks(boolean enable) {
        try {
            Statement stmt = conn.createStatement();
            if (enable) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            } else {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
            }
            return "Foreign key checks " + (enable ? "enabled" : "disabled") + ".";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error changing foreign key checks.";
        }
    }

    
}
