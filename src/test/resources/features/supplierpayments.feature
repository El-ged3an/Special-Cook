Feature: Manage Supplier Payments
 
  Scenario: Add a new payment successfully
    Given a payment with supplier ID 1, amount 100.00, and status "Pending" does not exist
    When I add the payment
    Then the payment should be successfully added

   
  Scenario: Update an existing payment successfully
    Given a payment with ID 1 exists and has status "Pending"
    When I update the payment with amount 150.00 and status "Paid"
    Then the payment should be updated successfully

  Scenario: Prevent updating a non-existing payment
    Given a payment with ID 100 does not exist
    When I attempt to update the payment with ID 100
    Then the payment should not be updated

  Scenario: Delete an existing payment successfully
    Given a payment with ID 1 exists
    When I delete the payment
    Then the payment should be deleted successfully

  Scenario: Prevent deleting a non-existing payment
    Given a payment with ID 100 does not exist
    When I attempt to delete the payment with ID 100
    Then the payment should not be deleted

 
