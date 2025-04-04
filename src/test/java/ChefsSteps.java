import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class ChefsSteps {
    private ChefsDAO chefsDAO = new ChefsDAO();
    private String chefName;
    private String chefSpecialization;
    private int chefId;

    @Given("I have a chef name {string} and specialization {string}")
    public void i_have_a_chef_name_and_specialization(String name, String specialization) {
        this.chefName = name;
        this.chefSpecialization = specialization;
    }

    @When("I add the chef")
    public void i_add_the_chef() {
        chefsDAO.addChef(chefName, chefSpecialization);
    }

    @Then("the chef should be added to the system")
    public void the_chef_should_be_added_to_the_system() {
        // Verify that the chef is added by checking if the name exists
        chefsDAO.viewChefs();  // You might want to enhance this step by checking the database for the chef
    }

    @Then("I should see the chef with name {string} and specialization {string}")
    public void i_should_see_the_chef_with_name_and_specialization(String name, String specialization) {
        // Verify that the chef appears with the correct name and specialization
        chefsDAO.viewChefs();
        // You can implement the validation more thoroughly
    }

    @Given("there are chefs in the system")
    public void there_are_chefs_in_the_system() {
        // This step assumes chefs exist, as they are already inserted into the system
    }

    @When("I view the chefs")
    public void i_view_the_chefs() {
        chefsDAO.viewChefs();
    }

    @Then("I should see a list of all chefs with their names and specializations")
    public void i_should_see_a_list_of_all_chefs_with_their_names_and_specializations() {
        chefsDAO.viewChefs();
    }

    @Given("I have a chef with ID {int} and the chef name {string} and specialization {string}")
    public void i_have_a_chef_with_id_and_the_chef_name_and_specialization(int id, String name, String specialization) {
        this.chefId = id;
        this.chefName = name;
        this.chefSpecialization = specialization;
    }

    @When("I update the chef's name to {string} and specialization to {string}")
    public void i_update_the_chef_s_name_to_and_specialization_to(String newName, String newSpecialization) {
        chefsDAO.updateChef(chefId, newName, newSpecialization);
    }

    @Then("the chef with ID {int} should be updated to have name {string} and specialization {string}")
    public void the_chef_with_id_should_be_updated_to_have_name_and_specialization(int id, String name, String specialization) {
        chefsDAO.viewChefs();  // This should check the updated chef
    }

    @Given("I have a chef with ID {int} and name {string}")
    public void i_have_a_chef_with_id_and_name(int id, String name) {
        this.chefId = id;
        this.chefName = name;
    }

    @When("I delete the chef with ID {int}")
    public void i_delete_the_chef_with_id(int id) {
        chefsDAO.deleteChef(id);
    }

    @Then("the chef with ID {int} should be removed from the system")
    public void the_chef_with_id_should_be_removed_from_the_system(int id) {
        chefsDAO.viewChefs();  // Ensure the chef is deleted
    }

    @Given("I have a non-existing chef with ID {int}")
    public void i_have_a_non_existing_chef_with_id(int id) {
        this.chefId = id;
    }

    @When("I try to update the chef with ID {int}")
    public void i_try_to_update_the_chef_with_id(int id) {
        chefsDAO.updateChef(id, "New Name", "New Specialization");
    }

    @Then("I should receive a message that the chef does not exist")
    public void i_should_receive_a_message_that_the_chef_does_not_exist() {
        // Here you should check for the appropriate error message or response
    }

    @When("I try to delete the chef with ID {int}")
    public void i_try_to_delete_the_chef_with_id(int id) {
        chefsDAO.deleteChef(id);
    }
}
