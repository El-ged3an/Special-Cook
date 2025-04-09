import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.sql.*;

public class KitchenManagerSteps {

    private Connection conn;
    private KitchenManagers kitchenManagers;
    private String response;
    private int managerId;

    public KitchenManagerSteps() {
        try {
            // Establish connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
            kitchenManagers = new KitchenManagers(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Given("a connection to the database is established")
    public void a_connection_to_the_database_is_established() {
        assertNotNull(conn);
    }

    @When("I add a new Kitchen Manager with name {string} and contact {string}")
    public void i_add_a_new_Kitchen_Manager_with_name_and_contact(String name, String contactInfo) {
        response = kitchenManagers.addManager(name, contactInfo);
    }

    

    @When("I try to add another Kitchen Manager with name {string} and contact {string}")
    public void i_try_to_add_another_Kitchen_Manager_with_name_and_contact(String name, String contactInfo) {
        response = kitchenManagers.addManager(name, contactInfo);
    }

    @Given("there is an existing Kitchen Manager with ID {int}")
    public void there_is_an_existing_Kitchen_Manager_with_ID(int id) {
        managerId = id;
    }

    @When("I update the manager with ID {int} to name {string} and contact {string}")
    public void i_update_the_manager_with_ID_to_name_and_contact(int id, String newName, String newContactInfo) {
        response = kitchenManagers.updateManager(id, newName, newContactInfo);
    }

    @When("I update a non-existing manager with ID {int} to name {string} and contact {string}")
    public void i_update_a_non_existing_manager_with_ID_to_name_and_contact(int id, String newName, String newContactInfo) {
        response = kitchenManagers.updateManager(id, newName, newContactInfo);
    }

    @When("I delete the manager with ID {int}")
    public void i_delete_the_manager_with_ID(int id) {
        response = kitchenManagers.deleteManager(id);
    }

    @When("I delete a non-existing manager with ID {int}")
    public void i_delete_a_non_existing_manager_with_ID(int id) {
        response = kitchenManagers.deleteManager(id);
    }

    @When("I retrieve the manager with ID {int}")
    public void i_retrieve_the_manager_with_ID(int id) {
        response = kitchenManagers.getManager(id);
    }

    @When("I retrieve a non-existing manager with ID {int}")
    public void i_retrieve_a_non_existing_manager_with_ID(int id) {
        response = kitchenManagers.getManager(id);
    }
}
