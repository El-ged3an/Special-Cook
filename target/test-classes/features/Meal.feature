Feature: Meal Object Functionality

  # Scenario 1: Create a Meal using the parameterized constructor
  Scenario: Create a Meal using parameterized constructor and validate properties
    Given I create a Meal using parameterized constructor with id "101", name "Pasta", description "Delicious Italian pasta", and price "15.0"
    When I retrieve its properties
    Then the id should be "101"
    And the name should be "Pasta"
    And the description should be "Delicious Italian pasta"
    And the price should be "15.0"

  # Scenario 2: Create a Meal using the no-argument constructor and set properties via setters
  Scenario: Create a Meal using no-argument constructor and update properties via setters
    Given I create a Meal using no-argument constructor
    When I set the id to "102", name to "Pizza", description to "Cheesy pizza", and price to "12.5"
    Then the id should be "102"
    And the name should be "Pizza"
    And the description should be "Cheesy pizza"
    And the price should be "12.5"

  # Scenario 3: Test Meal with empty name and description and zero price
  Scenario: Test Meal with empty name and description, and zero price
    Given I create a Meal using parameterized constructor with id "104", name "", description "", and price "0.0"
    When I retrieve its properties
    Then the id should be "104"
    And the name should be ""
    And the description should be ""
    And the price should be "0.0"

  # Scenario 4: Test Meal with a negative price value
  Scenario: Test Meal with negative price value
    Given I create a Meal using parameterized constructor with id "105", name "Negative Meal", description "Should be invalid", and price "-5.0"
    When I retrieve its properties
    Then the id should be "105"
    And the name should be "Negative Meal"
    And the description should be "Should be invalid"
    And the price should be "-5.0"
