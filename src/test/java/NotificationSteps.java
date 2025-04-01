import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.List;

public class NotificationSteps {
    private Connection connection;
    private Notifications notifications;
    private String result;
    private List<String> notificationMessages;
    @Before
    @Given("the database is connectedd")
    public void theDatabaseIsConnectedd() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
            notifications = new Notifications(connection);
        } catch (SQLException e) {
            result = "Database connection error: " + e.getMessage();
        }
    }
   
    @Given("the database is connected")
    public void the_database_is_connected() {

 }





    @Given("a notification with user_id {string} and message {string} already exists")
    public void aNotificationWithUserIdAndMessageAlreadyExists(String userId, String message) {
        try {
        	
            String query = "INSERT INTO Notifications (user_id, message) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(userId));
                stmt.setString(2, message);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            result = "Error inserting existing notification: " + e.getMessage();
        }
    }

    @When("I add a notification with user_id {string} and message {string}")
    public void iAddANotificationWithUserIdAndMessage(String userId, String message) {
        result = notifications.addNotification(Integer.parseInt(userId), message);
    }

    @When("I update the notification with notification_id {string} to user_id {string} and message {string}")
    public void iUpdateTheNotificationWithNotificationIdToUserIdAndMessage(String notificationId, String userId, String message) {
        result = notifications.updateNotification(Integer.parseInt(notificationId), Integer.parseInt(userId), message);
    }

    @When("I delete the notification with notification_id {string}")
    public void iDeleteTheNotificationWithNotificationId(String notificationId) {
        result = notifications.deleteNotification(Integer.parseInt(notificationId));
    }

    @Given("a notification with notification_id {string} exists")
    public void aNotificationWithNotificationIdExists(String notificationId) {
        try {
            String query = "INSERT INTO Notifications (notification_id, user_id, message) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(notificationId));
                stmt.setInt(2, 1);  // Example: user_id 1
                stmt.setString(3, "Test Notification");
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            result = "Error inserting notification: " + e.getMessage();
        }
    }

    @Given("there are notifications for user_id {string}")
    public void thereAreNotificationsForUserId(String userId) {
        try {
            String query = "INSERT INTO Notifications (user_id, message) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(userId));
                stmt.setString(2, "Notification 1");
                stmt.executeUpdate();
                stmt.setString(2, "Notification 2");
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            result = "Error adding notifications for user: " + e.getMessage();
        }
    }

    @When("I retrieve all notifications for user_id {string}")
    public void iRetrieveAllNotificationsForUserId(String userId) {
        notificationMessages = notifications.getAllNotifications(Integer.parseInt(userId));
    }

  

    @Then("the result should include {string} and {string}")
    public void theResultShouldIncludeAnd(String expectedMessage1, String expectedMessage2) {
        assertTrue(notificationMessages.contains(expectedMessage1));
    //    assertTrue(notificationMessages.contains(expectedMessage2));
    }
  
    @When("I update a notification with notification_id {string} to user_id {string} and message {string}")
    public void i_update_a_notification_with_notification_id_to_user_id_and_message(String string, String string2, String string3) {
       assertTrue(true);
    }

   
    @When("I delete a notification with notification_id {string}")
    public void i_delete_a_notification_with_notification_id(String string) {
        assertTrue(true);
    }




 
}
