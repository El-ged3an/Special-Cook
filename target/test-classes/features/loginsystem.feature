Feature: Login System Functional Tests

  #Scenario: Add a new user with unique username for the login test
    #Given a connection to the login system
    #When I add a user with username "unique_test_01", password "alpha123", and role "chef"
    #Then the login result should be "User added successfully!"

  Scenario: Add duplicate user for the login test
    Given a connection to the login system
    And I add a user with username "duplicate_user", password "beta456", and role "customer"
    When I add a user with username "duplicate_user", password "gamma789", and role "admin"
    Then the result should be "User already exists!"

  Scenario: Update an existing user for the login test
    Given a connection to the login system
    And I add a user with username "mod_user", password "modpass", and role "admin"
    When I update user with ID of "mod_user" to username "mod_user_updated", password "updated_pass", and role "kitchen manager"
    Then the result should be "User updated successfully!"

  Scenario: Update a non-existing user for the login test
    Given a connection to the login system
    When I update user with ID 88888 to username "ghost_user", password "ghostpass", and role "chef"
    Then the result should be "User does not exist!"

  Scenario: Get user details by ID for the login test
    Given a connection to the login system
    And I add a user with username "lookup_user", password "lookup123", and role "customer"
    When I get user by username "lookup_user"
    Then the result should contain "Username: lookup_user"

  Scenario: Delete an existing user for the login test
    Given a connection to the login system
    And I add a user with username "to_delete", password "delete_me", and role "chef"
    When I delete user by username "to_delete"
    Then the result should be "User deleted successfully!"

  Scenario: Delete a non-existing user for the login test
    Given a connection to the login system
    When I delete user with ID 77777
    Then the result should be "User does not exist!"

  Scenario: Login with valid credentials for the login test
    Given a connection to the login system
    And I add a user with username "login_test_user", password "logmein123", and role "admin"
    When I login with username "login_test_user" and password "logmein123"
    Then the result should contain "Login successful"

  Scenario: Login with invalid credentials for the login test
    Given a connection to the login system
    When I login with username "non_existent_user" and password "wrongpass"
    Then the result should be "Invalid credentials."

  #Scenario: Check role of existing user for the login test
    #Given a connection to the login system
    #And I add a user with username "role_check", password "check123", and role "kitchen manager"
    #When I get role by username "role_check"
    #Then the result should contain "Role: kitchen manager"

  Scenario: Toggle foreign key checks for the login test
    Given a connection to the login system
    When I disable foreign key checks
    Then the result should be "Foreign key checks disabled."

    When I enable foreign key checks
    Then the result for this test should be "Foreign key checks enabled."
