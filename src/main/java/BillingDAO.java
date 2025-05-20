import java.sql.*;

public class BillingDAO {
    private final Connection conn;

    public BillingDAO(Connection conn) {
        this.conn = conn;
    }

    public String addBilling(int orderId, int customerId, double amount, String paymentStatus) {
        String checkQuery = "SELECT COUNT(*) FROM Billing WHERE order_id = ?";
        String insertQuery = "INSERT INTO Billing (order_id, customer_id, amount, payment_status) VALUES (?, ?, ?, ?)";
        
        // First, check for existing record
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, orderId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Billing record already exists for this order";
                }
            }
        } catch (SQLException e) {
            return "Error checking existing billing: " + e.getMessage();
        }

        // Then, insert new record
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, orderId);
            insertStmt.setInt(2, customerId);
            insertStmt.setDouble(3, amount);
            insertStmt.setString(4, paymentStatus);
            insertStmt.executeUpdate();
            return "Billing record added successfully";
        } catch (SQLException e) {
            return "Error adding billing: " + e.getMessage();
        }
    }

    public String updateBilling(int billingId, int orderId, int customerId, double amount, String paymentStatus) {
        String checkQuery = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
        String updateQuery = "UPDATE Billing SET order_id = ?, customer_id = ?, amount = ?, payment_status = ? WHERE billing_id = ?";

        // Check existence
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, billingId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    return "Billing record not found";
                }
            }
        } catch (SQLException e) {
            return "Error checking billing: " + e.getMessage();
        }

        // Perform update
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, orderId);
            updateStmt.setInt(2, customerId);
            updateStmt.setDouble(3, amount);
            updateStmt.setString(4, paymentStatus);
            updateStmt.setInt(5, billingId);
            updateStmt.executeUpdate();
            return "Billing record updated successfully";
        } catch (SQLException e) {
            return "Error updating billing: " + e.getMessage();
        }
    }

    public String deleteBilling(int billingId) {
        String checkQuery = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
        String deleteQuery = "DELETE FROM Billing WHERE billing_id = ?";

        // Check existence
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, billingId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    return "Billing record not found";
                }
            }
        } catch (SQLException e) {
            return "Error checking billing: " + e.getMessage();
        }

        // Perform delete
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, billingId);
            deleteStmt.executeUpdate();
            return "Billing record deleted successfully";
        } catch (SQLException e) {
            return "Error deleting billing: " + e.getMessage();
        }
    }

    public String getBillingById(int billingId) {
        String query = "SELECT * FROM Billing WHERE billing_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, billingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int customerId = rs.getInt("customer_id");
                    double amount = rs.getDouble("amount");
                    String paymentStatus = rs.getString("payment_status");
                    return String.format(
                        "Billing Record: Order ID = %d, Customer ID = %d, Amount = %.2f, Payment Status = %s",
                        orderId, customerId, amount, paymentStatus
                    );
                } else {
                    return "Billing record not found";
                }
            }
        } catch (SQLException e) {
            return "Error retrieving billing: " + e.getMessage();
        }
    }
}
