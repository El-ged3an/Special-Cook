Feature: User Management in SpecialCookDB

  Scenario: Add a new user
    Given I am connected to the SpecialCookDB database
    When I add a user with username "john_doe", password "password123", and role "Customer"
    Then the user should be added successfully
    And the username "john_doe" should not already exist in the Users table

  Scenario: Add a user that already exists
    Given I am connected to the SpecialCookDB database
    When I try to add a user with username "john_doe", password "password123", and role "Customer"
    Then I should receive a message "User already exists!"

  Scenario: Update an existing user
    Given I am connected to the SpecialCookDB database
    And a user with user_id 1 exists
    When I update the user with user_id 1 to have username "john_doe_updated", password "newpassword123", and role "Chef"
    Then the user with user_id 1 should have the updated information

  Scenario: Update a non-existent user
    Given I am connected to the SpecialCookDB database
    When I try to update a user with user_id 999
    Then I should receive a message "User does not exist!"

  Scenario: Delete an existing user
    Given I am connected to the SpecialCookDB database
    And a user with user_id 1 exists
    When I delete the user with user_id 1
    Then the user with user_id 1 should no longer exist in the Users table

  Scenario: Delete a non-existent user
    Given I am connected to the SpecialCookDB database
    When I try to delete a user with user_id 999
    Then I should receive a message "User does not exist!"

  Scenario: Retrieve an existing user by ID
    Given I am connected to the SpecialCookDB database
    And a user with user_id 1 exists
    When I retrieve the user with user_id 1
    Then I should get the user's details with username "john_doe", password "password123", and role "Customer"

  Scenario: Retrieve a non-existent user by ID
    Given I am connected to the SpecialCookDB database
    When I try to retrieve a user with user_id 999
    Then I should receive "null" or no user data
