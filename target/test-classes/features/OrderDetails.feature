Feature: OrderDetails CRUD operations

  Scenario: Add a new order detail
    Given a connection to the database
    When I add an order detail with order_id 1, ingredient_id 2, and quantity 5
    Then the order detail should be added successfully

  Scenario: Try to add an existing order detail
    Given a connection to the database
    When I try to add an order detail with order_id 1, ingredient_id 2, and quantity 5
    Then the order detail should not be added because it already exists

  Scenario: Update an existing order detail
    Given a connection to the database
    And an order detail with detail_id 1 exists
    When I update the order detail with detail_id 1 to order_id 1, ingredient_id 3, and quantity 10
    Then the order detail should be updated successfully

  Scenario: Try to update a non-existing order detail
    Given a connection to the database
    When I try to update an order detail with detail_id 999 to order_id 1, ingredient_id 4, and quantity 8
    Then the order detail should not be updated because it does not exist

  Scenario: Delete an existing order detail
    Given a connection to the database
    And an order detail with detail_id 1 exists
    When I delete the order detail with detail_id 1
    Then the order detail should be deleted successfully

  Scenario: Try to delete a non-existing order detail
    Given a connection to the database
    When I try to delete an order detail with detail_id 999
    Then the order detail should not be deleted because it does not exist

  Scenario: Retrieve order details by order_id
    Given a connection to the database
    When I retrieve order details for order_id 1
    Then I should get a list of order details for that order_id
