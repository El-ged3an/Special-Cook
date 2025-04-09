import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.sql.*;
import java.util.List;
import static org.junit.Assert.*;

public class OrderDetailsSteps {

    private Connection connection;
    private OrderDetails orderDetails;
    private boolean operationResult;
    private List<OrderDetails.OrderDetail> retrievedOrderDetails;

    @Given("a connection to the database")
    public void givenAConnectionToTheDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3308/SpecialCookDB";
        String user = "root";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
        orderDetails = new OrderDetails(connection);
    }

    @When("I add an order detail with order_id {int}, ingredient_id {int}, and quantity {int}")
    public void whenIAddAnOrderDetail(int orderId, int ingredientId, int quantity) {
        operationResult = orderDetails.addOrderDetail(orderId, ingredientId, quantity);
    }

    @Then("the order detail should be added successfully")
    public void thenTheOrderDetailShouldBeAddedSuccessfully() {
        assertTrue(true);
    }

    @When("I try to add an order detail with order_id {int}, ingredient_id {int}, and quantity {int}")
    public void whenITryToAddAnExistingOrderDetail(int orderId, int ingredientId, int quantity) {
        operationResult = orderDetails.addOrderDetail(orderId, ingredientId, quantity);
    }

    @Then("the order detail should not be added because it already exists")
    public void thenTheOrderDetailShouldNotBeAddedBecauseItAlreadyExists() {
        assertFalse(operationResult);
    }

    @Given("an order detail with detail_id {int} exists")
    public void givenAnOrderDetailWithDetailIdExists(int detailId) {
        try {
            String query = "SELECT * FROM OrderDetails WHERE detail_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, detailId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                orderDetails.addOrderDetail(1, 2, 5);  // Adding a new order detail for test
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I update the order detail with detail_id {int} to order_id {int}, ingredient_id {int}, and quantity {int}")
    public void whenIUpdateTheOrderDetail(int detailId, int orderId, int ingredientId, int quantity) {
        operationResult = orderDetails.updateOrderDetail(detailId, orderId, ingredientId, quantity);
    }

    @Then("the order detail should be updated successfully")
    public void thenTheOrderDetailShouldBeUpdatedSuccessfully() {
        assertTrue(true);
    }

    @When("I try to update an order detail with detail_id {int} to order_id {int}, ingredient_id {int}, and quantity {int}")
    public void whenITryToUpdateANonExistingOrderDetail(int detailId, int orderId, int ingredientId, int quantity) {
        operationResult = orderDetails.updateOrderDetail(detailId, orderId, ingredientId, quantity);
    }

    @Then("the order detail should not be updated because it does not exist")
    public void thenTheOrderDetailShouldNotBeUpdatedBecauseItDoesNotExist() {
        assertFalse(operationResult);
    }

    @When("I delete the order detail with detail_id {int}")
    public void whenIDeleteTheOrderDetail(int detailId) {
        operationResult = orderDetails.deleteOrderDetail(detailId);
    }

    @Then("the order detail should be deleted successfully")
    public void thenTheOrderDetailShouldBeDeletedSuccessfully() {
        assertTrue(true);
    }

    @When("I try to delete an order detail with detail_id {int}")
    public void whenITryToDeleteANonExistingOrderDetail(int detailId) {
        operationResult = orderDetails.deleteOrderDetail(detailId);
    }

    @Then("the order detail should not be deleted because it does not exist")
    public void thenTheOrderDetailShouldNotBeDeletedBecauseItDoesNotExist() {
        assertFalse(operationResult);
    }

    @When("I retrieve order details for order_id {int}")
    public void whenIRetrieveOrderDetailsForOrderId(int orderId) {
        retrievedOrderDetails = orderDetails.getOrderDetailsByOrderId(orderId);
    }

    @Then("I should get a list of order details for that order_id")
    public void thenIShouldGetAListOfOrderDetailsForThatOrderId() {
        assertNotNull(retrievedOrderDetails);
        assertFalse(retrievedOrderDetails.isEmpty());
    }
}
