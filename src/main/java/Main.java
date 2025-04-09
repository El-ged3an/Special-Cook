import java.sql.*;
import java.util.*;
import java.util.regex.*;

public class Main{
	
	private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static Connection conn;
    private static String username;
    private static String password;
    private static String api_key= "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";
    private static String ai_url="https://openrouter.ai/api/v1";
    private static String ai_model = "qwen/qwq-32b:free";
    private static int userId;
    private static List<String> dietaryPreferences = new ArrayList<>(Arrays.asList("Vegan","Keto","Gluten-Free","Mediterranean","Pescatarian"));
    private static List<String> allergyOptions = new ArrayList<>(Arrays.asList("Nuts","Dairy","Gluten","Seafood","Eggs","Soy","None"));
    private static AiAssistant Assistant = new AiAssistant(ai_url,api_key,ai_model);
    
	  public static void main(String args[]) {
		  login();
	}
	  
	  private static int extractUserId(String loginResult) {
	        String[] parts = loginResult.split("User ID: ");
	        if (parts.length > 1) {
	            return Integer.parseInt(parts[1].split(",")[0].trim());
	        }
	        return -1;
	    }
	    
	    private static void navigateToRolePage(String role) {
	        switch (role.toLowerCase()) {
	            case "chef":
	                chefPage();//done
	                break;
	            case "customer":
	                customerPage();//done
	                break;
	            case "admin":
	                sysAdminPage();//done
	                break;
	            case "kitchen manager":
	            	System.out.println("\nHeheh");
	                kitchenManagerPage();
	                break;
	            default:
	                System.out.println("\nUnknown role. Access denied");
	        }
	    }
	    
	    private static void chefPage() {
	        Scanner scanner = new Scanner(System.in);
	        int choice;
	        while (true) {
	            System.out.println("\n==== Welcome to the Chef Page ==== \n" +
	                               "1) View Customer Order History \n" +
	                               "2) View Customer Dietary Preferences and Allergies \n" +
	                               "3) Update Task Status \n"+
	                               "4) Approve Custom Meals \n" +
	                               "5) View Scheduled Cooking Tasks \n" +
	                               "6) Log Out \n" +
	                               "Enter your choice: ");
	            choice = scanner.nextInt();
	            scanner.nextLine(); // consume newline
	            switch (choice) {
	                case 1:
	                    // Sub-menu: choose between a specific customer or all customers
	                    System.out.println("\n--- Order History Options ---");
	                    System.out.println("1) View order history for a specific customer");
	                    System.out.println("2) View order history for all customers");
	                    System.out.print("Enter your choice: ");
	                    int subChoice = scanner.nextInt();
	                    scanner.nextLine(); // consume newline
	                    MealDAO mealDAO = new MealDAO();
	                    OrderDetails.setConnection(conn);
	                    if (subChoice == 1) {
	                        System.out.print("Enter customer ID to view order history: ");
	                        int custId = scanner.nextInt();
	                        scanner.nextLine();
	                        List<Order> orders = Order.getOrdersByCustomerId(custId, conn);
	                        if (orders.isEmpty()) {
	                            System.out.println("No orders found for customer " + custId);
	                        } else {
	                            System.out.println("Order History for Customer " + custId + ":");
	                            for (Order o : orders) {
	                                displayOrderWithDetails(o, mealDAO);
	                            }
	                        }
	                    } else if (subChoice == 2) {
	                        // View order history for all customers
	                        List<Order> orders = Order.readAllOrders(conn);
	                        if (orders.isEmpty()) {
	                            System.out.println("No orders found.");
	                        } else {
	                            System.out.println("Order History for All Customers:");
	                            for (Order o : orders) {
	                                displayOrderWithDetails(o, mealDAO);
	                            }
	                        }
	                    } else {
	                        System.out.println("Invalid choice, returning to main menu.");
	                    }
	                    break;
	                case 2:
	                    System.out.println("\n--- Dietary Preferences and Allergies Options ---");
	                    System.out.println("1) View for a specific customer");
	                    System.out.println("2) View for all customers");
	                    System.out.print("Enter your choice: ");
	                    int dpChoice = scanner.nextInt();
	                    scanner.nextLine(); // consume newline

	                    if (dpChoice == 1) {
	                        System.out.print("Enter customer ID to view profile: ");
	                        int custId = scanner.nextInt();
	                        scanner.nextLine();
	                        Customer cust = new Customer(custId, conn);
	                        System.out.println("Customer Profile:");
	                        System.out.println("Name: " + cust.getName());
	                        System.out.println("Dietary Preferences: " + cust.getDietaryPreferences());
	                        System.out.println("Allergies: " + cust.getAllergies());
	                    } else if (dpChoice == 2) {
	                        // View profiles for all customers
	                        CustomersDAO customersDAO = new CustomersDAO(conn);
	                        List<Customer> custList = customersDAO.viewCustomers();
	                        if (custList.isEmpty()) {
	                            System.out.println("No customer profiles found.");
	                        } else {
	                            System.out.println("All Customer Dietary Profiles:");
	                            for (Customer c : custList) {
	                                System.out.println("Customer ID: " + c.getCustomerId() +
	                                                   ", Name: " + c.getName() +
	                                                   ", Dietary Preferences: " + c.getDietaryPreferences() +
	                                                   ", Allergies: " + c.getAllergies());
	                            }
	                        }
	                    } else {
	                        System.out.println("Invalid option, returning to Chef Page.");
	                    }
	                    break;
	                case 3:
	                    // Update Task Status (only for tasks assigned to this chef)
	                    List<Task> allTasks = Task.getAllTasks();
	                    List<Task> chefTasks = new ArrayList<>();
	                    for (Task t : allTasks) {
	                        if (t.getChefId() == userId) {
	                            chefTasks.add(t);
	                        }
	                    }
	                    if (chefTasks.isEmpty()) {
	                        System.out.println("You have no tasks assigned.");
	                        break;
	                    }
	                    System.out.println("=== Your Tasks ===");
	                    for (Task t : chefTasks) {
	                        System.out.println("Task ID: " + t.getTaskId() +
	                                           ", Order ID: " + t.getOrderId() +
	                                           ", Description: " + t.getTaskDescription() +
	                                           ", Due: " + t.getDueTime() +
	                                           ", Status: " + t.getStatus());
	                    }
	                    System.out.print("Enter Task ID to update its status: ");
	                    int taskIdToUpdate = scanner.nextInt();
	                    scanner.nextLine();
	                    Task taskToUpdate = null;
	                    for (Task t : chefTasks) {
	                        if (t.getTaskId() == taskIdToUpdate) {
	                            taskToUpdate = t;
	                            break;
	                        }
	                    }
	                    if (taskToUpdate == null) {
	                        System.out.println("Task not found or not assigned to you.");
	                        break;
	                    }
	                    // Only allow marking the task as Completed (if not already completed)
	                    if (taskToUpdate.getStatus().equalsIgnoreCase("Completed")) {
	                        System.out.println("This task is already marked as Completed.");
	                        break;
	                    }
	                    System.out.print("Enter new task status (only 'Completed' is allowed): ");
	                    String newStatus = scanner.nextLine();
	                    while (!newStatus.equalsIgnoreCase("Completed")) {
	                        System.out.println("Invalid status. You can only mark a task as 'Completed'.");
	                        newStatus = scanner.nextLine();
	                    }
	                    // Update task using the existing updateTask method
	                    String updateTaskResult = Task.updateTask(taskToUpdate.getTaskId(), taskToUpdate.getChefId(), 
	                                                              taskToUpdate.getOrderId(), taskToUpdate.getTaskDescription(), 
	                                                              taskToUpdate.getDueTime(), newStatus);
	                    System.out.println("Update Task result: " + updateTaskResult);
	                    
	                    // If the task was updated to Completed, notify the customer that their order is ready.
	                    if (newStatus.equalsIgnoreCase("Completed")) {
	                        Order order = Order.readOrder(taskToUpdate.getOrderId(), conn);
	                        if (order != null) {
	                            int customerId = order.getCustomerId();
	                            Notifications notif = new Notifications(conn);
	                            String message = "Your order (Order ID: " + order.getOrderId() + ") is ready for pickup!";
	                            String notifResult = notif.addNotification(customerId, message);
	                            System.out.println("Notification sent to customer " + customerId + ": " + notifResult);
	                        } else {
	                            System.out.println("Unable to retrieve order details for notification.");
	                        }
	                    }
	                    break;
	                case 4:
	                    // Approve or Reject Custom Meals
	                    try {
	                        Statement stmt = conn.createStatement();
	                        String customQuery = "SELECT DISTINCT m.meal_id, m.name, o.order_id " +
	                                             "FROM OrderDetails od " +
	                                             "JOIN meals m ON od.meal_id = m.meal_id " +
	                                             "JOIN Orders o ON o.order_id = od.order_id " +
	                                             "WHERE m.name LIKE 'Custom%'";
	                        ResultSet rs = stmt.executeQuery(customQuery);
	                        List<Integer> customMealIds = new ArrayList<>();
	                        System.out.println("Custom Meal Orders Pending Approval:");
	                        while (rs.next()) {
	                            int mealId = rs.getInt("meal_id");
	                            int orderId = rs.getInt("order_id");
	                            String mealName = rs.getString("name");
	                            System.out.println("Order ID: " + orderId + ", Custom Meal: " + mealName + " (Meal ID: " + mealId + ")");
	                            if (!customMealIds.contains(mealId)) {
	                                customMealIds.add(mealId);
	                            }
	                        }
	                        rs.close();
	                        stmt.close();
	                        if (customMealIds.isEmpty()) {
	                            System.out.println("No custom meals pending approval.");
	                        } else {
	                            System.out.print("Enter a Meal ID from the list to approve/reject: ");
	                            int selectedMealId = scanner.nextInt();
	                            scanner.nextLine();
	                            if (customMealIds.contains(selectedMealId)) {
	                                System.out.print("Approve this custom meal? (Y/N): ");
	                                String decision = scanner.nextLine();
	                                if (decision.equalsIgnoreCase("Y")) {
	                                    // Approved: Remove "Custom " prefix from the meal name.
	                                    String getNameQuery = "SELECT name FROM meals WHERE meal_id = ?";
	                                    try (PreparedStatement psGet = conn.prepareStatement(getNameQuery)) {
	                                        psGet.setInt(1, selectedMealId);
	                                        try (ResultSet rsName = psGet.executeQuery()) {
	                                            if (rsName.next()) {
	                                                String customName = rsName.getString("name");
	                                                // Remove "Custom " prefix (case-insensitive)
	                                                String approvedName = customName.replaceFirst("(?i)^custom\\s+", "");
	                                                String updateNameQuery = "UPDATE meals SET name = ? WHERE meal_id = ?";
	                                                try (PreparedStatement psUpdate = conn.prepareStatement(updateNameQuery)) {
	                                                    psUpdate.setString(1, approvedName);
	                                                    psUpdate.setInt(2, selectedMealId);
	                                                    psUpdate.executeUpdate();
	                                                    System.out.println("Custom meal approved. Updated name: " + approvedName);
	                                                }
	                                            }
	                                        }
	                                    } catch (SQLException ex) {
	                                        ex.printStackTrace();
	                                    }
	                                    
	                                    // Automatically retrieve affected customer IDs from orders that contained the custom meal.
	                                    String affectedCustomersQuery = "SELECT DISTINCT o.customer_id " +
	                                                                    "FROM Orders o " +
	                                                                    "JOIN OrderDetails od ON o.order_id = od.order_id " +
	                                                                    "WHERE od.meal_id = ?";
	                                    List<Integer> affectedCustomerIds = new ArrayList<>();
	                                    try (PreparedStatement ps = conn.prepareStatement(affectedCustomersQuery)) {
	                                        ps.setInt(1, selectedMealId);
	                                        try (ResultSet rsAffected = ps.executeQuery()) {
	                                            while (rsAffected.next()) {
	                                                affectedCustomerIds.add(rsAffected.getInt("customer_id"));
	                                            }
	                                        }
	                                    } catch (SQLException e) {
	                                        e.printStackTrace();
	                                    }
	                                    
	                                    // Notify each affected customer that their custom meal has been approved.
	                                    Notifications notif = new Notifications(conn);
	                                    for (Integer affectedCustomerId : affectedCustomerIds) {
	                                        String message = "Your custom meal order (Meal ID " + selectedMealId + ") has been approved. Enjoy your meal!";
	                                        String notifResult = notif.addNotification(affectedCustomerId, message);
	                                        System.out.println("Notification for customer " + affectedCustomerId + ": " + notifResult);
	                                    }
	                                } else {
	                                	// Rejected branch: Delete the custom meal, remove it from orders, and automatically notify affected customers.
	                                	try {
	                                	    // Retrieve the custom meal's price (refund amount) before deletion.
	                                	    String priceQuery = "SELECT price FROM meals WHERE meal_id = ?";
	                                	    double refundAmount = 0.0;
	                                	    try (PreparedStatement psPrice = conn.prepareStatement(priceQuery)) {
	                                	        psPrice.setInt(1, selectedMealId);
	                                	        try (ResultSet rsPrice = psPrice.executeQuery()) {
	                                	            if (rsPrice.next()) {
	                                	                refundAmount = rsPrice.getDouble("price");
	                                	            }
	                                	        }
	                                	    }
	                                	    
	                                	    // Delete order details referencing the custom meal.
	                                	    String deleteOrderDetailsQuery = "DELETE FROM OrderDetails WHERE meal_id = ?";
	                                	    try (PreparedStatement psDeleteOD = conn.prepareStatement(deleteOrderDetailsQuery)) {
	                                	        psDeleteOD.setInt(1, selectedMealId);
	                                	        int affectedOrderDetails = psDeleteOD.executeUpdate();
	                                	        System.out.println("Removed " + affectedOrderDetails + " order details for the custom meal.");
	                                	    }
	                                	    
	                                	    // Delete the custom meal from the meals table.
	                                	    String deleteMealQuery = "DELETE FROM meals WHERE meal_id = ?";
	                                	    try (PreparedStatement psDeleteMeal = conn.prepareStatement(deleteMealQuery)) {
	                                	        psDeleteMeal.setInt(1, selectedMealId);
	                                	        psDeleteMeal.executeUpdate();
	                                	        System.out.println("Custom meal rejected and removed from the database.");
	                                	    }
	                                	    
	                                	    // Automatically retrieve affected customer IDs from orders that contained the custom meal.
	                                	 // Automatically retrieve affected customer IDs from orders that contained the custom meal.
	                                	    String affectedCustomersQuery = "SELECT DISTINCT o.customer_id " +
	                                	                                    "FROM Orders o " +
	                                	                                    "JOIN OrderDetails od ON o.order_id = od.order_id " +
	                                	                                    "WHERE od.meal_id = ?";
	                                	    List<Integer> affectedCustomerIds = new ArrayList<>();
	                                	    try (PreparedStatement ps = conn.prepareStatement(affectedCustomersQuery)) {
	                                	        ps.setInt(1, selectedMealId);
	                                	        try (ResultSet rsAffected = ps.executeQuery()) {
	                                	            while (rsAffected.next()) {
	                                	                affectedCustomerIds.add(rsAffected.getInt("customer_id"));
	                                	            }
	                                	        }
	                                	    }

	                                	    
	                                	    // Notify each affected customer.
	                                	    Notifications notif = new Notifications(conn);
	                                	    for (Integer affectedCustomerId : affectedCustomerIds) {
	                                	        String message = "Your custom meal order (Meal ID " + selectedMealId +
	                                	                         ") has been rejected and you have been refunded $" + refundAmount + ".";
	                                	        String notifResult = notif.addNotification(affectedCustomerId, message);
	                                	        System.out.println("Notification for customer " + affectedCustomerId + ": " + notifResult);
	                                	    }
	                                	} catch (SQLException ex) {
	                                	    ex.printStackTrace();
	                                	}
	                                }
	                            } else {
	                                System.out.println("Invalid Meal ID.");
	                            }
	                        }
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                    break;

	                case 5:
	                    // View Scheduled Cooking Tasks
	                    List<Task> tasks = Task.getAllTasks();
	                    boolean hasTasks = false;
	                    System.out.println("Scheduled Cooking Tasks for You:");
	                    for (Task t : tasks) {
	                        if (t.getChefId() == userId) {
	                            System.out.println("Task ID: " + t.getTaskId() +
	                                               ", Order ID: " + t.getOrderId() +
	                                               ", Description: " + t.getTaskDescription() +
	                                               ", Due: " + t.getDueTime() +
	                                               ", Status: " + t.getStatus());
	                            hasTasks = true;
	                        }
	                    }
	                    if (!hasTasks) {
	                        System.out.println("No tasks scheduled for you at this time.");
	                    }
	                    break;
	                case 6:
	                    logout();
	                    return; // Exit chefPage
	                default:
	                    System.out.println("Invalid choice, please try again.");
	                    break;
	            }
	        }
	    }

	    /**
	     * Helper method to display an order and its details.
	     */
	    private static void displayOrderWithDetails(Order o, MealDAO mealDAO) {
	        System.out.println("-------------------------------------------------");
	        System.out.println("Order ID: " + o.getOrderId() +
	                           ", Date: " + o.getOrderDate() +
	                           ", Total: $" + o.getTotalPrice());
	        // Retrieve and display order details
	        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(o.getOrderId());
	        if (details.isEmpty()) {
	            System.out.println("   No order details found.");
	        } else {
	            System.out.println("   Order Details:");
	            for (OrderDetails.OrderDetail detail : details) {
	                // Here we assume that the meal_id in OrderDetails corresponds to a meal
	                Meal meal = mealDAO.getMealById(conn, detail.getIngredientId());
	                String mealName = (meal != null) ? meal.getName() : "Unknown Meal";
	                System.out.println("      Meal: " + mealName +
	                                   " (ID: " + detail.getIngredientId() + "), " +
	                                   "Quantity: " + detail.getQuantity());
	            }
	        }
	    }


	    
	    private static void createorder(Scanner scanner, MealDAO mealDAO, int customerId, Order Order, Order newOrder) {
	        // Consume leftover newline
	        scanner.nextLine();

	        // Display available meals
	        List<Meal> availableMeals = mealDAO.getAvailableMeals(conn);
	        System.out.println("=== Available Meals ===");
	        for (Meal meal : availableMeals) {
	            System.out.println("Meal ID: " + meal.getId() +
	                               ", Name: " + meal.getName() +
	                               ", Description: " + meal.getDescription() +
	                               ", Price: " + meal.getPrice());
	        }

	        // Retrieve customer's dietary preference and allergy from Customer record
	        Customer customer = new Customer(customerId, conn);
	        String dietaryPref = customer.getDietaryPreferences(); // e.g., "Vegan", "Keto", etc.
	        String allergy = customer.getAllergies();              // e.g., "Dairy", "Nuts", etc.

	        newOrder.setCustomerId(customerId);
	        newOrder.setOrderDate(new Timestamp(System.currentTimeMillis()));
	        newOrder.setTotalPrice(0.0);

	        // Create the order in the database.
	        if (Order.createOrder(newOrder, conn)) {
	        	double totalPrice = 0.0;
	            
	            System.out.print("Enter number of meals in the order: ");
	            int numMeals = scanner.nextInt();
	            
	            OrderDetails.setConnection(conn);


	            // Instantiate a MealIngredientDAO to check meal ingredients
	            MealIngredientDAO miDAO = new MealIngredientDAO();

	            for (int i = 0; i < numMeals; i++) {
	                Meal meal = null;
	                int mealId;
	                // Keep prompting for a valid meal id
	                do {
	                    System.out.print("Enter meal id: ");
	                    mealId = scanner.nextInt();
	                    meal = mealDAO.getMealById(conn, mealId);
	                    if (meal == null) {
	                        System.out.println("Meal with id " + mealId + " not found. Please try again.");
	                    }
	                } while (meal == null);

	                // Retrieve the ingredients for the selected meal
	                List<MealIngredient> mealIngredients = miDAO.getMealIngredientsByMealId(conn, mealId);
	                boolean conflict = false;
	                String conflictMessage = "";

	                // Check dietary restrictions
	                if (dietaryPref.equalsIgnoreCase("Vegan")) {
	                    for (MealIngredient mi : mealIngredients) {
	                        String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                        if (isNonVeganIngredient(ingName)) {
	                            conflict = true;
	                            conflictMessage = "contains non-vegan ingredient: " + ingName;
	                            break;
	                        }
	                    }
	                } else if (dietaryPref.equalsIgnoreCase("Keto")) {
	                    for (MealIngredient mi : mealIngredients) {
	                        String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                        if (isHighCarbIngredient(ingName)) {
	                            conflict = true;
	                            conflictMessage = "contains high-carb ingredient: " + ingName;
	                            break;
	                        }
	                    }
	                } else if (dietaryPref.equalsIgnoreCase("Gluten-Free")) {
	                    for (MealIngredient mi : mealIngredients) {
	                        String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                        if (isGlutenIngredient(ingName)) {
	                            conflict = true;
	                            conflictMessage = "contains gluten ingredient: " + ingName;
	                            break;
	                        }
	                    }
	                } else if (dietaryPref.equalsIgnoreCase("Mediterranean")) {
	                    for (MealIngredient mi : mealIngredients) {
	                        String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                        if (isExcessRedMeat(ingName)) {
	                            conflict = true;
	                            conflictMessage = "contains high red-meat ingredient: " + ingName;
	                            break;
	                        }
	                    }
	                } else if (dietaryPref.equalsIgnoreCase("Pescatarian")) {
	                    for (MealIngredient mi : mealIngredients) {
	                        String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                        if (isNonPescatarianIngredient(ingName)) {
	                            conflict = true;
	                            conflictMessage = "contains non-pescatarian ingredient: " + ingName;
	                            break;
	                        }
	                    }
	                }

	                // Check allergies (if not "None")
	                if (!allergy.equalsIgnoreCase("None") && !conflict) {
	                    if (allergy.equalsIgnoreCase("Nuts")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isNutIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains nut ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    } else if (allergy.equalsIgnoreCase("Dairy")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isDairyIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains dairy ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    } else if (allergy.equalsIgnoreCase("Gluten")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isGlutenIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains gluten ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    } else if (allergy.equalsIgnoreCase("Seafood")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isSeafoodIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains seafood ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    } else if (allergy.equalsIgnoreCase("Eggs")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isEggIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains egg ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    } else if (allergy.equalsIgnoreCase("Soy")) {
	                        for (MealIngredient mi : mealIngredients) {
	                            String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                            if (isSoyIngredient(ingName)) {
	                                conflict = true;
	                                conflictMessage = "contains soy ingredient: " + ingName;
	                                break;
	                            }
	                        }
	                    }
	                }

	                // If conflict is detected, ask the customer for confirmation
	                if (conflict) {
	                    System.out.println("Warning: This meal " + conflictMessage + ". " +
	                                       "Your profile indicates you're " + dietaryPref + " and/or have a " + allergy + " allergy. " +
	                                       "Are you sure you want to order it? (Y/N)");
	                    String response = scanner.next();
	                    if (!response.equalsIgnoreCase("Y")) {
	                        System.out.println("Meal skipped.");
	                        continue;  // Skip this meal
	                    }
	                }

	                System.out.print("Enter quantity for meal " + mealId + ": ");
	                int quantity = scanner.nextInt();

	                // Update running total price
	                totalPrice += meal.getPrice() * quantity;

	                // Insert the order detail into the OrderDetails table
	                boolean detailAdded = OrderDetails.addOrderDetail(newOrder.getOrderId(), mealId, quantity);
	                if (detailAdded) {
	                    System.out.println("Added " + quantity + " of " + meal.getName() + " to the order.");
	                } else {
	                    System.out.println("Failed to add " + meal.getName() + " to the order.");
	                }
	            }

	            // Update the order's total price
	            newOrder.setTotalPrice(totalPrice);
	            if (Order.updateOrder(newOrder, conn)) {
	                System.out.println("Order updated successfully with total price: " + totalPrice);
	            } else {
	                System.out.println("Failed to update the order with total price.");
	            }
	            
	            int defaultChefId = 1;
	            String taskDesc = "Prepare Order #" + newOrder.getOrderId();
	            // Set a due time 1 hour from now (3600000 milliseconds)
	            Timestamp dueTime = new Timestamp(System.currentTimeMillis() + 3600000);
	            String taskStatus = "Pending";
	            String taskResult = Task.addTask(defaultChefId, newOrder.getOrderId(), taskDesc, dueTime, taskStatus);
	            
	        } else {
	            System.out.println("Failed to create the order.");
	        }
	    }


	    private static String getIngredientNameById(Connection conn, int ingredientId) {
	        String name = "";
	        String sql = "SELECT name FROM Ingredients WHERE ingredient_id = ?";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, ingredientId);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                name = rs.getString("name");
	            }
	            rs.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return name;
	    }

	    private static boolean isNonVeganIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("steak") || lower.contains("chicken") || lower.contains("pork") ||
	               lower.contains("beef") || lower.contains("lamb") || lower.contains("fish");
	    }

	    private static boolean isHighCarbIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("sugar") || lower.contains("flour") || lower.contains("rice") ||
	               lower.contains("pasta") || lower.contains("bread");
	    }

	    private static boolean isGlutenIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("wheat") || lower.contains("barley") || lower.contains("rye") ||
	               lower.contains("malt");
	    }

	    private static boolean isExcessRedMeat(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("beef") || lower.contains("pork") || lower.contains("lamb");
	    }

	    private static boolean isNonPescatarianIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        // For pescatarians, allow fish but not other meats.
	        return (lower.contains("steak") || lower.contains("chicken") || lower.contains("pork") ||
	                lower.contains("beef") || lower.contains("lamb")) && !lower.contains("fish");
	    }

	    

	    private static boolean isNutIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("almond") || lower.contains("walnut") || lower.contains("cashew") ||
	               lower.contains("pecan") || lower.contains("hazelnut");
	    }

	    private static boolean isDairyIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("milk") || lower.contains("cheese") || lower.contains("cream") ||
	               lower.contains("butter") || lower.contains("yogurt");
	    }

	    private static boolean isSeafoodIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("fish") || lower.contains("shrimp") || lower.contains("crab") ||
	               lower.contains("lobster") || lower.contains("oyster");
	    }

	    private static boolean isEggIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("egg");
	    }

	    private static boolean isSoyIngredient(String ingName) {
	        String lower = ingName.toLowerCase();
	        return lower.contains("soy");
	    }

	    
	    
	    private static void sysAdminPage() {
	    	CustomersDAO customersDAO = new CustomersDAO();
	    	LoginSystem LoginSystem= new LoginSystem(conn);
	    	Order Order= new Order();
	    	Scanner scanner = new Scanner(System.in);
	    	Order newOrder = new Order();
	    	MealDAO mealDAO = new MealDAO();
	    	int choice;
	    	
	    	while(true) {
	    		System.out.println("\n==== Welcome to the Admin Page ==== \n"+
	    							"1) Add customer \n"+
		        					"2) Add order \n"+
	    							"3) View All orders \n"+
	    							"4) View All Customers \n"+
		        					"5) Generate Financial Report \n"+
		        					"6) LogOut \n"+
		        					"======================================= \n");
		        choice = scanner.nextInt();
		        switch (choice) {
	            case 1:
	            	 scanner.nextLine();
	            	    
	            	    System.out.print("Enter customer name: ");
	            	    String name = scanner.nextLine();
	            	    
	            	    System.out.print("Enter customer email: ");
	            	    String email = scanner.nextLine();
	            	    
	            	    System.out.print("Enter customer phone: ");
	            	    String phone = scanner.nextLine();
	            	    
	            	    System.out.print("Enter dietary preferences (Vegan, Keto, Gluten-Free, Mediterranean, Pescatarian):");
	            	    String dietaryPreferences = scanner.nextLine();
	            	    
	            	    System.out.print("Enter allergies: ");
	            	    String allergies = scanner.nextLine();
	            	    
	            	    customersDAO.addCustomer(name,email,phone,dietaryPreferences,allergies);
	            	    System.out.println("Customer added successfully!");
	                break;
	            case 2:
	                scanner.nextLine();
	                
	                List<Meal> availableMeals = mealDAO.getAvailableMeals(conn);
	                System.out.println("=== Available Meals ===");
	                for (Meal meal : availableMeals) {
	                    System.out.println("Meal ID: " + meal.getId() +
	                                       ", Name: " + meal.getName() +
	                                       ", Description: " + meal.getDescription() +
	                                       ", Price: " + meal.getPrice());
	                }
	                
	                // Step 1: Create the Order
	                System.out.print("Enter customer id for the order: ");
	                int customerId = scanner.nextInt();
	                
	                createorder(scanner,mealDAO,customerId,Order,newOrder);
	            	
	                break;
	            case 3:
	            	List<Order> OrderList = Order.readAllOrders(conn);
	            	
	            	for (Order order : OrderList) {
	                    System.out.println("Order ID: " + order.getOrderId() +
	                                       ", Customer ID: " + order.getCustomerId() +
	                                       ", Order Date: " + order.getOrderDate() +
	                                       ", Total Price: " + order.getTotalPrice());
	                }
	            	
	                break;
	            case 4:
	                 List<Customer> customerList = customersDAO.viewCustomers();
	                 for (Customer customer : customerList) {
	                     System.out.println("ID: " + customer.getCustomerId() +
	                                        ", Name: " + customer.getName() +
	                                        ", Email: " + customer.getEmail() +
	                                        ", Phone: " + customer.getPhone() +
	                                        ", Dietary Preferences: " + customer.getDietaryPreferences() +
	                                        ", Allergies: " + customer.getAllergies());
	                 }
	                break;
	            case 5:
	            	gen_financial_report(conn); 
	            	break;
	            case 6:
	            	logout();
	            default:
	                System.out.println("Please Enter a valid choice:");
	                break;
		        }
	    	}
	    }
	    
	    private static void gen_financial_report(Connection conn) {
	        try {
	            Statement stmt = conn.createStatement();

	            // 1. Top 5 Best Seller Dishes
	            String topDishesQuery = "SELECT m.name, SUM(od.quantity) AS total_sold " +
	                                    "FROM OrderDetails od " +
	                                    "JOIN meals m ON od.meal_id = m.meal_id " +
	                                    "GROUP BY m.name " +
	                                    "ORDER BY total_sold DESC " +
	                                    "LIMIT 5";
	            ResultSet rs = stmt.executeQuery(topDishesQuery);
	            System.out.println("----- Top 5 Best Seller Dishes -----");
	            while (rs.next()) {
	                String dishName = rs.getString("name");
	                int totalSold = rs.getInt("total_sold");
	                System.out.println(dishName + ": " + totalSold + " sold");
	            }
	            rs.close();

	            // 2. Peak Rush Hour
	            String peakHourQuery = "SELECT HOUR(order_date) AS orderHour, COUNT(*) AS orderCount " +
	                                   "FROM Orders " +
	                                   "GROUP BY orderHour " +
	                                   "ORDER BY orderCount DESC " +
	                                   "LIMIT 1";
	            rs = stmt.executeQuery(peakHourQuery);
	            if (rs.next()) {
	                int peakHour = rs.getInt("orderHour");
	                int orderCount = rs.getInt("orderCount");
	                System.out.println("\n----- Peak Rush Hour -----");
	                System.out.println("Peak Hour: " + peakHour + ":00 with " + orderCount + " orders");
	            }
	            rs.close();

	            // 3. Revenue Summary
	            String revenueQuery = "SELECT SUM(total_price) AS totalRevenue, AVG(total_price) AS avgOrderValue " +
	                                  "FROM Orders";
	            rs = stmt.executeQuery(revenueQuery);
	            if (rs.next()) {
	                double totalRevenue = rs.getDouble("totalRevenue");
	                double avgOrderValue = rs.getDouble("avgOrderValue");
	                System.out.println("\n----- Revenue Summary -----");
	                System.out.println("Total Revenue: $" + totalRevenue);
	                System.out.println("Average Order Value: $" + avgOrderValue);
	            }
	            rs.close();

	            stmt.close();
	        } catch (SQLException e) {
	            System.err.println("Error generating financial report: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    
	    
	    private static void customerPage() {
	    	Scanner scanner = new Scanner(System.in);
	    	Notifications notif = new Notifications(conn);
	    	Order newOrder = new Order();
	    	MealDAO mealDAO = new MealDAO();
	    	Order Order= new Order();
	    	List<String> reminders = new ArrayList<>();
	    	int choice;
	    	
	    	while(true) {
	    		System.out.println("\n==== Welcome to the Customer Page ====");
		        System.out.println("1) Profile \n"+
		        					"2) Past Orders \n"+
		        					"3) Place an Order \n"+
		        					"4) Custom Order \n"+
		        					"5) Reminders \n"+
		        					"6) Get Recipe Recomendations From Ai \n"+
		        					"7) logout \n"+
		        					"======================================= \n");
		        choice = scanner.nextInt();
		        switch (choice) {
	            case 1:
	                CustomerProfile();
	                break;
	            case 2:
	                CustomerPastOrders();
	                break;
	            case 3:
	            	createorder(scanner,mealDAO,userId,Order,newOrder);
	                break;
	            case 4:
	            	create_custom_order(scanner, mealDAO, new MealIngredientDAO(), userId, Order, new Order());
	            case 5:
	            	reminders=notif.getAllNotifications(userId);
	            	for (int i = 0; i < reminders.size(); i++) {
	            	    System.out.println((i + 1) + ") " + reminders.get(i));
	            	}
	                break;
	            case 6:
	            	assistant_page(userId);
	            	break;
	            case 7:
	            	logout();
	            default:
	                System.out.println("Please Enter a valid choice:");
	                break;
		        }
	    	}
	    	
	    }
	    
	 // Add this function to your Main.java class

	    private static void create_custom_order(Scanner scanner, MealDAO mealDAO, MealIngredientDAO miDAO, int customerId, Order Order, Order newOrder) {
	        // Consume leftover newline
	        scanner.nextLine();
	        
	        // Create a new order for this custom meal
	        newOrder.setCustomerId(customerId);
	        newOrder.setOrderDate(new Timestamp(System.currentTimeMillis()));
	        newOrder.setTotalPrice(0.0);
	        if (!Order.createOrder(newOrder, conn)) {
	             System.out.println("Failed to create order for custom meal.");
	             return;
	        }
	        
	        // Display available ingredients
	        System.out.println("=== Available Ingredients ===");
	        try {
	             Statement stmt = conn.createStatement();
	             String sql = "SELECT * FROM Ingredients";
	             ResultSet rs = stmt.executeQuery(sql);
	             while(rs.next()){
	                 System.out.println("Ingredient ID: " + rs.getInt("ingredient_id") +
	                                    ", Name: " + rs.getString("name") +
	                                    ", Stock: " + rs.getInt("stock_quantity") +
	                                    ", Unit: " + rs.getString("unit") +
	                                    ", Price: $" + rs.getDouble("price"));
	             }
	             rs.close();
	             stmt.close();
	        } catch(SQLException e){
	             e.printStackTrace();
	        }
	        
	        // Prompt customer for custom meal details
	        System.out.print("Enter a name for your custom meal: ");
	        String mealName = scanner.nextLine();
	        mealName = "Custom " + mealName;
	        System.out.print("Enter a description for your custom meal: ");
	        String mealDesc = scanner.nextLine();
	        
	        System.out.print("How many ingredients would you like to add? ");
	        int numIngredients = scanner.nextInt();
	        
	        double mealPrice = 0.0;
	        // List to hold the custom meal ingredients for later insertion
	        List<MealIngredient> customIngredients = new ArrayList<>();
	        
	        // Loop to let the customer select ingredients and specify quantities
	     // Loop to let the customer select ingredients and specify quantities
	        for (int i = 0; i < numIngredients; i++) {
	             System.out.print("Enter ingredient id: ");
	             int ingredientId = scanner.nextInt();
	             System.out.print("Enter quantity for ingredient " + ingredientId + ": ");
	             int qty = scanner.nextInt();
	             double pricePerUnit = getIngredientPriceById(conn, ingredientId);
	             mealPrice += pricePerUnit * qty;
	             // Instead of generating a new meal ingredient ID, use a placeholder (e.g., 0)
	             MealIngredient mi = new MealIngredient(0, 0, ingredientId, qty);
	             customIngredients.add(mi);
	        }

	        
	        // Generate a new meal id
	        int newMealId = getNextMealId(conn);
	        // Create the custom Meal object
	        Meal customMeal = new Meal(newMealId, mealName, mealDesc, mealPrice);
	        // Insert the custom meal into the meals table
	        mealDAO.addMeal(conn, customMeal);
	        
	        // Now insert each custom ingredient with the correct meal id into the meal_ingredients table
	        for (MealIngredient mi : customIngredients) {
	             mi.setMealId(newMealId);
	             miDAO.addMealIngredient(conn, mi);
	        }
	        
	        // Ask the customer how many portions of this custom meal they want to order
	        System.out.print("Enter quantity of your custom meal you would like to order: ");
	        int orderQty = scanner.nextInt();
	        
	        // Add the custom meal as an order detail
	        OrderDetails.setConnection(conn);
	        boolean detailAdded = OrderDetails.addOrderDetail(newOrder.getOrderId(), newMealId, orderQty);
	        if (detailAdded) {
	             System.out.println(mealName + " added to your order.");
	        } else {
	             System.out.println("Failed to add custom meal to your order.");
	        }
	        
	        // Update the order's total price based on the custom meal price and quantity
	        newOrder.setTotalPrice(mealPrice * orderQty);
	        if (Order.updateOrder(newOrder, conn)) {
	             System.out.println("Order updated successfully with custom meal total price: $" + (mealPrice * orderQty));
	        } else {
	             System.out.println("Failed to update order with custom meal total price.");
	        }
	        
	        int defaultChefId = 1;
	        String taskDesc = "Prepare Custom Order #" + newOrder.getOrderId();
	        Timestamp dueTime = new Timestamp(System.currentTimeMillis() + 3600000);
	        String taskStatus = "Pending";
	        String taskResult = Task.addTask(defaultChefId, newOrder.getOrderId(), taskDesc, dueTime, taskStatus);
	        //System.out.println("Task creation result: " + taskResult);
	        
	    }


	    // --- Helper Methods ---

	    // Helper: Retrieve the next meal id (assumes meal_id is numeric)
	    private static int getNextMealId(Connection conn) {
	        int nextId = 0;
	        String sql = "SELECT MAX(meal_id) AS max_id FROM meals";
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	             if (rs.next()) {
	                  nextId = rs.getInt("max_id") + 1;
	             }
	        } catch(SQLException e) {
	             e.printStackTrace();
	        }
	        return nextId;
	    }

	    // Helper: Get the price of an ingredient from the Ingredients table
	    private static double getIngredientPriceById(Connection conn, int ingredientId) {
	        double price = 0.0;
	        String sql = "SELECT price FROM Ingredients WHERE ingredient_id = ?";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	             ps.setInt(1, ingredientId);
	             ResultSet rs = ps.executeQuery();
	             if (rs.next()) {
	                  price = rs.getDouble("price");
	             }
	             rs.close();
	        } catch(SQLException e) {
	             e.printStackTrace();
	        }
	        return price;
	    }

	    
	    private static void CustomerPastOrders() {
	        Scanner scanner = new Scanner(System.in);
	        
	        // Retrieve all orders for the given customer
	        List<Order> orders = Order.getOrdersByCustomerId(userId, conn);
	        
	        if(orders.isEmpty()){
	            System.out.println("No orders found for customer " + userId);
	            return;
	        }
	        
	        int currentPage = 0;
	        int ordersPerPage = 5;
	        int totalPages = (int) Math.ceil(orders.size() / (double) ordersPerPage);
	        
	        while(true) {
	            // Calculate the indexes for the current page
	            int startIndex = currentPage * ordersPerPage;
	            int endIndex = Math.min(startIndex + ordersPerPage, orders.size());
	            
	            // Display header with current page info
	            System.out.println("\n==== Customer Orders (Page " + (currentPage + 1) + " of " + totalPages + ") ====");
	            
	            // Display orders for the current page
	            for (int i = startIndex; i < endIndex; i++) {
	                int displayNum = i - startIndex + 1;
	                Order order = orders.get(i);
	                System.out.println(displayNum + ") Order ID: " + order.getOrderId() +
	                                   " | Date: " + order.getOrderDate() +
	                                   " | Total: $" + order.getTotalPrice());
	            }
	            
	            // Navigation instructions
	            System.out.println("Enter the number of the order to view its details.");
	            System.out.println("For Next Page, type '>>'. For Previous Page, type '<<'. To exit, type 'exit'.");
	            
	            String input = scanner.nextLine().trim();
	            if (input.equalsIgnoreCase(">>")) {
	                if (currentPage < totalPages - 1) {
	                    currentPage++;
	                } else {
	                    System.out.println("You are already on the last page.");
	                }
	            } else if (input.equalsIgnoreCase("<<")) {
	                if (currentPage > 0) {
	                    currentPage--;
	                } else {
	                    System.out.println("You are already on the first page.");
	                }
	            } else if (input.equalsIgnoreCase("exit")) {
	            	customerPage();
	            	break;
	            } else {
	                // Try parsing the input as a selection number
	                try {
	                    int selection = Integer.parseInt(input);
	                    if (selection < 1 || selection > (endIndex - startIndex)) {
	                        System.out.println("Invalid selection. Please enter a number between 1 and " + (endIndex - startIndex) + ".");
	                    } else {
	                        // Get the selected order and display its details
	                        Order selectedOrder = orders.get(startIndex + selection - 1);
	                        System.out.println("\n--- Order Details ---");
	                        System.out.println("Order ID: " + selectedOrder.getOrderId());
	                        System.out.println("Customer ID: " + selectedOrder.getCustomerId());
	                        System.out.println("Order Date: " + selectedOrder.getOrderDate());
	                        System.out.println("Total Price: $" + selectedOrder.getTotalPrice());
	                        System.out.println("---------------------");
	                        // Optionally wait for the user to press Enter before continuing
	                        System.out.println("Press Enter to continue...");
	                        scanner.nextLine();
	                    }
	                } catch (NumberFormatException e) {
	                    System.out.println("Invalid input. Please try again.");
	                }
	            }
	        }
	        scanner.close();
	    }

	    
	    private static void CustomerProfile() {
	    	Scanner scanner = new Scanner(System.in);
	    	int choice;
	    	int dpChoice;
	    	int allergyChoice;
	    	
	    	while(true) {
	    		Customer customer = new Customer(userId,conn);
		        System.out.println("\n==== Customer Profile ==== \n"+
		        					"   ID: "+ userId + "\n"+
		        					"1) Name:"+ customer.getName() +" \n"+
		        					"2) Email:"+ customer.getEmail()+ "\n"+
		        					"3) Phone:"+ customer.getPhone()+ "\n"+
		        					"4) Dietary Prefrences:"+ customer.getDietaryPreferences()+ "\n"+
		        					"5) Allergies:"+ customer.getAllergies()+ "\n"+
		        					"============================ \n"+
		        					"6) Return to Customer Page \n");
		        System.out.println("To update a Field enter the number adjacent to the field then enter the updated info");
		        choice = scanner.nextInt();
		        scanner.nextLine();
		        switch (choice) {
	            case 1:
	            	System.out.println("Please Enter your Name");
	                customer.setName(scanner.nextLine(),conn);
	                break;
	            case 2:
	            	System.out.println("Please Enter your Email");
	                customer.setEmail(scanner.nextLine(),conn);
	                break;
	            case 3:
	            	System.out.println("Please Enter your Phone");
	                customer.setPhone(scanner.nextLine(),conn);
	                break;
	            case 4:
	            	System.out.println("Please select your Dietary Preference from the following list by entering the number adjacent to your choice:");
	                // Display options using the ArrayList
	                for (int i = 0; i < dietaryPreferences.size(); i++) {
	                    System.out.println((i + 1) + ") " + dietaryPreferences.get(i));
	                }
	                dpChoice = scanner.nextInt();
	                scanner.nextLine();  // consume newline
	                String selectedPreference = "";
	                switch (dpChoice) {
	                    case 1:
	                        selectedPreference = dietaryPreferences.get(0);
	                        break;
	                    case 2:
	                        selectedPreference = dietaryPreferences.get(1);
	                        break;
	                    case 3:
	                        selectedPreference = dietaryPreferences.get(2);
	                        break;
	                    case 4:
	                        selectedPreference = dietaryPreferences.get(3);
	                        break;
	                    case 5:
	                        selectedPreference = dietaryPreferences.get(4);
	                        break;
	                    default:
	                        System.out.println("Invalid selection. Please try again.");
	                        continue; // Skip setting preference and restart loop
	                }
	                customer.setDietaryPreferences(selectedPreference, conn);
	                break;
	            case 5:
	            	System.out.println("Please select your Allergy from the following list by entering the number adjacent to your choice:");
	                for (int i = 0; i < allergyOptions.size(); i++) {
	                    System.out.println((i + 1) + ") " + allergyOptions.get(i));
	                }
	                allergyChoice = scanner.nextInt();
	                scanner.nextLine(); // consume newline
	                String selectedAllergy = "";
	                switch (allergyChoice) {
	                    case 1:
	                        selectedAllergy = allergyOptions.get(0);
	                        break;
	                    case 2:
	                        selectedAllergy = allergyOptions.get(1);
	                        break;
	                    case 3:
	                        selectedAllergy = allergyOptions.get(2);
	                        break;
	                    case 4:
	                        selectedAllergy = allergyOptions.get(3);
	                        break;
	                    case 5:
	                        selectedAllergy = allergyOptions.get(4);
	                        break;
	                    case 6:
	                        selectedAllergy = allergyOptions.get(5);
	                        break;
	                    case 7:
	                        selectedAllergy = allergyOptions.get(6);
	                        break;
	                    default:
	                        System.out.println("Invalid selection. Please try again.");
	                        continue; 
	                }
	                customer.setAllergies(selectedAllergy, conn);
	                break;
	            case 6:
	            	customerPage();
	            	break;
	            default:
	                System.out.println("Please Enter a valid choice (1-6):");
	                break;
		        }
	    		
	    	}
	        
	    }

	    
	    private static void login() {
	        try {
	            // Initialize the class-level connection outside of try-with-resources.
	            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Database connection failed.");
	            return;
	        }
	        
	        try (Scanner scanner = new Scanner(System.in)) {
	            LoginSystem loginSystem = new LoginSystem(conn);
	            
	            System.out.println("\n===== Welcome to Special Cook Restaurant System =====");
	            String role;
	            
	            while (true) {
	                System.out.print("Enter username: ");
	                username = scanner.nextLine();
	                
	                System.out.print("Enter password: ");
	                password = scanner.nextLine();
	                
	                String loginResult = loginSystem.login(username, password);
	                if (loginResult.startsWith("Login successful")) {
	                    System.out.println(loginResult);
	                    userId = extractUserId(loginResult);
	                    role = loginSystem.checkRole(userId);
	                    Pattern pattern = Pattern.compile("Role:\\s*(.+)");
	                    Matcher matcher = pattern.matcher(role);
	                    if (matcher.find()) {
	                        role = matcher.group(1).trim();
	                    } else {
	                        role = null;
	                    }

	                    break;
	                } else {
	                    System.out.println("\nInvalid credentials. Please try again.");
	                }
	            }
	            navigateToRolePage(role);
	        }
	    }

	    
	    private static void logout() {
	    	username=null;
	    	password=null;
	    	userId = -1;
	        
	        try {
	            if (conn != null && !conn.isClosed()) {
	                conn.close();
	                System.out.println("Have a nice Day :D");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error closing database connection.");
	        }
	        
	        login(); // Redirects back to login page
	    	
	    }
	    
	    private static void kitchenManagerPage() {
	        Scanner scanner = new Scanner(System.in);
	        int choice;
	        while (true) {
	        	System.out.println("\n==== Welcome to the Kitchen Manager Page ====");
	            System.out.println("1) Add Task");
	            System.out.println("2) Delete Task");
	            System.out.println("3) Update Task");
	            System.out.println("4) View Ingredient Stocks");
	            System.out.println("5) Check Low Stock Notifications");
	            System.out.println("6) View Supplier Pricing Info");
	            System.out.println("7) Manage Supplier Payments");
	            System.out.println("8) Log Out");
	            System.out.print("Enter your choice: ");
	            choice = scanner.nextInt();
	            scanner.nextLine(); // consume newline
	            
	            switch (choice) {
	                case 1:
	                    // Add Task: Show current tasks and available meals
	                    System.out.println("\n=== Current Tasks ===");
	                    List<Task> currentTasks = Task.getAllTasks();
	                    if (currentTasks.isEmpty()) {
	                        System.out.println("No tasks scheduled.");
	                    } else {
	                        for (Task t : currentTasks) {
	                            System.out.println("Task ID: " + t.getTaskId() +
	                                               ", Order ID: " + t.getOrderId() +
	                                               ", Chef ID: " + t.getChefId() +
	                                               ", Description: " + t.getTaskDescription() +
	                                               ", Due: " + t.getDueTime() +
	                                               ", Status: " + t.getStatus());
	                        }
	                    }
	                    System.out.println("\n=== Available Meals ===");
	                    MealDAO mealDAO = new MealDAO();
	                    List<Meal> meals = mealDAO.getAvailableMeals(conn);
	                    for (Meal meal : meals) {
	                        System.out.println("Meal ID: " + meal.getId() + ", Name: " + meal.getName());
	                    }
	                    // Prompt manager to add a task
	                    System.out.print("Enter Chef ID to assign task: ");
	                    int chefId = scanner.nextInt();
	                    System.out.print("Enter Order ID associated with the task: ");
	                    int orderId = scanner.nextInt();
	                    scanner.nextLine(); // consume newline
	                    System.out.print("Enter task description: ");
	                    String taskDesc = scanner.nextLine();
	                    System.out.print("Enter due time in minutes from now: ");
	                    int minutesFromNow = scanner.nextInt();
	                    scanner.nextLine(); // consume newline
	                    Timestamp dueTime = new Timestamp(System.currentTimeMillis() + minutesFromNow * 60000);
	                    System.out.print("Enter task status: ");
	                    String status = scanner.nextLine();
	                    String addTaskResult = Task.addTask(chefId, orderId, taskDesc, dueTime, status);
	                    System.out.println("Task creation result: " + addTaskResult);
	                    break;
	                case 2:
	                    // Delete Task: Show current tasks and then delete one.
	                    System.out.println("\n=== Current Tasks ===");
	                    currentTasks = Task.getAllTasks();
	                    if (currentTasks.isEmpty()) {
	                        System.out.println("No tasks scheduled.");
	                    } else {
	                        for (Task t : currentTasks) {
	                            System.out.println("Task ID: " + t.getTaskId() +
	                                               ", Order ID: " + t.getOrderId() +
	                                               ", Chef ID: " + t.getChefId() +
	                                               ", Description: " + t.getTaskDescription() +
	                                               ", Due: " + t.getDueTime() +
	                                               ", Status: " + t.getStatus());
	                        }
	                        System.out.print("Enter Task ID to delete: ");
	                        int taskIdToDelete = scanner.nextInt();
	                        scanner.nextLine();
	                        String deleteTaskResult = Task.deleteTask(taskIdToDelete);
	                        System.out.println("Delete Task result: " + deleteTaskResult);
	                    }
	                    break;
	                case 3:
	                    // Update Task: Show current tasks and allow only chef ID and status to be changed.
	                    System.out.println("\n=== Current Tasks ===");
	                    currentTasks = Task.getAllTasks();
	                    if (currentTasks.isEmpty()) {
	                        System.out.println("No tasks scheduled.");
	                    } else {
	                        for (Task t : currentTasks) {
	                            System.out.println("Task ID: " + t.getTaskId() +
	                                               ", Order ID: " + t.getOrderId() +
	                                               ", Chef ID: " + t.getChefId() +
	                                               ", Description: " + t.getTaskDescription() +
	                                               ", Due: " + t.getDueTime() +
	                                               ", Status: " + t.getStatus());
	                        }
	                        System.out.print("Enter Task ID to update: ");
	                        int taskIdToUpdate = scanner.nextInt();
	                        scanner.nextLine();
	                        Task taskToUpdate = null;
	                        for (Task t : currentTasks) {
	                            if (t.getTaskId() == taskIdToUpdate) {
	                                taskToUpdate = t;
	                                break;
	                            }
	                        }
	                        if (taskToUpdate == null) {
	                            System.out.println("Task not found.");
	                            break;
	                        }
	                        // Only allow updating chefId and status
	                        System.out.print("Enter new Chef ID: ");
	                        int newChefId = scanner.nextInt();
	                        scanner.nextLine();
	                        // Force the status to be either "Pending" or "Completed"
	                        System.out.print("Enter new task status (Pending/Completed): ");
	                        String newStatus = scanner.nextLine();
	                        while (!newStatus.equalsIgnoreCase("Pending") && !newStatus.equalsIgnoreCase("Completed")) {
	                            System.out.println("Invalid status. Please enter either 'Pending' or 'Completed'.");
	                            newStatus = scanner.nextLine();
	                        }
	                        // Retain existing order ID, description, and due time.
	                        String updateTaskResult = Task.updateTask(taskIdToUpdate, newChefId, 
	                                                                  taskToUpdate.getOrderId(), 
	                                                                  taskToUpdate.getTaskDescription(), 
	                                                                  taskToUpdate.getDueTime(), 
	                                                                  newStatus);
	                        System.out.println("Update Task result: " + updateTaskResult);
	                        
	                        // If status changed to "Completed", notify the customer that their order is ready.
	                        if (newStatus.equalsIgnoreCase("Completed")) {
	                            // Retrieve the order to get the customer ID
	                            Order order = Order.readOrder(taskToUpdate.getOrderId(), conn);
	                            if (order != null) {
	                                int customerId = order.getCustomerId();
	                                Notifications notif = new Notifications(conn);
	                                String message = "Your order (Order ID: " + order.getOrderId() + ") is ready for pickup!";
	                                String notifResult = notif.addNotification(customerId, message);
	                                System.out.println("Notification sent to customer " + customerId + ": " + notifResult);
	                            } else {
	                                System.out.println("Unable to retrieve order details for notification.");
	                            }
	                        }
	                    }
	                    break;

	                case 4:
	                    // View Ingredient Stocks
	                    InventoryCRUD inventoryCRUD = new InventoryCRUD(conn);
	                    List<String> inventoryList = inventoryCRUD.getInventoryList();
	                    System.out.println("\n=== Ingredient Stocks ===");
	                    for (String detail : inventoryList) {
	                        System.out.println(detail);
	                    }
	                    break;
	                case 5:
	                    // Check Low Stock Notifications
	                    int threshold = 10; // Set your low-stock threshold here.
	                    try {
	                        String lowStockQuery = "SELECT ingredient_id, name, stock_quantity FROM Ingredients WHERE stock_quantity < ?";
	                        PreparedStatement ps = conn.prepareStatement(lowStockQuery);
	                        ps.setInt(1, threshold);
	                        ResultSet rs = ps.executeQuery();
	                        boolean foundLow = false;
	                        System.out.println("\n=== Low Stock Notifications ===");
	                        while (rs.next()) {
	                            int ingId = rs.getInt("ingredient_id");
	                            String ingName = rs.getString("name");
	                            int stock = rs.getInt("stock_quantity");
	                            System.out.println("Low stock alert: Ingredient " + ingName + " (ID: " + ingId + ") has only " + stock + " left.");
	                            foundLow = true;
	                        }
	                        if (!foundLow) {
	                            System.out.println("No ingredients are low in stock.");
	                        }
	                        rs.close();
	                        ps.close();
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                    break;
	                case 6:
	                    // View Supplier Pricing Info
	                    viewSupplierPricingInfo();
	                    break;
	                case 7:
	                    // Manage Supplier Payments
	                    manageSupplierPayments(scanner);
	                    break;
	                case 8:
	                    logout();
	                default:
	                    System.out.println("Invalid choice, please try again.");
	                    break;
	            }
	        }
	    }
	    
	    private static void viewSupplierPricingInfo() {
	        System.out.println("\n=== Supplier Pricing Information ===");
	        // This query joins Ingredients, Inventory, and Suppliers.
	        String sql = "SELECT i.ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, " +
	                     "inv.stock_level, s.name AS supplier_name " +
	                     "FROM Ingredients i " +
	                     "JOIN Inventory inv ON i.ingredient_id = inv.ingredient_id " +
	                     "JOIN Suppliers s ON inv.supplier_id = s.supplier_id";
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) {
	                int ingId = rs.getInt("ingredient_id");
	                String ingName = rs.getString("ingredient_name");
	                double ingPrice = rs.getDouble("ingredient_price");
	                int stock = rs.getInt("stock_level");
	                String supplierName = rs.getString("supplier_name");
	                System.out.println("Ingredient ID: " + ingId +
	                                   ", Name: " + ingName +
	                                   ", Price: $" + ingPrice +
	                                   ", Stock: " + stock +
	                                   ", Supplier: " + supplierName);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void manageSupplierPayments(Scanner scanner) {
	        System.out.println("\n=== Manage Supplier Payments ===");
	        System.out.println("1) View all payments");
	        System.out.println("2) Update a payment");
	        System.out.println("3) Delete a payment");
	        System.out.print("Enter your choice: ");
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // consume newline
	        
	        SupplierPaymentsDAO spDAO = new SupplierPaymentsDAO(conn);
	        switch (choice) {
	            case 1:
	                List<String> paymentDetails = spDAO.getAllPaymentDetails();
	                System.out.println("\n=== Supplier Payments ===");
	                for (String detail : paymentDetails) {
	                    System.out.println(detail);
	                }
	                break;

	            case 2:
	                System.out.print("Enter Payment ID to update: ");
	                int paymentId = scanner.nextInt();
	                scanner.nextLine();
	                System.out.print("Enter new amount: ");
	                double newAmount = scanner.nextDouble();
	                scanner.nextLine();
	                System.out.print("Enter new status: ");
	                String newStatus = scanner.nextLine();
	                boolean updated = spDAO.updatePayment(paymentId, newAmount, newStatus);
	                System.out.println(updated ? "Payment updated successfully." : "Failed to update payment.");
	                break;
	            case 3:
	                System.out.print("Enter Payment ID to delete: ");
	                int paymentIdToDelete = scanner.nextInt();
	                scanner.nextLine();
	                boolean deleted = spDAO.deletePayment(paymentIdToDelete);
	                System.out.println(deleted ? "Payment deleted successfully." : "Failed to delete payment.");
	                break;
	            default:
	                System.out.println("Invalid choice.");
	                break;
	        }
	    }
	    
	    private static void assistant_page(int customerId) {
	        Scanner scanner = new Scanner(System.in);
	        
	        // Retrieve customer details
	        Customer customer = new Customer(customerId, conn);
	        String dietaryRestrictions = customer.getDietaryPreferences();
	        String allergy = customer.getAllergies();
	        
	        System.out.println("\n==== Assistant Page ====");
	        System.out.println("1) Recommendation for custom meal");
	        System.out.println("2) Recommendation for already available meals");
	        System.out.print("Enter your choice: ");
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // consume newline
	        
	        String prompt = "";
	        
	        if (choice == 1) {
	            // Option 1: Custom meal recommendation
	            InventoryCRUD inventoryCRUD = new InventoryCRUD(conn);
	            // Use the new method that returns human-readable ingredient details
	            List<String> ingredientDetails = inventoryCRUD.getInventoryListforai();
	            StringBuilder ingredientsStr = new StringBuilder();
	            for (String detail : ingredientDetails) {
	                ingredientsStr.append(detail).append(", ");
	            }
	            prompt = "You are a recipe recommendation assistant. A user has the following preferences:\n" +
	                     "Dietary Restrictions: " + dietaryRestrictions + "\n" +
	                     "Allergy: " + allergy + "\n" +
	                     "Ingredients Available: " + ingredientsStr.toString() + "\n" +
	                     "Provide recipes based on these preferences.";
	        } else if (choice == 2) {
	            // Option 2: Recommendation for already available meals
	            MealDAO mealDAO = new MealDAO();
	            List<Meal> meals = mealDAO.getAvailableMeals(conn);
	            MealIngredientDAO miDAO = new MealIngredientDAO();
	            StringBuilder mealsStr = new StringBuilder();
	            for (Meal m : meals) {
	                mealsStr.append("Meal: ").append(m.getName()).append(" - Ingredients: ");
	                List<MealIngredient> mealIngredients = miDAO.getMealIngredientsByMealId(conn, m.getId());
	                for (MealIngredient mi : mealIngredients) {
	                    String ingName = getIngredientNameById(conn, mi.getIngredientId());
	                    mealsStr.append(ingName).append(", ");
	                }
	                mealsStr.append("\n");
	            }
	            prompt = "You are a recipe recommendation assistant. A user has the following preferences:\n" +
	                     "Dietary Restrictions: " + dietaryRestrictions + "\n" +
	                     "Allergy: " + allergy + "\n" +
	                     "Below are the Available meals:\n" +
	                     mealsStr.toString() + "\n" +
	                     "Explain your recommendation clearly.";
	        } else {
	            System.out.println("Invalid choice. Returning to customer page.");
	            return;
	        }
	        
	        // Display the constructed prompt (for debugging)
	        System.out.println("Prompt :\n" + prompt);
	        
	        // Send the prompt and get the response
	        String response = Assistant.Send_Recieve(prompt);
	        System.out.println("\n=== Assistant Response ===");
	        System.out.println(response);
	    }


}