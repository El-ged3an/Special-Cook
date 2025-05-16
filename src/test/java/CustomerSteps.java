import io.cucumber.java.en.*;
import static org.junit.Assert.*;

import java.sql.SQLException;

public class CustomerSteps {

    private CustomersDAO customersDAO = new CustomersDAO();
    private Customer customer;
    private boolean operationResult;

    @Given("I have the following customer details:")
    public void i_have_the_following_customer_details(io.cucumber.datatable.DataTable dataTable) {
        String name = dataTable.cell(1, 0);
        String phone = dataTable.cell(1, 1);
        String dietaryPreferences = dataTable.cell(1, 2);
        String allergies = dataTable.cell(1, 3);
        customer = new Customer(0, name, phone, dietaryPreferences, allergies);
    }

    @When("I add the customer")
    public void i_add_the_customer() {
        operationResult = customersDAO.addCustomer(customer.getName(), customer.getPhone(), customer.getDietaryPreferences(), customer.getAllergies());
    }

    @Then("the customer should be added successfully")
    public void the_customer_should_be_added_successfully() {
        assertTrue(operationResult);
    }

    @Given("there are existing customers")
    public void there_are_existing_customers() {
        // You may want to pre-populate data or mock the database here
    }

    @When("I view all customers")
    public void i_view_all_customers() {
        // Fetch and validate customers from the database
    }

    @Then("I should see a list of customers")
    public void i_should_see_a_list_of_customers() {
        assertNotNull(customersDAO.viewCustomers());
    }

    @Given("I have an existing customer with the following details:")
    public void i_have_an_existing_customer_with_the_following_details(io.cucumber.datatable.DataTable dataTable) {
        // Populate the customer with existing data (in real scenario, customer exists in DB)
        int customerId = Integer.parseInt(dataTable.cell(1, 0));
        String name = dataTable.cell(1, 1);
        String phone = dataTable.cell(1, 2);
        String dietaryPreferences = dataTable.cell(1, 3);
        String allergies = dataTable.cell(1, 4);
        customer = new Customer(customerId, name, phone, dietaryPreferences, allergies);
    }

    @When("I update the customer with new details:")
    public void i_update_the_customer_with_new_details(io.cucumber.datatable.DataTable dataTable) {
        String name = dataTable.cell(1, 0);
        String phone = dataTable.cell(1, 1);
        String dietaryPreferences = dataTable.cell(1, 2);
        String allergies = dataTable.cell(1, 3);
        operationResult = customersDAO.updateCustomer(customer.getCustomerId(), name, phone, dietaryPreferences, allergies);
    }

    @Then("the customer's details should be updated successfully")
    public void the_customer_s_details_should_be_updated_successfully() {
        assertTrue(operationResult);
    }

    @When("I delete the customer with customer_id {int}")
    public void i_delete_the_customer_with_customer_id(int customerId) {
        try {
			operationResult = customersDAO.deleteCustomer(customerId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Then("the customer should be deleted successfully")
    public void the_customer_should_be_deleted_successfully() {
        //assertTrue(operationResult);
    }

    @When("I check if the customer with customer_id {int} exists")
    public void i_check_if_the_customer_with_customer_id_exists(int customerId) {
        operationResult = customersDAO.customerExists(customerId);
    }

    @Then("the customer should exist")
    public void the_customer_should_exist() {
        assertTrue(operationResult);
    }
}
