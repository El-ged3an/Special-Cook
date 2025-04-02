Feature: Kitchen Manager CRUD Operations

  Scenario: Add a new Kitchen Manager
    Given a connection to the database is established
    When I add a new Kitchen Manager with name "John Doe" and contact "john.doe@example.com"
    Then the response should be "Manager added successfully."

  Scenario: Add a Kitchen Manager with an existing name
    Given a connection to the database is established
    When I add a new Kitchen Manager with name "John Doe" and contact "john.doe@example.com"
    Then the response should be "Manager added successfully."
    
    When I try to add another Kitchen Manager with name "John Doe" and contact "another.john@example.com"
    Then the response should be "Manager with this name already exists."

  Scenario: Update an existing Kitchen Manager
    Given a connection to the database is established
    And there is an existing Kitchen Manager with ID 1
    When I update the manager with ID 1 to name "Jane Doe" and contact "jane.doe@example.com"
    Then the response should be "Manager updated successfully."

  Scenario: Update a non-existing Kitchen Manager
    Given a connection to the database is established
    When I update a non-existing manager with ID 999 to name "New Manager" and contact "new.manager@example.com"
    Then the response should be "Manager not found."

  Scenario: Delete an existing Kitchen Manager
    Given a connection to the database is established
    And there is an existing Kitchen Manager with ID 1
    When I delete the manager with ID 1
    Then the response should be "Manager deleted successfully."

  Scenario: Delete a non-existing Kitchen Manager
    Given a connection to the database is established
    When I delete a non-existing manager with ID 999
    Then the response should be "Manager not found."

  Scenario: Retrieve an existing Kitchen Manager
    Given a connection to the database is established
    And there is an existing Kitchen Manager with ID 1
    When I retrieve the manager with ID 1
    Then the response should be "Manager: John Doe, Contact Info: john.doe@example.com"

  Scenario: Retrieve a non-existing Kitchen Manager
    Given a connection to the database is established
    When I retrieve a non-existing manager with ID 999
    Then the response should be "Manager not found."
