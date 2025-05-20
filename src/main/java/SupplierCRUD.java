import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierCRUD {

    private Connection conn;

    public SupplierCRUD() throws SQLException {
        // Define the connection to the database
        String url = "jdbc:mysql://localhost:3308/SpecialCookDB";
        String username = "root"; // Replace with your DB username
        String password = ""; // NOSONAR
        conn = DriverManager.getConnection(url, username, password);// NOSONAR
    }

    public String createSupplier(String name, String contactInfo) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Suppliers WHERE name = ?";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Supplier count for '" + name + "': " + count);
                
                if (count > 0) {
                    return "Supplier already exists.";  // Prevent duplicate
                }
            }
        }

        String insertQuery = "INSERT INTO Suppliers (name, contact_info) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, contactInfo);
            stmt.executeUpdate();
        }

        return "Supplier created successfully.";
    }

    public Supplier getSupplier(int supplierId) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE supplier_id = ?";// NOSONAR
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contact_info"));
            }
        }
        return null;
    }

    public Supplier getSupplierByName(String name) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contact_info"));
            }
        }
        return null;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Suppliers";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                suppliers.add(new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contact_info")));
            }
        }
        return suppliers;
    }

    public void updateSupplier(int supplierId, String name, String contactInfo) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, supplierId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Supplier not found.");
                return;
            }
        }

        String query = "UPDATE Suppliers SET name = ?, contact_info = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, contactInfo);
            stmt.setInt(3, supplierId);
            stmt.executeUpdate();
        }
    }

    public void deleteSupplier(int supplierId) throws SQLException {
        String deleteInventoryQuery = "DELETE FROM Inventory WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteInventoryQuery)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
        }

        String deleteSupplierPaymentsQuery = "DELETE FROM SupplierPayments WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSupplierPaymentsQuery)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
        }

        String deleteSupplierQuery = "DELETE FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSupplierQuery)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
        }
    }
}

class Supplier {
    private int supplierId;
    private String name;
    private String contactInfo;

    public Supplier(int supplierId, String name, String contactInfo) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
