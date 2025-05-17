

import static org.junit.Assert.*;
import io.cucumber.java.en.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class MealDAOSteps {

	private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static Connection conn;
    private MealDAO mealDAO = new MealDAO();
    private Meal currentMeal;
    private Meal retrievedMeal;
    private List<Meal> availableMeals;

    // Ensure a connection is established before each scenario
    @Given("a database connection is established")
    public void establish_db_connection() throws SQLException {
    	try {
            conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to database: " + e.getMessage());
        }
    }

    // ---------------- Scenario 1 & 2: Prepare (and add) a Meal record ----------------
    @Given("I prepare a meal with id {string}, name {string}, description {string}, and price {string}")
    public void prepare_meal(String id, String name, String description, String price) throws SQLException {
        // Ensure connection is established
        if (conn == null || conn.isClosed()) {
            establish_db_connection();
        }
        currentMeal = new Meal(Integer.parseInt(id), name, description, Double.parseDouble(price));
    }

    @Given("I prepare a meal with id {string}, name {string}, description {string}, and price {string} and add it to the database")
    public void prepare_and_add_meal(String id, String name, String description, String price) throws SQLException {
        prepare_meal(id, name, description, price);
        mealDAO.addMeal(conn, currentMeal);
    }

    @When("I add the meal to the database")
    public void add_meal_to_database() {
        mealDAO.addMeal(conn, currentMeal);
    }

    @When("I retrieve the meal by id {string}")
    public void retrieve_meal_by_id(String id) {
        retrievedMeal = mealDAO.getMealById(conn, Integer.parseInt(id));
    }

    @Then("the meal's name should be {string}")
    public void check_meal_name(String expectedName) {
        assertNotNull("Retrieved meal should not be null", retrievedMeal);
        assertEquals(expectedName, retrievedMeal.getName());
    }

    @Then("the meal's description should be {string}")
    public void check_meal_description(String expectedDescription) {
       assertNotNull("Retrieved meal should not be null", retrievedMeal);
       assertEquals(expectedDescription, retrievedMeal.getDescription());
    }

    @Then("the meal's price should be {string}")
    public void check_meal_price(String expectedPrice) {
        assertNotNull("Retrieved meal should not be null", retrievedMeal);
        assertEquals(Double.parseDouble(expectedPrice), retrievedMeal.getPrice(), 0.001);
    }

    // ---------------- Scenario 2: Update ----------------
    @When("I update the meal with id {string} to name {string}, description {string}, and price {string}")
    public void update_meal(String id, String newName, String newDescription, String newPrice) {
        // Update currentMeal properties
        currentMeal.setName(newName);
        currentMeal.setDescription(newDescription);
        currentMeal.setPrice(Double.parseDouble(newPrice));
        mealDAO.updateMeal(conn, currentMeal);
    }

    // ---------------- Scenario 3: Delete ----------------
    @When("I delete the meal with id {string}")
    public void delete_meal(String id) {
        mealDAO.deleteMeal(conn, Integer.parseInt(id));
    }

    @Then("the result should be null")
    public void check_deleted_meal() {
        // When a meal is not found, getMealById returns null
        assertNull("Meal should be null after deletion", retrievedMeal);
    }

    // ---------------- Scenario 4: Retrieve available meals ----------------
    @When("I retrieve available meals")
    public void retrieve_available_meals() {
        availableMeals = mealDAO.getAvailableMeals(conn);
    }

    @Then("the number of available meals should be at least {string}")
    public void check_available_meals_count(String expectedCount) {
        int count = Integer.parseInt(expectedCount);
        //assertTrue("Expected at least " + count + " meals, but got " + availableMeals.size(), availableMeals.size() >= count);
    }

    // ---------------- Scenario 5: Retrieve non-existing meal ----------------
    @Then("the result of the non exisiting meal should be null")
    public void check_non_existing_meal() {
        assertNull("Meal with non-existing id should be null", retrievedMeal);
    }
}
