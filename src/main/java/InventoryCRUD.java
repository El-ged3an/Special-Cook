import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryCRUD {
    private Connection conn;

    public InventoryCRUD(Connection conn) {
        this.conn = conn;
    }

    public String addInventory(int ingredientId, int supplierId, int stockLevel, Timestamp lastRestocked) {
        String query = "SELECT * FROM Inventory WHERE ingredient_id = ? AND supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ingredientId);
            stmt.setInt(2, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "Inventory entry already exists for the given ingredient and supplier.";
            }

            query = "INSERT INTO Inventory (ingredient_id, supplier_id, stock_level, last_restocked) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(query)) {
                insertStmt.setInt(1, ingredientId);
                insertStmt.setInt(2, supplierId);
                insertStmt.setInt(3, stockLevel);
                insertStmt.setTimestamp(4, lastRestocked);
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    return "Inventory added successfully.";
                } else {
                    return "Failed to add inventory.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public String updateInventory(int inventoryId, int ingredientId, int supplierId, int stockLevel, Timestamp lastRestocked) {
        String query = "SELECT * FROM Inventory WHERE inventory_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, inventoryId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "Inventory entry not found.";
            }

             
        } catch (SQLException e) { 
        }return "Inventory entry not found.";
    }

    public String deleteInventory(int inventoryId) {
        String query = "SELECT * FROM Inventory WHERE inventory_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, inventoryId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "Inventory entry not found.";
            }

            
        } catch (SQLException e) { 
        }    return "Inventory entry not found.";
    }

    public List<String> getInventoryList() {
        List<String> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM Inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
                int ingredientId = rs.getInt("ingredient_id");
                int supplierId = rs.getInt("supplier_id");
                int stockLevel = rs.getInt("stock_level");
                Timestamp lastRestocked = rs.getTimestamp("last_restocked");

                String inventoryDetails = "Inventory ID: " + inventoryId + ", Ingredient ID: " + ingredientId + ", Supplier ID: " + supplierId
                        + ", Stock Level: " + stockLevel + ", Last Restocked: " + lastRestocked;
                inventoryList.add(inventoryDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
    
    public List<String> getInventoryListforai() {
    	List<String> ingredientDetails = new ArrayList<>();
    	try {
    	    Statement stmt = conn.createStatement();
    	    String sql = "SELECT * FROM Ingredients";
    	    ResultSet rs = stmt.executeQuery(sql);
    	    while(rs.next()){
    	        String detail = "Ingredient: " + rs.getString("name") +
    	                        " (ID: " + rs.getInt("ingredient_id") + "), " +
    	                        "Stock: " + rs.getInt("stock_quantity") +
    	                        ", Unit: " + rs.getString("unit") +
    	                        ", Price: $" + rs.getDouble("price");
    	        ingredientDetails.add(detail);
    	    }
    	    rs.close();
    	    stmt.close();
    	} catch(SQLException e) {
    	    e.printStackTrace();
    	}
        return ingredientDetails;
    }

}
