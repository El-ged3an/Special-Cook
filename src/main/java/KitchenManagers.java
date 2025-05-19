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
 return "Manager with this name already exists.";
    

        } catch (SQLException e) {
    
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
  return "Manager not found.";
        } catch (SQLException e) {
           
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
 
  return "Manager not found.";
        } catch (SQLException e) { 
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
  return "Manager not found.";
           

        } catch (SQLException e) { 
        }
    }
}
