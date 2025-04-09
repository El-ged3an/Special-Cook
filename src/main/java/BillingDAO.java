import java.sql.*;

public class BillingDAO {
    private Connection conn;

    public BillingDAO(Connection conn) {
        this.conn = conn;
    }

    public String addBilling(int orderId, int customerId, double amount, String paymentStatus) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Billing WHERE order_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, orderId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "Billing record already exists for this order";
            }

            String query = "INSERT INTO Billing (order_id, customer_id, amount, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderId);
            stmt.setInt(2, customerId);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentStatus);
            stmt.executeUpdate();
            return "Billing record added successfully";
        } catch (SQLException e) {
            return "Error adding billing: " + e.getMessage();
        }
    }

    public String updateBilling(int billingId, int orderId, int customerId, double amount, String paymentStatus) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, billingId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return "Billing record not found";
            }

            String query = "UPDATE Billing SET order_id = ?, customer_id = ?, amount = ?, payment_status = ? WHERE billing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderId);
            stmt.setInt(2, customerId);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentStatus);
            stmt.setInt(5, billingId);
            stmt.executeUpdate();
            return "Billing record updated successfully";
        } catch (SQLException e) {
            return "Error updating billing: " + e.getMessage();
        }
    }

    public String deleteBilling(int billingId) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, billingId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return "Billing record not found";
            }

            String query = "DELETE FROM Billing WHERE billing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, billingId);
            stmt.executeUpdate();
            return "Billing record deleted successfully";
        } catch (SQLException e) {
            return "Error deleting billing: " + e.getMessage();
        }
    }

    public String getBillingById(int billingId) {
        try {
            String query = "SELECT * FROM Billing WHERE billing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, billingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                double amount = rs.getDouble("amount");
                String paymentStatus = rs.getString("payment_status");
                return "Billing Record: Order ID = " + orderId + ", Customer ID = " + customerId + ", Amount = " + amount + ", Payment Status = " + paymentStatus;
            } else {
                return "Billing record not found";
            }
        } catch (SQLException e) {
            return "Error retrieving billing: " + e.getMessage();
        }
    }
}
