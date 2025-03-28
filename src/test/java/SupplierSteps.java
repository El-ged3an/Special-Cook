import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;
import java.sql.*;

public class SupplierSteps {

    private SupplierCRUD supplierCRUD;
    private Connection conn;
    private String message;
    private Supplier supplier;

    @Given("I have a connection to the database")
    public void i_have_a_connection_to_the_database() throws SQLException {
        // Define the connection to the database
        String url = "jdbc:mysql://localhost:3308/SpecialCookDB";
        String username = "root"; // Replace with your DB username
        String password = ""; // Replace with your DB password
        conn = DriverManager.getConnection(url, username, password);
        supplierCRUD = new SupplierCRUD();
    }

    @When("I create a supplier with name {string} and contact {string}")
    public void i_create_a_supplier_with_name_and_contact(String name, String contactInfo) {
        try {
            supplierCRUD.createSupplier(name, contactInfo);
            message = "Supplier created successfully.";
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    @Then("the supplier {string} should be added to the Suppliers table")
    public void the_supplier_should_be_added_to_the_suppliers_table(String name) {
        try {
            supplier = supplierCRUD.getSupplierByName(name);
            assertNotNull(supplier);
            assertEquals(name, supplier.getName());
        } catch (SQLException e) {
            fail("Supplier creation failed.");
        }
    }
 
    @When("I get the supplier with ID {int}")
    public void i_get_the_supplier_with_id(int supplierId) {
        try {
            supplier = supplierCRUD.getSupplier(supplierId);
        } catch (SQLException e) {
            fail("Failed to retrieve supplier.");
        }
    }

    @Then("I should get the supplier details with ID {int}")
    public void i_should_get_the_supplier_details_with_id(int supplierId) {
        assertNotNull(supplier);
        assertEquals(supplierId, supplier.getSupplierId());
    }
/*
    @When("I get the supplier with ID {int}")
    public void i_get_the_supplier_with_id_for_non_existing_supplier(int supplierId) {
        try {
            supplier = supplierCRUD.getSupplier(supplierId);
        } catch (SQLException e) {
            fail("Failed to retrieve supplier.");
        }
    }
*/
    @Then("I should receive {string}")
    public void i_should_receive_supplier_not_found(String message) {
        assertNull(supplier);
    }

    @When("I update the supplier with ID {int} to name {string} and contact {string}")
    public void i_update_the_supplier_with_id_to_name_and_contact(int supplierId, String name, String contactInfo) {
        try {
            supplierCRUD.updateSupplier(supplierId, name, contactInfo);
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    @Then("the supplier with ID {int} should have name {string} and contact {string}")
    public void the_supplier_with_id_should_have_name_and_contact(int supplierId, String name, String contactInfo) {
        try {
            supplier = supplierCRUD.getSupplier(supplierId);
            assertEquals(name, supplier.getName());
            assertEquals(contactInfo, supplier.getContactInfo());
        } catch (SQLException e) {
            fail("Supplier update failed.");
        }
    }

 /*   @When("I update the supplier with ID {int} to name {string} and contact {string}")
    public void i_update_the_supplier_with_id_to_name_and_contact_for_non_existing_supplier(int supplierId, String name, String contactInfo) {
        try {
            supplierCRUD.updateSupplier(supplierId, name, contactInfo);
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }
*/
  /*  @Then("I should receive {string}")
    public void i_should_receive_supplier_not_found_for_update(String message) {
        assertTrue(message.contains("Supplier not found."));
    }
*/
    @When("I delete the supplier with ID {int}")
    public void i_delete_the_supplier_with_id(int supplierId) {
        try {
            supplierCRUD.deleteSupplier(supplierId);
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    @Then("the supplier with ID {int} should be removed from the Suppliers table")
    public void the_supplier_with_id_should_be_removed_from_the_suppliers_table(int supplierId) {
        try {
            supplier = supplierCRUD.getSupplier(supplierId);
            assertNull(supplier);
        } catch (SQLException e) {
            fail("Supplier deletion failed.");
        }
    }
/*
    @When("I delete the supplier with ID {int}")
    public void i_delete_the_supplier_with_id_and_related_data(int supplierId) {
        try {
            supplierCRUD.deleteSupplier(supplierId);
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }*/

    @Then("the supplier with ID {int} and all related inventory and supplier payments should be deleted from the database")
    public void the_supplier_with_id_and_all_related_inventory_and_supplier_payments_should_be_deleted_from_the_database(int supplierId) {
        try {
            supplier = supplierCRUD.getSupplier(supplierId);
            assertNull(supplier);
            // Implement checks for related Inventory and SupplierPayments tables
        } catch (SQLException e) {
            fail("Related data deletion failed.");
        }
    }
}
