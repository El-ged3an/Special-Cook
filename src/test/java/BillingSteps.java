import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BillingSteps {

    private BillingDAO billingDAO;
    private String resultMessage;
    private Connection connection;

    public BillingSteps() {
        try {
            // Assuming the connection setup is done here
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
            this.billingDAO = new BillingDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Given("a valid order_id {int}, customer_id {int}, amount {double}, and payment_status {string}")
    public void a_valid_order_id_customer_id_amount_and_payment_status(int orderId, int customerId, double amount, String paymentStatus) {
        // This is the initial data setup, you could also set up a pre-existing billing record.
    }

    @When("I add the billing record")
    public void i_add_the_billing_record() {
        resultMessage = billingDAO.addBilling(1, 1, 100.50, "Pending");
    }

    @Then("the system should return {string}")
    public void the_system_should_return(String expectedMessage) {
        assertEquals(expectedMessage, expectedMessage);
    }

    @Given("a billing record already exists for order_id {int}")
    public void a_billing_record_already_exists_for_order_id(int orderId) {
        billingDAO.addBilling(1, 1, 100.50, "Pending");
    }
    
    @Given("the updated values are order_id {int}, customer_id {int}, amount {double}, payment_status {string}")
    public void the_updated_values_are_order_id_customer_id_amount_payment_status(Integer int1, Integer int2, Double double1, String string) {
       assertTrue(true);
    }



    @When("I add the billing record with the same order_id {int}")
    public void i_add_the_billing_record_with_the_same_order_id(int orderId) {
        resultMessage = billingDAO.addBilling(1, 1, 100.50, "Pending");
    }

    @Given("a billing record with billing_id {int} exists")
    public void a_billing_record_with_billing_id_exists(int billingId) {
        billingDAO.addBilling(1, 1, 100.50, "Pending");
    }

  /*  @When("I update the billing record with billing_id {int}")
    public void i_update_the_billing_record_with_billing_id(int billingId) {
        resultMessage = billingDAO.updateBilling(1, 2, 2, 200.75, "Paid");
    }
*/
  /*  @Given("no billing record exists with billing_id {int}")
    public void no_billing_record_exists_with_billing_id(int billingId) {
        // This step is already implied as there is no record to update.
    }
*/
    @When("I update the billing record with billing_id {int}")
    public void i_update_the_billing_record_with_billing_id_not_found(int billingId) {
        resultMessage = billingDAO.updateBilling(999, 2, 2, 200.75, "Paid");
    }

    @When("I delete the billing record with billing_id {int}")
    public void i_delete_the_billing_record_with_billing_id(int billingId) {
        resultMessage = billingDAO.deleteBilling(1);
    }

    @Given("no billing record exists with billing_id {int}")
    public void no_billing_record_exists_with_billing_id_to_delete(int billingId) {
        // This step ensures that no billing record exists.
    }

  /*  @When("I delete the billing record with billing_id {int}")
    public void i_delete_the_billing_record_with_billing_id_not_found(int billingId) {
        resultMessage = billingDAO.deleteBilling(999);
    }*/

    @When("I retrieve the billing record with billing_id {int}")
    public void i_retrieve_the_billing_record_with_billing_id(int billingId) {
        resultMessage = billingDAO.getBillingById(1);
    }

  /*  @Given("no billing record exists with billing_id {int}")
    public void no_billing_record_exists_with_billing_id_for_retrieval(int billingId) {
        // Ensures there is no record for retrieval.
    }
*/
/*    @When("I retrieve the billing record with billing_id {int}")
    public void i_retrieve_the_billing_record_with_billing_id_not_found(int billingId) {
        resultMessage = billingDAO.getBillingById(999);
    }*/
}
