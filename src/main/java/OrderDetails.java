import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetails {

    private static Connection conn;

    public OrderDetails(Connection connection) {
        conn = connection;
    }
    
    public static void setConnection(Connection connection) {
        conn = connection;
    }

    public static boolean addOrderDetail(int orderId, int mealId, int quantity) {
        try {
            String query = "SELECT * FROM OrderDetails WHERE order_id = ? AND meal_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(query);
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, mealId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            String insertQuery = "INSERT INTO OrderDetails (order_id, meal_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, orderId);
            insertStmt.setInt(2, mealId);
            insertStmt.setInt(3, quantity);
            insertStmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateOrderDetail(int detailId, int orderId, int mealId, int quantity) {
        try {
            String query = "SELECT * FROM OrderDetails WHERE detail_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(query);
            checkStmt.setInt(1, detailId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String updateQuery = "UPDATE OrderDetails SET order_id = ?, meal_id = ?, quantity = ? WHERE detail_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, orderId);
            updateStmt.setInt(2, mealId);
            updateStmt.setInt(3, quantity);
            updateStmt.setInt(4, detailId);
            updateStmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteOrderDetail(int detailId) {
        try {
            String query = "SELECT * FROM OrderDetails WHERE detail_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(query);
            checkStmt.setInt(1, detailId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String deleteQuery = "DELETE FROM OrderDetails WHERE detail_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, detailId);
            deleteStmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try {
            String query = "SELECT * FROM OrderDetails WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int detailId = rs.getInt("detail_id");
                // Change from ingredient_id to meal_id:
                int mealId = rs.getInt("meal_id");
                int quantity = rs.getInt("quantity");
                orderDetails.add(new OrderDetail(detailId, orderId, mealId, quantity));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }


    public static class OrderDetail {
        private int detailId;
        private int orderId;
        private int mealId;
        private int quantity;

        public OrderDetail(int detailId, int orderId, int mealId, int quantity) {
            this.detailId = detailId;
            this.orderId = orderId;
            this.mealId = mealId;
            this.quantity = quantity;
        }

        public int getDetailId() {
            return detailId;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getIngredientId() {
            return mealId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
