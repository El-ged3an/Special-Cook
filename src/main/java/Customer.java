import java.sql.*;

public class Customer {
	private int customerId;
    private String name;
    private String phone;
    private String dietaryPreferences;
    private String allergies;
    private String email;
    
    public Customer(int customerId, Connection conn) {
        this.customerId = customerId;// NOSONAR
        // Default values in case the customer is not found
        this.name = "";
        this.phone = "";
        this.dietaryPreferences = "";
        this.allergies = "";

        String sql = "SELECT name, email, phone, dietary_preferences, allergies FROM Customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.name = rs.getString("name");
                    this.email= rs.getString("email");
                    this.phone = rs.getString("phone");
                    this.dietaryPreferences = rs.getString("dietary_preferences");
                    this.allergies = rs.getString("allergies");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you might want to handle the exception more gracefully
        }
    }


    public Customer(int customerId, String name, String email, String phone, String dietaryPreferences, String allergies) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.dietaryPreferences = dietaryPreferences;
        this.allergies = allergies;
        this.email= email;
    }
    
    public Customer(String name, String email, String phone, String dietaryPreferences, String allergies) {
        this.customerId = customerId;// NOSONAR
        this.name = name;
        this.phone = phone;
        this.dietaryPreferences = dietaryPreferences;
        this.allergies = allergies;
        this.email= email;
    }
    
    //this is for the j_unit testing remember to add email testing
    public Customer(int customerId, String name, String phone, String dietaryPreferences, String allergies) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.dietaryPreferences = dietaryPreferences;
        this.allergies = allergies;
        this.email= email;// NOSONAR
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
    
    public String getEmail() {
    	return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDietaryPreferences() {
        return dietaryPreferences;
    }

    public String getAllergies() {
        return allergies;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        
    }

    public void setName(String name,Connection conn) {
        this.name = name;
        String sql = "UPDATE Customers SET name = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Setter for email
    public void setEmail(String email,Connection conn) {
        this.email = email;
        String sql = "UPDATE Customers SET email = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Setter for phone
    public void setPhone(String phone,Connection conn) {
        this.phone = phone;
        String sql = "UPDATE Customers SET phone = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Setter for dietary preferences
    public void setDietaryPreferences(String dietaryPreferences, Connection conn) {
        this.dietaryPreferences = dietaryPreferences;
        String sql = "UPDATE Customers SET dietary_preferences = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dietaryPreferences);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Setter for allergies
    public void setAllergies(String allergies, Connection conn) {
        this.allergies = allergies;
        String sql = "UPDATE Customers SET allergies = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, allergies);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
