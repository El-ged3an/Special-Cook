Feature: Customer Management

  Scenario: Add a new customer
    Given I have the following customer details:
      | name            | phone        | dietary_preferences | allergies    |
      | John Doe        | 1234567890   | Vegetarian          | Peanuts      |
    When I add the customer
    Then the customer should be added successfully

  Scenario: View all customers
    Given there are existing customers
    When I view all customers
    Then I should see a list of customers

  Scenario: Update an existing customer
    Given I have an existing customer with the following details:
      | customer_id | name    | phone        | dietary_preferences | allergies    |
      | 1           | John Doe| 1234567890   | Vegetarian          | Peanuts      |
    When I update the customer with new details:
      | name          | phone      | dietary_preferences | allergies    |
      | John Smith    | 9876543210 | Vegan               | Dairy        |
    Then the customer's details should be updated successfully

  Scenario: Delete a customer
    Given I have an existing customer with the following details:
      | customer_id | name    | phone        | dietary_preferences | allergies    |
      | 2           | Jane Doe| 1122334455   | Gluten-Free         | None         |
    When I delete the customer with customer_id 2
    Then the customer should be deleted successfully

  Scenario: Check if a customer exists
    Given I have an existing customer with the following details:
      | customer_id | name    | phone        | dietary_preferences | allergies    |
      | 3           | Mike Lee| 5566778899   | Pescatarian         | Shellfish    |
    When I check if the customer with customer_id 3 exists
    Then the customer should exist
