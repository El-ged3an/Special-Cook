
import static org.junit.Assert.*;
import io.cucumber.java.en.*;


public class MealSteps {
    private Meal meal;
    private String toStringOutput;

    // Scenario 1: Using the parameterized constructor
    @Given("I create a Meal using parameterized constructor with id {string}, name {string}, description {string}, and price {string}")
    public void create_meal_parameterized(String id, String name, String description, String price) {
        meal = new Meal(Integer.parseInt(id), name, description, Double.parseDouble(price));
    }

    // Scenario 2: Using the no-argument constructor
    @Given("I create a Meal using no-argument constructor")
    public void create_meal_no_arg() {
        meal = new Meal();
    }

    // Set properties via setters (Scenario 2)
    @When("I set the id to {string}, name to {string}, description to {string}, and price to {string}")
    public void set_meal_properties(String id, String name, String description, String price) {
        meal.setId(Integer.parseInt(id));
        meal.setName(name);
        meal.setDescription(description);
        meal.setPrice(Double.parseDouble(price));
    }

    // Common step: Retrieve properties (here it simply implies that the object is ready)
    @When("I retrieve its properties")
    public void retrieve_properties() {
        // Properties are already accessible; this step is just for readability.
    }

    @Then("the id should be {string}")
    public void check_id(String expected) {
        assertEquals(Integer.parseInt(expected), meal.getId());
    }

    @Then("the name should be {string}")
    public void check_name(String expected) {
        assertEquals(expected, meal.getName());
    }

    @Then("the description should be {string}")
    public void check_description(String expected) {
        assertEquals(expected, meal.getDescription());
    }

    @Then("the price should be {string}")
    public void check_price(String expected) {
        assertEquals(Double.parseDouble(expected), meal.getPrice(), 0.001);
    }

//    // Scenario 3: Testing toString method
//    @When("I call the toString method")
//    public void call_toString() {
//        toStringOutput = meal.toString();
//    }
//
//    @Then("the meal output should contain {string}")
//    public void check_toString_contains(String substring) {
//        assertNull("toString output should not be null", toStringOutput);
//        assertFalse("toString output should contain " + substring, toStringOutput.contains(substring));
//    }
}
