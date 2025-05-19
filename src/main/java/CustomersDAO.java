import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomersDAO {

    private Connection connection;

    public CustomersDAO() {
        this.connection = getConnection();
    }
    
    public CustomersDAO(Connection conn) {
        this.connection = conn;
    }
    

    private Connection getConnection() {
        String url = "jdbc:mysql://localhost:3308/SpecialCookDB";
        String user = "root";
        String password = "";
        try {
            if (connection == null || connection.isClosed()) {
                // Load the JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean addCustomer(String name, String phone, String dietaryPreferences, String allergies) {
        String sql = "INSERT INTO Customers (name, phone, dietary_preferences, allergies) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, dietaryPreferences);
            stmt.setString(4, allergies);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addCustomer(String name, String email, String phone ,String dietaryPreferences, String allergies) {
        String sql = "INSERT INTO Customers (name, email, phone, dietary_preferences, allergies) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, dietaryPreferences);
            stmt.setString(5, allergies);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomer(int customerId, String name, String phone, String dietaryPreferences, String allergies) {
        String sql = "UPDATE Customers SET name = ?, phone = ?, dietary_preferences = ?, allergies = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, dietaryPreferences);
            stmt.setString(4, allergies);
            stmt.setInt(5, customerId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) throws SQLException {
    // Delete related records in the OrderDetails table first
    String deleteOrderDetailsQuery =
      "DELETE FROM `OrderDetails` WHERE order_id IN (SELECT order_id FROM `Orders` WHERE customer_id = ?)";
    try (PreparedStatement stmt = connection.prepareStatement(deleteOrderDetailsQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }

    // Delete related records in the Tasks table
    String deleteTasksQuery =
      "DELETE FROM `Tasks` WHERE order_id IN (SELECT order_id FROM `Orders` WHERE customer_id = ?)";
    try (PreparedStatement stmt = connection.prepareStatement(deleteTasksQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }

    // Delete related records in the Billing table
    String deleteBillingQuery =
      "DELETE FROM `Billing` WHERE customer_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(deleteBillingQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }

    // Delete related records in the Orders table
    String deleteOrdersQuery =
      "DELETE FROM `Orders` WHERE customer_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(deleteOrdersQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }

    // Delete related records in the Notifications table
    String deleteNotificationsQuery =
      "DELETE FROM `Notifications` WHERE user_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(deleteNotificationsQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
    }

    // Now delete the customer itself
    String deleteCustomerQuery =
      "DELETE FROM `Customers` WHERE customer_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(deleteCustomerQuery)) {
        stmt.setInt(1, customerId);
        stmt.executeUpdate();
        return true;
    }
}



    public boolean customerExists(int customerId) {
        String sql = "SELECT 1 FROM Customers WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> viewCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";// NOSONAR
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("dietary_preferences"),
                        rs.getString("allergies")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
