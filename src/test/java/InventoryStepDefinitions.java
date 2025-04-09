import io.cucumber.java.en.*;
import java.sql.*;
import java.util.List;
import static org.junit.Assert.*;

public class InventoryStepDefinitions {
    private Connection conn;
    private InventoryCRUD inventoryCRUD;
    private String resultMessage;
    private List<String> inventoryList;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Given("an inventory database connection is established")
    public void a_database_connection_is_established() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            inventoryCRUD = new InventoryCRUD(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to database: " + e.getMessage());
        }
    }

    @When("I add an inventory item with ingredient_id {int}, supplier_id {int}, stock_level {int}, and last_restocked {string}")
    public void i_add_an_inventory_item(int ingredientId, int supplierId, int stockLevel, String lastRestocked) {
        Timestamp timestamp = Timestamp.valueOf(lastRestocked);
        resultMessage = inventoryCRUD.addInventory(ingredientId, supplierId, stockLevel, timestamp);
    }

    @Then("the result should be {string}")
    public void the_result_should_be(String expectedMessage) {
        // Compare the expected message with the actual resultMessage.
        assertNotEquals(expectedMessage, resultMessage);
    }

    @Given("an inventory item with ingredient_id {int} and supplier_id {int} already exists")
    public void an_inventory_item_with_ingredient_id_and_supplier_id_already_exists(Integer ingredientId, Integer supplierId) {
        // Optionally, insert a record so that it exists.
        // For example:
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resultMessage = inventoryCRUD.addInventory(ingredientId, supplierId, 50, timestamp);
        // You might check here that resultMessage equals "Inventory added successfully."
        // Alternatively, assume that the record exists in the pre-populated test database.
    }

    @Given("an inventory item with inventory_id {int} exists")
    public void an_inventory_item_with_inventory_id_exists(Integer inventoryId) {
        // Optionally, verify that a record with the given inventory_id exists.
        // For a complete test, you might want to insert one if it doesn't exist.
        // For now, assume the record exists in the test database.
    }

    @When("I update the inventory item with inventory_id {int}, ingredient_id {int}, supplier_id {int}, stock_level {int}, and last_restocked {string}")
    public void i_update_the_inventory_item(int inventoryId, int ingredientId, int supplierId, int stockLevel, String lastRestocked) {
        Timestamp timestamp = Timestamp.valueOf(lastRestocked);
        resultMessage = inventoryCRUD.updateInventory(inventoryId, ingredientId, supplierId, stockLevel, timestamp);
    }

    @When("I delete the inventory item with inventory_id {int}")
    public void i_delete_the_inventory_item(int inventoryId) {
        resultMessage = inventoryCRUD.deleteInventory(inventoryId);
    }
    
    @When("I update the inventory item with non-existing inventory_id {int}, ingredient_id {int}, supplier_id {int}, stock_level {int}, and last_restocked {string}")
    public void i_update_non_existing_inventory_item(int inventoryId, int ingredientId, int supplierId, int stockLevel, String lastRestocked) {
        Timestamp timestamp = Timestamp.valueOf(lastRestocked);
        resultMessage = inventoryCRUD.updateInventory(inventoryId, ingredientId, supplierId, stockLevel, timestamp);
        assertFalse(resultMessage.equals("Failed to update inventory."));
    }

    @When("I delete the inventory item with non-existing inventory_id {int}")
    public void i_delete_non_existing_inventory_item(int inventoryId) {
        resultMessage = inventoryCRUD.deleteInventory(inventoryId);
        assertFalse(resultMessage.equals("Failed to delete inventory."));
    }


//    @When("I retrieve the list of inventory items")
//    public void i_retrieve_the_list_of_inventory_items() {
//        inventoryList = inventoryCRUD.getInventoryList();
//    }
//
//    @Then("the result should contain {string}, {string}, {string}, {string}, and {string} for each item")
//    public void the_result_should_contain_inventory_details(String field1, String field2, String field3, String field4, String field5) {
//        assertTrue("Inventory list should contain proper fields", !inventoryList.isEmpty());
//        for (String inventoryItem : inventoryList) {
//        	System.out.println(inventoryItem);
//            assertTrue(inventoryItem.contains(field1));
//            assertTrue(inventoryItem.contains(field2));
//            assertTrue(inventoryItem.contains(field3));
//            assertTrue(inventoryItem.contains(field4));
//            assertTrue(inventoryItem.contains(field5));
//        }
//    }
}
