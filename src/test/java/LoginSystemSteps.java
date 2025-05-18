import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class LoginSystemSteps {

    private LoginSystem loginSystem;
    private String result;
    private Connection conn;

    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private int getUserIdByUsername(String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM Users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("user_id") : -1;
    }

    @Given("a connection to the login system")
    public void connectToLoginSystem() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        loginSystem = new LoginSystem(conn);
    }

    @When("I add a user with username {string}, password {string}, and role {string}")
    public void addUser(String username, String password, String role) {
        result = loginSystem.addUser(username, password, role);
    }

    @When("I update user with ID of {string} to username {string}, password {string}, and role {string}")
    public void updateExistingUser(String oldUsername, String newUsername, String newPassword, String newRole) throws SQLException {
        int id = getUserIdByUsername(oldUsername);
        result = loginSystem.updateUser(id, newUsername, newPassword, newRole);
    }

    @When("I update user with ID {int} to username {string}, password {string}, and role {string}")
    public void updateNonExistentUser(int id, String username, String password, String role) {
        result = loginSystem.updateUser(id, username, password, role);
    }

    @When("I get user by username {string}")
    public void getUserByUsername(String username) throws SQLException {
        int id = getUserIdByUsername(username);
        result = loginSystem.getUser(id);
    }

    @When("I delete user by username {string}")
    public void deleteUserByUsername(String username) throws SQLException {
        int id = getUserIdByUsername(username);
        result = loginSystem.deleteUser(id);
    }

    @When("I delete user with ID {int}")
    public void deleteUserById(int id) {
        result = loginSystem.deleteUser(id);
    }

    @When("I login with username {string} and password {string}")
    public void loginUser(String username, String password) {
        result = loginSystem.login(username, password);
    }

    @When("I get role by username {string}")
    public void getRoleByUsername(String username) throws SQLException {
        int id = getUserIdByUsername(username);
        result = loginSystem.checkRole(id);
    }

    @When("I enable foreign key checks")
    public void enableFKChecks() {
        result = loginSystem.setForeignKeyChecks(true);
    }

    @When("I disable foreign key checks")
    public void disableFKChecks() {
        result = loginSystem.setForeignKeyChecks(false);
    }

    @Then("the login result should be {string}") // MISSING
    public void theLoginResultShouldBe(String expected) {
        assertEquals(expected, result);
    }

    @Then("the result should contain {string}")
    public void theResultShouldContain(String expected) {
        assertTrue(result.contains(expected));
    }
    
    @Then("the result for this test should be {string}")
    public void theResultForThisTestShouldBe(String expected) {
        assertEquals(expected, result);
    }

}
