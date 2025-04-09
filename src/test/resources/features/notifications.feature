Feature: Notifications Management
Background:
the database is connected

  Scenario: Add a new notification
    Given the database is connectedd
    When I add a notification with user_id "1" and message "Order confirmed"
    Then the result should be "Notification added successfully."

  Scenario: Try to add an existing notification
    Given the database is connected
    And a notification with user_id "1" and message "Order confirmed" already exists
    When I add a notification with user_id "1" and message "Order confirmed"
    Then the result should be "Notification already exists."

  Scenario: Update an existing notification
    Given the database is connected
    And a notification with notification_id "1" exists
    When I update the notification with notification_id "1" to user_id "2" and message "Order shipped"
    Then the result should be "Notification updated successfully."

  Scenario: Try to update a non-existing notification
    Given the database is connected
    When I update a notification with notification_id "999" to user_id "2" and message "Order shipped"
    Then the result should be "Notification not found."

  Scenario: Delete an existing notification
    Given the database is connected
    And a notification with notification_id "1" exists
    When I delete the notification with notification_id "1"
    Then the result should be "Notification deleted successfully."

  Scenario: Try to delete a non-existing notification
    Given the database is connected
    When I delete a notification with notification_id "999"
    Then the result should be "Notification not found."

  Scenario: Retrieve all notifications for a user
    Given the database is connected
    And there are notifications for user_id "1"
    When I retrieve all notifications for user_id "1"
    Then the result should include "Order confirmed" and "Order shipped"
