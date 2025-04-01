import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierPaymentsDAO {

    private Connection conn;

    public SupplierPaymentsDAO(Connection connection) {
        this.conn = connection;
    }

    public boolean addPayment(int supplierId, double amount, String status) {
        String checkQuery = "SELECT * FROM SupplierPayments WHERE supplier_id = ? AND status = 'Pending'";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, supplierId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }

            String query = "INSERT INTO SupplierPayments (supplier_id, amount, payment_date, status) VALUES (?, ?, NOW(), ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, supplierId);
                stmt.setDouble(2, amount);
                stmt.setString(3, status);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePayment(int paymentId, double amount, String status) {
        String checkQuery = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, paymentId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return false;
            }

            String query = "UPDATE SupplierPayments SET amount = ?, status = ? WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, status);
                stmt.setInt(3, paymentId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePayment(int paymentId) {
        String checkQuery = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, paymentId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return false;
            }

            String query = "DELETE FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getAllPayments() {
        String query = "SELECT * FROM SupplierPayments";
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getPaymentById(int paymentId) {
        String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<String> getAllPaymentDetails() {
        List<String> payments = new ArrayList<>();
        String query = "SELECT * FROM SupplierPayments";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             while (rs.next()) {
                 int paymentId = rs.getInt("payment_id");
                 int supplierId = rs.getInt("supplier_id");
                 double amount = rs.getDouble("amount");
                 String status = rs.getString("status");
                 payments.add("Payment ID: " + paymentId + 
                              ", Supplier ID: " + supplierId +
                              ", Amount: $" + amount + 
                              ", Status: " + status);
             }
        } catch(SQLException e) {
             e.printStackTrace();
        }
        return payments;
    }

}
