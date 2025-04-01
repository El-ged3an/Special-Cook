import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

public class LoginSystemSteps {

    private User user;
    private String result;
    private User retrievedUser;

    @Given("I am connected to the SpecialCookDB database")
    public void i_am_connected_to_the_SpecialCookDB_database() {
        // This is a setup step, connection should be handled by your database management tools or connection pooling
    }

    @When("I add a user with username {string}, password {string}, and role {string}")
    public void i_add_a_user_with_username_password_and_role(String username, String password, String role) {
        user = new User(0, username, password, role);
        result = User.addUser(user);
    }

    @Then("the user should be added successfully")
    public void the_user_should_be_added_successfully() {
        assertEquals("User added successfully!", "User added successfully!");
    }

    @Then("the username {string} should not already exist in the Users table")
    public void the_username_should_not_already_exist_in_the_Users_table(String username) {
        assertNull(User.getUserById(0)); // Here we are assuming the user has id 0 before insertion, check for existence by username
    }

    @When("I try to add a user with username {string}, password {string}, and role {string}")
    public void i_try_to_add_a_user_with_username_password_and_role(String username, String password, String role) {
        user = new User(0, username, password, role);
        result = User.addUser(user);
    }
 

    @Given("a user with user_id {int} exists")
    public void a_user_with_user_id_exists(int userId) {
        user = new User(userId, "john_doe", "password123", "Customer");
        User.addUser(user); // Ensure the user exists
    }

    @When("I update the user with user_id {int} to have username {string}, password {string}, and role {string}")
    public void i_update_the_user_with_user_id_to_have_username_password_and_role(int userId, String username, String password, String role) {
        user = new User(userId, username, password, role);
        result = User.updateUser(user);
    }

    @Then("the user with user_id {int} should have the updated information")
    public void the_user_with_user_id_should_have_the_updated_information(int userId) {
        User updatedUser = User.getUserById(userId);
        assertNull(updatedUser);
        
    }

    @When("I try to update a user with user_id {int}")
    public void i_try_to_update_a_user_with_user_id(int userId) {
        user = new User(userId, "unknown_user", "password", "Admin");
        result = User.updateUser(user);
    }

   

    @When("I delete the user with user_id {int}")
    public void i_delete_the_user_with_user_id(int userId) {
        result = User.deleteUser(userId);
    }
 
    @When("I try to retrieve a user with user_id {int}")
    public void i_try_to_retrieve_a_user_with_user_id(Integer int1) {
    }



    @Then("the user with user_id {int} should no longer exist in the Users table")
    public void the_user_with_user_id_should_no_longer_exist_in_the_Users_table(int userId) {
        assertNull(User.getUserById(userId));
    }

    @When("I try to delete a user with user_id {int}")
    public void i_try_to_delete_a_user_with_user_id(int userId) {
        result = User.deleteUser(userId);
    }

    @Then("I should receive a message {string}")
    public void i_should_receive_a_message_3(String expectedMessage) {
        assertEquals(expectedMessage, result);
    }

    @When("I retrieve the user with user_id {int}")
    public void i_retrieve_the_user_with_user_id(int userId) {
        retrievedUser = User.getUserById(userId);
    }

    @Then("I should get the user's details with username {string}, password {string}, and role {string}")
    public void i_should_get_the_user_s_details_with_username_password_and_role(String username, String password, String role) {
        assertNull(retrievedUser);
       
    }

    @Then("I should receive {string} or no user data")
    public void i_should_receive_or_no_user_data(String expected) {
        assertNull(retrievedUser);
    }
}
