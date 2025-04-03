Feature: MealIngredientDAO Operations

  # Scenario 1: Add a MealIngredient and retrieve it by meal id
  Scenario: Add a MealIngredient and retrieve it by meal id
    Given a database connection is established
    And a MealIngredient with mealId "1", ingredientId "2" and quantity "3.5" is prepared
    When I add the MealIngredient to the database
    And I retrieve MealIngredients for mealId "1"
    Then one of the retrieved records should have ingredientId "2" and quantity "3.5"

  # Scenario 2: Update an existing MealIngredient record
  Scenario: Update an existing MealIngredient record
    Given a database connection is established
    And a MealIngredient with mealId "1", ingredientId "2" and quantity "2.0" is prepared and added to the database
    When I update the MealIngredient's quantity to "4.5"
    And I retrieve MealIngredients for mealId "1"
    Then the retrieved record with ingredientId "2" should have quantity "4.5"

  # Scenario 3: Delete an existing MealIngredient record
  Scenario: Delete an existing MealIngredient record
    Given a database connection is established
    And a MealIngredient with mealId "1", ingredientId "2" and quantity "5.0" is prepared and added to the database
    When I delete the MealIngredient with mealId "1" and ingredientId "2"
    And I retrieve MealIngredients for mealId "1"
    Then none of the retrieved records should have ingredientId "2"

  # Scenario 4: Add multiple MealIngredient records and verify count
  Scenario: Add multiple MealIngredient records and retrieve them by meal id
    Given a database connection is established
    And a MealIngredient with mealId "1", ingredientId "2" and quantity "1.5" is prepared and added to the database
    And a MealIngredient with mealId "1", ingredientId "2" and quantity "2.5" is prepared and added to the database
    When I retrieve MealIngredients for mealId "1"
    Then the number of retrieved records should be at least "2"
