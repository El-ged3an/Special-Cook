import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealIngredientDAO {

    // Create: Insert a new meal ingredient record into the database
	public void addMealIngredient(Connection conn, MealIngredient mealIngredient) {
	    String sql = "INSERT INTO meal_ingredients (meal_id, ingredient_id, quantity) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, mealIngredient.getMealId());
	        pstmt.setInt(2, mealIngredient.getIngredientId());
	        pstmt.setDouble(3, mealIngredient.getQuantity());
	        pstmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}


    // Update: Update an existing meal ingredient record
    public void updateMealIngredient(Connection conn, MealIngredient mealIngredient) {
        String sql = "UPDATE meal_ingredients SET ingredient_id = ?, quantity = ? WHERE meal_id = ? AND ingredient_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mealIngredient.getIngredientId());
            pstmt.setDouble(2, mealIngredient.getQuantity());
            pstmt.setInt(3, mealIngredient.getMealId());
            pstmt.setInt(4, mealIngredient.getIngredientId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMealIngredient(Connection conn, int mealId, int ingredientId) {
        String sql = "DELETE FROM meal_ingredients WHERE meal_id = ? AND ingredient_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mealId);
            pstmt.setInt(2, ingredientId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // Optional: Retrieve all meal ingredients for a given meal id
    public List<MealIngredient> getMealIngredientsByMealId(Connection conn, int mealId) {
        List<MealIngredient> mealIngredients = new ArrayList<>();
        String sql = "SELECT meal_id, ingredient_id, quantity FROM meal_ingredients WHERE meal_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mealId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MealIngredient mi = new MealIngredient();
                    // Set the mealId, ingredientId, and quantity from the result set
                    mi.setMealId(rs.getInt("meal_id"));
                    mi.setIngredientId(rs.getInt("ingredient_id"));
                    mi.setQuantity(rs.getDouble("quantity"));
                    mealIngredients.add(mi);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mealIngredients;
    }

}
