import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MealDAO {

    // Create: Inserts a new meal record into the database using an existing connection.
    public void addMeal(Connection conn, Meal meal) {
        String sql = "INSERT INTO meals (meal_id, name, description, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, meal.getId());
            pstmt.setString(2, meal.getName());
            pstmt.setString(3, meal.getDescription());
            pstmt.setDouble(4, meal.getPrice());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Read: Retrieves a meal record by its id using an existing connection.
    public Meal getMealById(Connection conn, int id) {
        String sql = "SELECT meal_id, name, description, price FROM meals WHERE meal_id = ?";
        Meal meal = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    meal = new Meal(
                        rs.getInt("meal_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return meal;
    }



    // Update: Updates an existing meal record in the database using an existing connection.
    public void updateMeal(Connection conn, Meal meal) {
        String sql = "UPDATE meals SET name = ?, description = ?, price = ? WHERE meal_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, meal.getName());
            pstmt.setString(2, meal.getDescription());
            pstmt.setDouble(3, meal.getPrice());
            pstmt.setInt(4, meal.getId());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Delete: Removes a meal record from the database using an existing connection.
    public void deleteMeal(Connection conn, int id) {
        String sql = "DELETE FROM meals WHERE meal_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public List<Meal> getAvailableMeals(Connection conn) {
        List<Meal> availableMeals = new ArrayList<>();
        String mealQuery = "SELECT * FROM meals";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(mealQuery)) {
            while (rs.next()) {
                Meal meal = new Meal();
                meal.setId(rs.getInt("meal_id"));
                meal.setName(rs.getString("name"));
                meal.setDescription(rs.getString("description"));
                meal.setPrice(rs.getDouble("price"));
                availableMeals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableMeals;
    }

    
}
