import java.sql.*;

public class User {
     int userId;
  String username;
    String password;
      String role;

    private static final String URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.setPassword(password);
        this.role = role;
    }

    public static String addUser(User user) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

            String checkUserExists = "SELECT COUNT(*) FROM Users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserExists)) {
                ps.setString(1, user.username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
                    return "User already exists!";
                }
            }

            String insertUser = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
                ps.setString(1, user.username);
                ps.setString(2, user.getPassword());
                ps.setString(3, user.role);
                ps.executeUpdate();
            }

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            conn.commit();
            return "User added successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String updateUser(User user) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

            String checkUserExists = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserExists)) {
                ps.setInt(1, user.userId);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
                    return "User does not exist!";
                }
            }

            String updateUser = "UPDATE Users SET username = ?, password = ?, role = ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateUser)) {
                ps.setString(1, user.username);
                ps.setString(2, user.getPassword());
                ps.setString(3, user.role);
                ps.setInt(4, user.userId);
                ps.executeUpdate();
            }

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            conn.commit();
            return "User updated successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String deleteUser(int userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

            String checkUserExists = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserExists)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
                    return "User does not exist!";
                }
            }

            String deleteUser = "DELETE FROM Users WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteUser)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            conn.commit();
            return "User deleted successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static User getUserById(int userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String selectUser = "SELECT * FROM Users WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectUser)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
