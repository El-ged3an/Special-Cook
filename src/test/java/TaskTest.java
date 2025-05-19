import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    // a “manual” connection we use for cleanup & helpers
    private Connection manualConn;
    // track any task_ids we need to delete at teardown
    private List<Integer> cleanupTaskIds;

    @BeforeAll
    void initDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @AfterAll
    void closeDatabase() throws SQLException {
        if (manualConn != null && !manualConn.isClosed()) {
            manualConn.close();
        }
    }

    @BeforeEach
    void setUp() {
        cleanupTaskIds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // disable FK checks so we can delete whatever we inserted
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        }
        for (int id : cleanupTaskIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM Tasks WHERE task_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
        }
    }

    // helper to find the auto‐generated ID for a just‐inserted task
    private int findTaskIdByDescription(String description) throws SQLException {
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT task_id FROM Tasks WHERE task_description = ?")) {
            p.setString(1, description);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new AssertionError("Could not find task with description: " + description);
    }

    @Test
    void testCheckTaskExistsForOrderFalse() {
        int fakeOrder = -99999;
        assertFalse(Task.checkTaskExistsForOrder(fakeOrder),
                    "No task should exist for a random negative order ID");
    }

    @Test
    void testAddTaskSuccess() throws SQLException {
        int chefId = -1;
        int orderId = (int)(System.currentTimeMillis() & 0x7FFFFFFF);
        String desc = "JUnit AddSuccess " + System.nanoTime();
        Timestamp due = new Timestamp(System.currentTimeMillis() + 60000);
        String status = "PENDING";

        String res = Task.addTask(chefId, orderId, desc, due, status);
        assertEquals("Task added successfully.", res);

        // now checkExists should be true
        assertTrue(Task.checkTaskExistsForOrder(orderId));

        int taskId = findTaskIdByDescription(desc);
        cleanupTaskIds.add(taskId);
    }

    @Test
    void testAddTaskAlreadyExists() throws SQLException {
        int chefId  = -2;
        int orderId = (int)((System.currentTimeMillis() + 1) & 0x7FFFFFFF);
        String desc = "JUnit AddDup " + System.nanoTime();
        Timestamp due    = new Timestamp(System.currentTimeMillis() + 120000);
        String status = "Pending";            // ← must be exactly "Pending"

        // first insert
        String first = Task.addTask(chefId, orderId, desc, due, status);
        assertEquals("Task added successfully.", first);
        int taskId = findTaskIdByDescription(desc);
        cleanupTaskIds.add(taskId);

        // second insert should detect duplicate by orderId
        String second = Task.addTask(chefId, orderId, desc, due, status);
        assertEquals("Task for the order already exists.", second);

        // verify only one row in DB for that order
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT COUNT(*) FROM Tasks WHERE order_id = ?")) {
            p.setInt(1, orderId);
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                assertEquals(1, rs.getInt(1));
            }
        }
    }

    @Test
    void testUpdateTaskSuccess() throws SQLException {
        int testId = (int)((System.nanoTime()>>1) & 0x7FFFFFFF);
        String originalDesc = "JUnit UpdOrig " + testId;
        Timestamp originalDue = new Timestamp(System.currentTimeMillis() + 300000);
        insertManualTask(testId, -4, originalDesc, originalDue, "Pending");
        cleanupTaskIds.add(testId);

        // now update it
        String newDesc   = "JUnit UpdNew " + testId;
        // pick a dueTime with millisecond precision
        Timestamp newDue = new Timestamp(System.currentTimeMillis() + 360000);
        String newStatus = "Completed";
        int newChef      = -5;
        int newOrder     = -6;

        String res = Task.updateTask(
            testId, newChef, newOrder, newDesc, newDue, newStatus
        );
        assertEquals("Task updated successfully.", res);

        // verify the row was updated
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT chef_id, order_id, task_description, due_time, status "
              + "FROM Tasks WHERE task_id = ?")) {
            p.setInt(1, testId);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next(), "Updated row should exist");

                // compare chef/order/desc/status as before
                assertEquals(newChef, rs.getInt("chef_id"));
                assertEquals(newOrder, rs.getInt("order_id"));
                assertEquals(newDesc, rs.getString("task_description"));
                assertEquals(newStatus, rs.getString("status"));

                // compare timestamps to the second
                Timestamp actualDue = rs.getTimestamp("due_time");
                long expectedSecs = newDue.getTime() / 1000;
                long actualSecs   = actualDue.getTime() / 1000;

 }
        }
    }



    @Test
    void testGetAllTasks() throws SQLException {
        List<Task> before = Task.getAllTasks();
        int beforeCount = before.size();

        // insert two manual tasks with explicit IDs
        int id1 = (int)(System.nanoTime() & 0x7FFFFFFF);
        String d1 = "JUnit GA1 " + id1;
        insertManualTask(id1, -3, d1, new Timestamp(System.currentTimeMillis()+180000), "PENDING");
        cleanupTaskIds.add(id1);

        int id2 = id1 + 1;
        String d2 = "JUnit GA2 " + id2;
        insertManualTask(id2, -3, d2, new Timestamp(System.currentTimeMillis()+240000), "PENDING");
        cleanupTaskIds.add(id2);

        List<Task> after = Task.getAllTasks();
        assertEquals(beforeCount + 2, after.size(),
                     "getAllTasks should reflect the two new manual inserts");

        boolean found1 = false, found2 = false;
        for (Task t : after) {
            if (t.getTaskId() == id1 && d1.equals(t.getTaskDescription())) found1 = true;
            if (t.getTaskId() == id2 && d2.equals(t.getTaskDescription())) found2 = true;
        }
        assertTrue(found1, "Must see task " + id1);
        assertTrue(found2, "Must see task " + id2);
    }

    // inserts a row with task_id and order_id both = given id
    private void insertManualTask(int id, int chefId, String desc, Timestamp due, String status)
            throws SQLException {
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        }
        try (PreparedStatement p = manualConn.prepareStatement(
                "INSERT INTO Tasks "
              + "(task_id, chef_id, order_id, task_description, due_time, status) "
              + "VALUES (?, ?, ?, ?, ?, ?)")) {
            p.setInt(1, id);
            p.setInt(2, chefId);
            p.setInt(3, id);          // order_id = task_id
            p.setString(4, desc);
            p.setTimestamp(5, due);
            p.setString(6, status);
            p.executeUpdate();
        }
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
        }
    }


    @Test
    void testUpdateTaskNotFound() {
        String res = Task.updateTask(-777777, -1, -1,
                                     "nope", new Timestamp(System.currentTimeMillis()), "NONE");
        assertEquals("Task not found for update.", res);
    }

    @Test
    void testDeleteTaskSuccess() throws SQLException {
        int delId = (int)((System.nanoTime()>>2) & 0x7FFFFFFF);
        insertManualTask(delId, -6, "JUnit Del " + delId,
                         new Timestamp(System.currentTimeMillis()+420000), "PENDING");
        // delete it via the DAO
        String res = Task.deleteTask(delId);
        assertEquals("Task deleted successfully.", res);

        // verify it's gone
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT COUNT(*) FROM Tasks WHERE task_id = ?")) {
            p.setInt(1, delId);
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeleteTaskNotFound() {
        String res = Task.deleteTask(-888888);
        assertEquals("Task not found for deletion.", res);
    }
    
    @Test
    void testTaskPojoGettersAndSetters() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        // construct
        Task t = new Task(10, 20, 30, "Desc", ts, "NEW");
        assertEquals(10, t.getTaskId());
        assertEquals(20, t.getChefId());
        assertEquals(30, t.getOrderId());
        assertEquals("Desc", t.getTaskDescription());
        assertEquals(ts, t.getDueTime());
        assertEquals("NEW", t.getStatus());

        // mutate
        t.setTaskId(11);
        t.setChefId(21);
        t.setOrderId(31);
        t.setTaskDescription("Other");
        Timestamp ts2 = new Timestamp(ts.getTime() + 1000);
        t.setDueTime(ts2);
        t.setStatus("DONE");

        assertEquals(11, t.getTaskId());
        assertEquals(21, t.getChefId());
        assertEquals(31, t.getOrderId());
        assertEquals("Other", t.getTaskDescription());
        assertEquals(ts2, t.getDueTime());
        assertEquals("DONE", t.getStatus());
    }

}
