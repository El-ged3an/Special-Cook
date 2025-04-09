import java.sql.*;

public class KitchenManagers {

    private Connection conn;

    public KitchenManagers(Connection conn) {
        this.conn = conn;
    }

    public String addManager(String name, String contactInfo) {
        try {
            String checkQuery = "SELECT * FROM KitchenManagers WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return "Manager with this name already exists.";
            }

            String query = "INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, contactInfo);
            stmt.executeUpdate();

            return "Manager added successfully.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while adding manager.";
        }
    }

    public String updateManager(int managerId, String newName, String newContactInfo) {
        try {
            String checkQuery = "SELECT * FROM KitchenManagers WHERE manager_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, managerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return "Manager not found.";
            }

            String query = "UPDATE KitchenManagers SET name = ?, contact_info = ? WHERE manager_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newName);
            stmt.setString(2, newContactInfo);
            stmt.setInt(3, managerId);
            stmt.executeUpdate();

            return "Manager updated successfully.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while updating manager.";
        }
    }

    public String deleteManager(int managerId) {
        try {
            String checkQuery = "SELECT * FROM KitchenManagers WHERE manager_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, managerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return "Manager not found.";
            }

            String query = "DELETE FROM KitchenManagers WHERE manager_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, managerId);
            stmt.executeUpdate();

            return "Manager deleted successfully.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting manager.";
        }
    }

    public String getManager(int managerId) {
        try {
            String query = "SELECT * FROM KitchenManagers WHERE manager_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return "Manager not found.";
            }

            String managerName = rs.getString("name");
            String contactInfo = rs.getString("contact_info");

            return "Manager: " + managerName + ", Contact Info: " + contactInfo;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while retrieving manager.";
        }
    }
}
