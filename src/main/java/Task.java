import java.sql.*;
import java.util.*;

public class Task {
    private int taskId;
    private int chefId;
    private int orderId;
    private String taskDescription;
    private Timestamp dueTime;
    private String status;

    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Task(int taskId, int chefId, int orderId, String taskDescription, Timestamp dueTime, String status) {
        this.taskId = taskId;
        this.chefId = chefId;
        this.orderId = orderId;
        this.taskDescription = taskDescription;
        this.dueTime = dueTime;
        this.status = status;
    }
    
 // Getter and Setter for taskId
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    // Getter and Setter for chefId
    public int getChefId() {
        return chefId;
    }
    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    // Getter and Setter for orderId
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Getter and Setter for taskDescription
    public String getTaskDescription() {
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    // Getter and Setter for dueTime
    public Timestamp getDueTime() {
        return dueTime;
    }
    public void setDueTime(Timestamp dueTime) {
        this.dueTime = dueTime;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    public static boolean checkTaskExistsForOrder(int orderId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT COUNT(*) FROM Tasks WHERE order_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, orderId);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String addTask(int chefId, int orderId, String taskDescription, Timestamp dueTime, String status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Disable foreign key checks temporarily
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            }

            if (!checkTaskExistsForOrder(orderId)) {  // Change to check by orderId
                String query = "INSERT INTO Tasks (chef_id, order_id, task_description, due_time, status) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, chefId);
                    statement.setInt(2, orderId);
                    statement.setString(3, taskDescription);
                    statement.setTimestamp(4, dueTime);
                    statement.setString(5, status);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Re-enable foreign key checks
                        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                        return "Task added successfully.";
                    } else {
                        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                        return "Failed to add task.";
                    }
                }
            } else {
                // Re-enable foreign key checks before returning
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                }
                return "Task for the order already exists.";  // Return the correct message
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the exception to log any issues
            return "Error: " + e.getMessage();
        }
    }

 
    public static String updateTask(int taskId, int chefId, int orderId, String taskDescription, Timestamp dueTime, String status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Disable foreign key checks temporarily
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            }

            if (checkTaskExistsForOrder(taskId)) {
                String query = "UPDATE Tasks SET chef_id = ?, order_id = ?, task_description = ?, due_time = ?, status = ? WHERE task_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, chefId);
                    statement.setInt(2, orderId);
                    statement.setString(3, taskDescription);
                    statement.setTimestamp(4, dueTime);
                    statement.setString(5, status);
                    statement.setInt(6, taskId);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Re-enable foreign key checks
                        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                        return "Task updated successfully.";
                    }
                }
            } else {
                // Re-enable foreign key checks before returning
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                }
                return "Task not found for update.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to update task.";
    }

    public static String deleteTask(int taskId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Disable foreign key checks temporarily
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            }

            if (checkTaskExistsForOrder(taskId)) {
                String query = "DELETE FROM Tasks WHERE task_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, taskId);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Re-enable foreign key checks
                        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                        return "Task deleted successfully.";
                    }
                }
            } else {
                // Re-enable foreign key checks before returning
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
                }
                return "Task not found for deletion.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to delete task.";
    }

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Tasks";// NOSONAR
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        tasks.add(new Task(
                                rs.getInt("task_id"),
                                rs.getInt("chef_id"),
                                rs.getInt("order_id"),
                                rs.getString("task_description"),
                                rs.getTimestamp("due_time"),
                                rs.getString("status")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
