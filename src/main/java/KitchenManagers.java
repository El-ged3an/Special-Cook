import java.sql.*;

public class KitchenManagers {

    private final Connection conn;

    public KitchenManagers(Connection conn) {
        this.conn = conn;
    }

    public String addManager(String name, String contactInfo) {
        String checkQuery  = "SELECT COUNT(*) FROM KitchenManagers WHERE name = ?";
        String insertQuery = "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)";

        // 1) Check for existing manager
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, name);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Manager with this name already exists.";
                }
            }
        } catch (SQLException e) {
            return "Error checking existing manager: " + e.getMessage();
        }

        // 2) Insert new manager
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, contactInfo);
            insertStmt.executeUpdate();
            return "Manager added successfully.";
        } catch (SQLException e) {
            return "Error adding manager: " + e.getMessage();
        }
    }

    public String updateManager(int managerId, String newName, String newContactInfo) {
        String checkQuery   = "SELECT COUNT(*) FROM KitchenManagers WHERE manager_id = ?";
        String updateQuery  = "UPDATE KitchenManagers SET name = ?, contact_info = ? WHERE manager_id = ?";

        // 1) Check existence
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, managerId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    return "Manager not found.";
                }
            }
        } catch (SQLException e) {
            return "Error checking manager: " + e.getMessage();
        }

        // 2) Perform update
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, newName);
            updateStmt.setString(2, newContactInfo);
            updateStmt.setInt(3, managerId);
            updateStmt.executeUpdate();
            return "Manager updated successfully.";
        } catch (SQLException e) {
            return "Error updating manager: " + e.getMessage();
        }
    }

    public String deleteManager(int managerId) {
        String checkQuery  = "SELECT COUNT(*) FROM KitchenManagers WHERE manager_id = ?";
        String deleteQuery = "DELETE FROM KitchenManagers WHERE manager_id = ?";

        // 1) Check existence
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, managerId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    return "Manager not found.";
                }
            }
        } catch (SQLException e) {
            return "Error checking manager: " + e.getMessage();
        }

        // 2) Perform delete
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, managerId);
            deleteStmt.executeUpdate();
            return "Manager deleted successfully.";
        } catch (SQLException e) {
            return "Error deleting manager: " + e.getMessage();
        }
    }

    public String getManager(int managerId) {
        String query = "SELECT * FROM KitchenManagers WHERE manager_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name        = rs.getString("name");
                    String contactInfo = rs.getString("contact_info");
                    return String.format(
                        "Manager [ID=%d, Name=%s, Contact Info=%s]",
                        managerId, name, contactInfo
                    );
                } else {
                    return "Manager not found.";
                }
            }
        } catch (SQLException e) {
            return "Error retrieving manager: " + e.getMessage();
        }
    }
}
