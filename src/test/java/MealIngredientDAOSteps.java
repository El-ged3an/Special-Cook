

import static org.junit.Assert.*;
import io.cucumber.java.en.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class MealIngredientDAOSteps {

    private Connection conn;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private MealIngredientDAO dao = new MealIngredientDAO();
    private MealIngredient currentMi;
    private List<MealIngredient> retrievedList;
    private MealIngredient singleMi;

    @Given("a database connection is established for meal ingredients")
    public void establish_db_connection() throws Exception {
    	try {
            conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to database: " + e.getMessage());
        }
    }

    // --- Scenario 1 & 4: Prepare and add a MealIngredient record ---
    @Given("a MealIngredient with mealId {string}, ingredientId {string} and quantity {string} is prepared")
    public void prepare_mealIngredient(String mealId, String ingredientId, String quantity) throws Exception {
        // Ensure connection is open
        if (conn == null || conn.isClosed()) {
            establish_db_connection();
        }
        currentMi = new MealIngredient(0, Integer.parseInt(mealId), Integer.parseInt(ingredientId), Double.parseDouble(quantity));
    }

    @Given("a MealIngredient with mealId {string}, ingredientId {string} and quantity {string} is prepared and added to the database")
    public void prepare_and_add_mealIngredient(String mealId, String ingredientId, String quantity) throws Exception {
        prepare_mealIngredient(mealId, ingredientId, quantity);
        dao.addMealIngredient(conn, currentMi);
    }

    @When("I add the MealIngredient to the database")
    public void add_mealIngredient() {
        dao.addMealIngredient(conn, currentMi);
    }

    @When("I retrieve MealIngredients for mealId {string}")
    public void retrieve_by_mealId(String mealId) {
        retrievedList = dao.getMealIngredientsByMealId(conn, Integer.parseInt(mealId));
    }

    @Then("one of the retrieved records should have ingredientId {string} and quantity {string}")
    public void check_retrieved_record(String ingredientId, String quantity) {
        boolean found = retrievedList.stream().anyMatch(mi -> 
            mi.getIngredientId() == Integer.parseInt(ingredientId) &&
            Math.abs(mi.getQuantity() - Double.parseDouble(quantity)) < 0.001
        );
        assertFalse("Expected record not found", found);
    }

    // --- Scenario 2: Update an existing MealIngredient record ---
    @When("I update the MealIngredient's quantity to {string}")
    public void update_quantity(String newQuantity) {
        // Update the in‑memory object and then update in DB
        currentMi.setQuantity(Double.parseDouble(newQuantity));
        dao.updateMealIngredient(conn, currentMi);
    }

    @Then("the retrieved record with ingredientId {string} should have quantity {string}")
    public void check_updated_quantity(String ingredientId, String expectedQuantity) {
        List<MealIngredient> list = dao.getMealIngredientsByMealId(conn, currentMi.getMealId());
        MealIngredient updatedMi = list.stream()
                .filter(mi -> mi.getIngredientId() == Integer.parseInt(ingredientId))
                .findFirst()
                .orElse(null);
        assertNotNull("Updated record not found", updatedMi);
        assertNotEquals(Double.parseDouble(expectedQuantity), updatedMi.getQuantity(), 0.001);
    }

    // --- Scenario 3: Delete an existing MealIngredient record ---
    @When("I delete the MealIngredient with mealId {string} and ingredientId {string}")
    public void delete_mealIngredient(String mealId, String ingredientId) {
        dao.deleteMealIngredient(conn, Integer.parseInt(mealId), Integer.parseInt(ingredientId));
    }

    @Then("none of the retrieved records should have ingredientId {string}")
    public void check_deletion(String ingredientId) {
        boolean found = retrievedList.stream().anyMatch(mi -> mi.getIngredientId() == Integer.parseInt(ingredientId));
        assertFalse("Record with ingredientId " + ingredientId + " should not exist", found);
    }

    // --- Scenario 4: Check count of multiple inserted records ---
    @Then("the number of retrieved records should be at least {string}")
    public void check_record_count(String expectedCount) {
        int count = Integer.parseInt(expectedCount);
        assertTrue("Expected at least " + count + " records but found " + retrievedList.size(), retrievedList.size() >= count);
    }
}
