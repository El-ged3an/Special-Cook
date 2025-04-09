Feature: Inventory Management

  Scenario: Add inventory item that does not exist
    Given an inventory database connection is established
    When I add an inventory item with ingredient_id 1, supplier_id 1, stock_level 100, and last_restocked "2025-02-15 12:00:00"
    Then the result should be "Inventory added successfully."

  Scenario: Add inventory item that already exists
    Given an inventory database connection is established
    And an inventory item with ingredient_id 1 and supplier_id 1 already exists
    When I add an inventory item with ingredient_id 1, supplier_id 1, stock_level 100, and last_restocked "2025-02-15 12:00:00"
    Then the result should be "Inventory entry already exists for the given ingredient and supplier."

  Scenario: Update existing inventory item
    Given an inventory database connection is established
    And an inventory item with inventory_id 1 exists
    When I update the inventory item with inventory_id 1, ingredient_id 2, supplier_id 2, stock_level 200, and last_restocked "2025-02-15 14:00:00"
    Then the result should be "Inventory updated successfully."

  Scenario: Update non-existing inventory item
  Given an inventory database connection is established
  When I update the inventory item with non-existing inventory_id 999, ingredient_id 2, supplier_id 2, stock_level 200, and last_restocked "2025-02-15 14:00:00"
  Then the result should be "Failed to update inventory."

Scenario: Delete non-existing inventory item
  Given an inventory database connection is established
  When I delete the inventory item with non-existing inventory_id 999
  Then the result should be "Failed to delete inventory."

  Scenario: Delete existing inventory item
    Given an inventory database connection is established
    And an inventory item with inventory_id 1 exists
    When I delete the inventory item with inventory_id 1
    Then the result should be "Inventory deleted successfully."

  #Scenario: Get all inventory items
    #Given an inventory database connection is established
    #When I retrieve the list of inventory items
    #Then the result should contain "Inventory ID", "Ingredient ID", "Supplier ID", "Stock Level", and "Last Restocked" for each item.
