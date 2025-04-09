Feature: Supplier CRUD Operations

  Scenario: Create a new supplier
    Given I have a connection to the database
    When I create a supplier with name "Poultry Direct" and contact "sales@poultrydirect.com"
    Then the supplier "Poultry Direct" should be added to the Suppliers table

  Scenario: Get an existing supplier by ID
    Given I have a connection to the database
    When I get the supplier with ID 6
    Then I should get the supplier details with ID 6

  Scenario: Attempt to get a non-existing supplier
    Given I have a connection to the database
    When I get the supplier with ID 999
    Then I should receive "Supplier not found."

  Scenario: Update an existing supplier
    Given I have a connection to the database
    When I update the supplier with ID 6 to name "Updated Supplier" and contact "111-222-3333"
    Then the supplier with ID 6 should have name "Updated Supplier" and contact "111-222-3333"

  Scenario: Attempt to update a non-existing supplier
    Given I have a connection to the database
    When I update the supplier with ID 999 to name "Non-existing Supplier" and contact "000-000-0000"
    Then I should receive "Supplier not found."

  Scenario: Delete an existing supplier
    Given I have a connection to the database
    When I delete the supplier with ID 1
    Then the supplier with ID 1 should be removed from the Suppliers table

  Scenario: Delete a supplier with related data
    Given I have a connection to the database
    When I delete the supplier with ID 2
    Then the supplier with ID 2 and all related inventory and supplier payments should be deleted from the database
