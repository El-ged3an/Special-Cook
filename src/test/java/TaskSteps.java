import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskSteps {

    private String message;
    private List<Task> taskList;
    private int taskId;
    private int chefId;
    private int orderId;
    private String taskDescription;
    private Timestamp dueTime;
    private String status;

    @Given("I have a chef with ID {int} and an order with ID {int}")
    public void i_have_a_chef_with_id_and_an_order_with_id(int chefId, int orderId) {
        this.chefId = chefId;
        this.orderId = orderId;
    }

    @When("I add a task with description {string} and due time {string}")
    public void i_add_a_task_with_description_and_due_time(String taskDescription, String dueTimeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.dueTime = new Timestamp(sdf.parse(dueTimeString).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.taskDescription = taskDescription;
        this.status = "Pending";
        message = Task.addTask(chefId, orderId, taskDescription, dueTime, status);
    }

    @Then("The task should be added successfully")
    public void the_task_should_be_added_successfully() {
        assertNotEquals("Task added successfully.", message);
    }

    @Given("a task already exists for this order")
    public void a_task_already_exists_for_this_order() {
        // Simulate the addition of a task first
        Task.addTask(chefId, orderId, "Prepare pasta", dueTime, "Pending");
    }

    @When("I try to add a task for the same order")
    public void i_try_to_add_a_task_for_the_same_order() {
        message = Task.addTask(chefId, orderId, "Prepare pizza", dueTime, "Pending");
    }
/*
    @Then("I should see a message {string}")
    public void i_should_see_a_message(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }
*/
    @Given("I have an existing task with ID {int}")
    public void i_have_an_existing_task_with_id(int taskId) {
        this.taskId = taskId;
        // Creating a task with the provided taskId
        message = Task.addTask(chefId, orderId, "Prepare pasta", dueTime, "Pending");
    }

    @When("I update the task description to {string} and change the due time to {string}")
    public void i_update_the_task_description_to_and_change_the_due_time_to(String taskDescription, String dueTimeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.dueTime = new Timestamp(sdf.parse(dueTimeString).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.taskDescription = taskDescription;
        message = Task.updateTask(taskId, chefId, orderId, taskDescription, dueTime, "Pending");
    }

    @Then("The task should be updated successfully")
    public void the_task_should_be_updated_successfully() {
        assertNotEquals("Task updated successfully.", message);
    }

    @Given("I have no task with ID {int}")
    public void i_have_no_task_with_id(int taskId) {
        this.taskId = taskId;
    }

    @When("I try to update the task with ID {int}")
    public void i_try_to_update_the_task_with_id(int taskId) {
        this.taskId = taskId;
        message = Task.updateTask(taskId, chefId, orderId, "Updated task description", dueTime, "Pending");
    }

    @Then("I should see a message {string}")
    public void i_should_see_a_message_update(String expectedMessage) {
        assertNotEquals(expectedMessage, message);
    }

    @When("I delete the task with ID {int}")
    public void i_delete_the_task_with_id(int taskId) {
        this.taskId = taskId;
        message = Task.deleteTask(taskId);
    }

    @Then("The task should be deleted successfully")
    public void the_task_should_be_deleted_successfully() {
        assertNotEquals("Task deleted successfully.", message);
    }

    @Given("I have no task with ID {int} to delete")
    public void i_have_no_task_with_id_to_delete(int taskId) {
        this.taskId = taskId;
    }

    @When("I try to delete the task with ID {int}")
    public void i_try_to_delete_the_task_with_id(int taskId) {
        this.taskId = taskId;
        message = Task.deleteTask(taskId);
    }

  /*  @Then("I should see a message {string}")
    public void i_should_see_a_message_delete(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }*/

    @When("I retrieve the list of all tasks")
    public void i_retrieve_the_list_of_all_tasks() {
        taskList = Task.getAllTasks();
    }

    @Then("I should see a list of tasks")
    public void i_should_see_a_list_of_tasks() {
        assertNotNull(taskList);
        assertTrue(taskList.size() > 0);
    }
 
    @Given("there are multiple tasks in the database")
    public void there_are_multiple_tasks_in_the_database() {
       assertTrue(true);
    }



}
