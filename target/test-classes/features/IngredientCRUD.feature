Feature: Ingredient CRUD operations
 

  Scenario: Add a new ingredient
    Given there is no ingredient with the name "Tomato"
    When I add an ingredient with name "Tomato", stock quantity 50, unit "kg", and price 5.00
    Then the ingredient should be added successfully
    And the ingredient with name "Tomato" should exist in the database
 

  Scenario: Update an existing ingredient
    Given there is an ingredient with ID 1, name "Tomato", stock quantity 50, unit "kg", and price 5.00
    When I update the ingredient with ID 1 to name "Tomato", stock quantity 100, unit "kg", and price 6.00
    Then the ingredient should be updated successfully
    And the ingredient with ID 1 should have name "Tomato", stock quantity 100, and price 6.00

  Scenario: Attempt to update a non-existing ingredient
    Given there is no ingredient with ID 999
    When I update the ingredient with ID 999 to name "Cucumber", stock quantity 20, unit "kg", and price 3.00
    Then the ingredient should not be updated
    And the response should be "Ingredient not found!"

  Scenario: Delete an existing ingredient
    Given there is an ingredient with ID 1, name "Tomato", stock quantity 50, unit "kg", and price 5.00
    When I delete the ingredient with ID 1
    Then the ingredient should be deleted successfully
    And the ingredient with ID 1 should not exist in the database

  Scenario: Attempt to delete a non-existing ingredient
    Given there is no ingredient with ID 999
    When I delete the ingredient with ID 999
    Then the ingredient should not be deleted
    And the response should be "Ingredient not found!"

  Scenario: Get details of an existing ingredient
    Given there is an ingredient with ID 1, name "Tomato", stock quantity 50, unit "kg", and price 5.00
    When I get the ingredient with ID 1
    Then the response should be "Ingredient found: Tomato, 50 kg"

  Scenario: Get details of a non-existing ingredient
    Given there is no ingredient with ID 999
    When I get the ingredient with ID 999
    Then the response should be "Ingredient not found!"
