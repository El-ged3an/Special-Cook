import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private Timestamp orderDate;
    private double totalPrice;
    
    public Order() {
    	
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public static List<Order> getOrdersByCustomerId(int customerId, Connection conn) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE customer_id = ?";// NOSONAR
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    
    public static boolean createOrder(Order order, Connection conn) {
        String disableFK = "SET FOREIGN_KEY_CHECKS = 0;";
        String sql = "INSERT INTO Orders (customer_id, order_date, total_price) VALUES (?, ?, ?)";
        String enableFK = "SET FOREIGN_KEY_CHECKS = 1;";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(disableFK);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getCustomerId());
            ps.setTimestamp(2, order.getOrderDate());
            ps.setDouble(3, order.getTotalPrice());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        order.setOrderId(generatedId);
                    }
                }
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(enableFK);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }




//    public static Order readOrder(int orderId,Connection conn) {
//        String sql = "SET FOREIGN_KEY_CHECKS = 0; SELECT * FROM Orders WHERE order_id = ?; SET FOREIGN_KEY_CHECKS = 1;";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, orderId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                Order order = new Order();
//                order.setOrderId(rs.getInt("order_id"));
//                order.setCustomerId(rs.getInt("customer_id"));
//                order.setOrderDate(rs.getTimestamp("order_date"));
//                order.setTotalPrice(rs.getDouble("total_price"));
//                return order;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
    public static Order readOrder(int orderId, Connection conn) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    

    public static List<Order> readAllOrders(Connection conn) {
        String sql = "SELECT * FROM Orders";
        List<Order> orders = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    public static boolean updateOrder(Order order, Connection conn) {
        String disableFK = "SET FOREIGN_KEY_CHECKS = 0;";
        String sql = "UPDATE Orders SET customer_id = ?, order_date = ?, total_price = ? WHERE order_id = ?";
        String enableFK = "SET FOREIGN_KEY_CHECKS = 1;";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(disableFK);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getCustomerId());
            ps.setTimestamp(2, order.getOrderDate());
            ps.setDouble(3, order.getTotalPrice());
            ps.setInt(4, order.getOrderId());
            int rows = ps.executeUpdate();
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(enableFK);
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteOrder(int orderId, Connection conn) {
        String disableFK = "SET FOREIGN_KEY_CHECKS = 0;";
        String deleteSQL = "DELETE FROM Orders WHERE order_id = ?";
        String enableFK = "SET FOREIGN_KEY_CHECKS = 1;";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(disableFK);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(enableFK);
            }
            
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public static Order getOrderByTimestamp(Connection conn, Timestamp timestamp, int customerId) {
        Order order = null;
        String sql = "SELECT * FROM Orders WHERE order_date = ? AND customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, timestamp);
            ps.setInt(2, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

}
