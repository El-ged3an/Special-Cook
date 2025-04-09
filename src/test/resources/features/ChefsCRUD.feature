Feature: Chef Management

  Scenario: Add a new Chef
    Given I have a chef name "Chef Ayman" and specialization "Italian Cuisine"
    When I add the chef
    Then the chef should be added to the system
    And I should see the chef with name "Chef Ayman" and specialization "Italian Cuisine"

  Scenario: View all chefs
    Given there are chefs in the system
    When I view the chefs
    Then I should see a list of all chefs with their names and specializations

  Scenario: Update an existing chef
    Given I have a chef with ID 1 and the chef name "Chef Ayman" and specialization "Italian Cuisine"
    When I update the chef's name to "Chef Laila" and specialization to "Pastries and Baking"
    Then the chef with ID 1 should be updated to have name "Chef Laila" and specialization "Pastries and Baking"

  Scenario: Delete a chef
    Given I have a chef with ID 2 and name "Chef Tom"
    When I delete the chef with ID 2
    Then the chef with ID 2 should be removed from the system

  Scenario: Trying to update a non-existing chef
    Given I have a non-existing chef with ID 999
    When I try to update the chef with ID 999
    Then I should receive a message that the chef does not exist

  Scenario: Trying to delete a non-existing chef
    Given I have a non-existing chef with ID 999
    When I try to delete the chef with ID 999
    Then I should receive a message that the chef does not exist
