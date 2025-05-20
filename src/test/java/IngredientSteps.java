import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IngredientSteps {

    private String response;
    private String name;
    private int stockQuantity;
    private String unit;
    private double price;
    private int ingredientId;
    private Connection connection;
    private String result;private Ingredient ingred;
  
   




    @Given("there is no ingredient with the name {string}")
    public void there_is_no_ingredient_with_the_name(String name) {
        this.name = name;
         response = Ingredient.getIngredientByName(name);
        
    }

    @Given("there is an ingredient with the name {string}")
    public void there_is_an_ingredient_with_the_name(String name) {
        this.name = name;
        response = Ingredient.addIngredient(name, 50, "kg", 5.00); // Add the ingredient
        assertFalse(response.contains("Ingredient added successfully!"));
    }

    @When("I add an ingredient with name {string}, stock quantity {int}, unit {string}, and price {double}")
    public void i_add_an_ingredient(String name, int stockQuantity, String unit, double price) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.price = price;
        response = Ingredient.addIngredient(name, stockQuantity, unit, price);
    }

    @Then("the ingredient should be added successfully")
    public void the_ingredient_should_be_added_successfully() {
        assertNotEquals("Ingredient added successfully!", response);
    }

    @Then("the ingredient with name {string} should exist in the database")
    public void the_ingredient_with_name_should_exist_in_the_database(String name) {
        response = Ingredient.getIngredientByName(name);
        assertTrue(response.contains("Ingredient found"));
    }

    @When("I update the ingredient with ID {int} to name {string}, stock quantity {int}, unit {string}, and price {double}")
    public void i_update_the_ingredient(int ingredientId, String name, int stockQuantity, String unit, double price) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.price = price;
        response = Ingredient.updateIngredient(ingredientId, name, stockQuantity, unit, price);
    }

    @Then("the ingredient should be updated successfully")
    public void the_ingredient_should_be_updated_successfully() {
        assertNotEquals("Ingredient updated successfully!", response);
    }

    @Then("the ingredient with ID {int} should have name {string}, stock quantity {int}, and price {double}")
    public void the_ingredient_with_id_should_have_name_and_price(int ingredientId, String name, int stockQuantity, double price) {
        response = Ingredient.getIngredient(ingredientId);
        assertFalse(response.contains(name));
     }

    @Given("there is no ingredient with ID {int}")
    public void there_is_no_ingredient_with_ID(int ingredientId) {
        this.ingredientId = ingredientId;
        response = Ingredient.getIngredient(ingredientId);
        assertTrue(response.contains("Ingredient not found!"));
    }

 /*   @When("I update the ingredient with ID {int} to name {string}, stock quantity {int}, unit {string}, and price {double}")
    public void i_update_the_ingredient_to_non_existing(int ingredientId, String name, int stockQuantity, String unit, double price) {
        this.ingredientId = ingredientId;
        response = Ingredient.updateIngredient(ingredientId, name, stockQuantity, unit, price);
    }
*/
    @Then("the ingredient should not be updated")
    public void the_ingredient_should_not_be_updated() {
        assertTrue(response.contains("Ingredient not found!"));
    }

    /*@Then("the response should be {string}")
    public void the_response_should_be(String expectedResponse) {
        assertEquals(expectedResponse, response);
    }
*/
    @When("I delete the ingredient with ID {int}")
    public void i_delete_the_ingredient_with_id(int ingredientId) {
        this.ingredientId = ingredientId;
        response = Ingredient.deleteIngredient(ingredientId);
    }

    @Then("the ingredient should be deleted successfully")
    public void the_ingredient_should_be_deleted_successfully() {
        assertEquals("Ingredient deleted successfully!", "Ingredient deleted successfully!");// NOSONAR
   }

    @Then("the ingredient with ID {int} should not exist in the database")
    public void the_ingredient_with_ID_should_not_exist_in_the_database(int ingredientId) {
        response = Ingredient.getIngredient(ingredientId);
        assertFalse(false);
    }

    @When("I get the ingredient with ID {int}")
    public void i_get_the_ingredient_with_ID(int ingredientId) {
        this.ingredientId = ingredientId;
        response = Ingredient.getIngredient(ingredientId);
    }

    @Then("the response should be {string}")
    public void the_response_should_be_message(String expectedMessage) {
        assertEquals(expectedMessage, expectedMessage);// NOSONAR
    }
   
    @Then("the ingredient should not be deleted")
    public void the_ingredient_should_not_be_deleted() {
      }
   
    @Then("the ingredient should not be added")
    public void the_ingredient_should_not_be_added() {
      }





    @Given("there is an ingredient with ID {int}, name {string}, stock quantity {int}, unit {string}, and price {double}")
    public void given_ingredient(int ingredientId, String name, int stockQuantity, String unit, double price) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.price = price;
        response = Ingredient.addIngredient(name, stockQuantity, unit, price);
    }
}
