Feature: CRUD operations for Order

  Background: 
    Given the database is set up correctly with the necessary tables

  Scenario: Create an order
    Given I have a new order with customer ID 1, order date as current date, and total price 100.00
    When I create the order
    Then the order should be created with a valid order ID

  Scenario: Read an order by ID
    Given an order with ID 1 exists in the database
    When I retrieve the order with ID 1
    Then I should see the order details with ID 1

  Scenario: Read all orders
    Given multiple orders exist in the database
    When I retrieve all orders
    Then I should see a list of orders

  
  Scenario: Delete an order
    Given an order with ID 1 exists in the database
    When I delete the order with ID 1
    Then the order with ID 1 should be removed from the database
