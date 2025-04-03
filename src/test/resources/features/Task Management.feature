Feature: Task Management in SpecialCookDB

  Scenario: Add a new task
    Given I have a chef with ID 1 and an order with ID 101
    When I add a task with description "Prepare pasta" and due time "2025-02-20 12:00:00"
    Then The task should be added successfully

  Scenario: Try to add a task for an existing order
    Given I have a chef with ID 1 and an order with ID 101
    And a task already exists for this order
    When I try to add a task for the same order
    Then I should see a message "Task for the order already exists"

  Scenario: Update an existing task
    Given I have an existing task with ID 1
    When I update the task description to "Prepare pizza" and change the due time to "2025-02-21 14:00:00"
    Then The task should be updated successfully

  Scenario: Try to update a non-existing task
    Given I have no task with ID 999
    When I try to update the task with ID 999
    Then I should see a message "Task not found for update"

  Scenario: Delete an existing task
    Given I have an existing task with ID 1
    When I delete the task with ID 1
    Then The task should be deleted successfully

  Scenario: Try to delete a non-existing task
    Given I have no task with ID 999
    When I try to delete the task with ID 999
    Then I should see a message "Task not found for deletion"

  Scenario: List all tasks
    Given there are multiple tasks in the database
    When I retrieve the list of all tasks
    Then I should see a list of tasks
