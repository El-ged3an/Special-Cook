Feature: MealDAO Operations

  # Scenario 1: Add and retrieve a meal
  Scenario: Add a meal and then retrieve it by id
    Given I prepare a meal with id "101", name "Spaghetti", description "Delicious pasta", and price "12.5"
    When I add the meal to the database
    And I retrieve the meal by id "101"
    Then the meal's name should be "Spaghetti"
    And the meal's description should be "Delicious pasta"
    And the meal's price should be "12.5"

  # Scenario 2: Update an existing meal
  Scenario: Update an existing meal record
    Given I prepare a meal with id "102", name "Pizza", description "Cheesy pizza", and price "10.0" and add it to the database
    When I update the meal with id "102" to name "Veggie Pizza", description "Cheesy veggie pizza", and price "11.0"
    And I retrieve the meal by id "102"
    Then the meal's name should be "Veggie Pizza"
    And the meal's description should be "Cheesy veggie pizza"
    And the meal's price should be "11.0"

  # Scenario 3: Delete a meal
  Scenario: Delete a meal record
    Given I prepare a meal with id "103", name "Salad", description "Fresh salad", and price "8.0" and add it to the database
    When I delete the meal with id "103"
    And I retrieve the meal by id "103"
    Then the result should be null

  # Scenario 4: Retrieve available meals
  Scenario: Retrieve all available meals
    Given I prepare a meal with id "104", name "Burger", description "Beef burger", and price "9.5" and add it to the database
    And I prepare a meal with id "105", name "Fries", description "Crispy fries", and price "4.5" and add it to the database
    When I retrieve available meals
    Then the number of available meals should be at least "2"

  # Scenario 5: Retrieve meal by non-existing id
  Scenario: Retrieve a meal using a non-existing id
    When I retrieve the meal by id "99999"
    Then the result should be null
