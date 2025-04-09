Feature: Billing CRUD operations

  Scenario: Add a new billing record
    Given a valid order_id 1, customer_id 1, amount 100.50, and payment_status 'Pending'
    When I add the billing record
    Then the system should return "Billing record added successfully"

  Scenario: Attempt to add a duplicate billing record
    Given a billing record already exists for order_id 1
    When I add the billing record with the same order_id 1
    Then the system should return "Billing record already exists for this order"

  Scenario: Update an existing billing record
    Given a billing record with billing_id 1 exists
    And the updated values are order_id 2, customer_id 2, amount 200.75, payment_status 'Paid'
    When I update the billing record with billing_id 1
    Then the system should return "Billing record updated successfully"

  Scenario: Attempt to update a non-existent billing record
    Given no billing record exists with billing_id 999
    When I update the billing record with billing_id 999
    Then the system should return "Billing record not found"

  Scenario: Delete an existing billing record
    Given a billing record with billing_id 1 exists
    When I delete the billing record with billing_id 1
    Then the system should return "Billing record deleted successfully"

  Scenario: Attempt to delete a non-existent billing record
    Given no billing record exists with billing_id 999
    When I delete the billing record with billing_id 999
    Then the system should return "Billing record not found"

  Scenario: Retrieve an existing billing record
    Given a billing record with billing_id 1 exists
    When I retrieve the billing record with billing_id 1
    Then the system should return "Billing Record: Order ID = 1, Customer ID = 1, Amount = 100.50, Payment Status = Pending"

  Scenario: Attempt to retrieve a non-existent billing record
    Given no billing record exists with billing_id 999
    When I retrieve the billing record with billing_id 999
    Then the system should return "Billing record not found"
