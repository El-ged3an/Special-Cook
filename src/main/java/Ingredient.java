import java.sql.*;

public class Ingredient {

    private static final String URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    private static Connection connection;

    static {
        try {
            // Replace with your database connection details
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String addIngredient(String name, int stockQuantity, String unit, double price) {
        try (Connection connection = connect()) {
            String checkQuery = "SELECT * FROM Ingredients WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, name);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return "Ingredient already exists!";
                }
            }

            String insertQuery = "INSERT INTO Ingredients (name, stock_quantity, unit, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setString(1, name);
                stmt.setInt(2, stockQuantity);
                stmt.setString(3, unit);
                stmt.setDouble(4, price);
                stmt.executeUpdate();
            }

            return "Ingredient added successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String updateIngredient(int ingredientId, String name, int stockQuantity, String unit, double price) {
        try (Connection connection = connect()) {
            String checkQuery = "SELECT * FROM Ingredients WHERE ingredient_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, ingredientId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Ingredient not found!";
                }
            }

            String updateQuery = "UPDATE Ingredients SET name = ?, stock_quantity = ?, unit = ?, price = ? WHERE ingredient_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, name);
                stmt.setInt(2, stockQuantity);
                stmt.setString(3, unit);
                stmt.setDouble(4, price);
                stmt.setInt(5, ingredientId);
                stmt.executeUpdate();
            }

            return "Ingredient updated successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String deleteIngredient(int ingredientId) {
        try (Connection connection = connect()) {
            String checkQuery = "SELECT * FROM Ingredients WHERE ingredient_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, ingredientId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Ingredient not found!";
                }
            }

            String deleteQuery = "DELETE FROM Ingredients WHERE ingredient_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                stmt.setInt(1, ingredientId);
                stmt.executeUpdate();
            }

            return "Ingredient deleted successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String getIngredient(int ingredientId) {
        try (Connection connection = connect()) {
            String selectQuery = "SELECT * FROM Ingredients WHERE ingredient_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
                stmt.setInt(1, ingredientId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return "Ingredient found: " + rs.getString("name") + ", " + rs.getInt("stock_quantity") + " " + rs.getString("unit");
                } else {
                    return "Ingredient not found!";
                }
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
    public static String getIngredientByName(String name) {
        try {
            String query = "SELECT * FROM Ingredients WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int ingredientId = resultSet.getInt("ingredient_id");
                String unit = resultSet.getString("unit");
                int stockQuantity = resultSet.getInt("stock_quantity");
                double price = resultSet.getDouble("price");
                return String.format("Ingredient found: ID %d, %s, %d %s, Price: %.2f", ingredientId, name, stockQuantity, unit, price);
            } else {
                return "Ingredient not found!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while retrieving ingredient by name.";
        }
    }
}
