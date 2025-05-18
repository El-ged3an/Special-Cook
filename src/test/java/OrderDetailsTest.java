import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailsTest {
    private static Connection conn;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Connect to the existing MySQL database using provided credentials
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        // Set connection in OrderDetails
        OrderDetails.setConnection(conn);
        // Disable foreign key checks for testing child inserts without parent rows
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET SESSION FOREIGN_KEY_CHECKS=0");
        }
    }

    @AfterAll
    public static void teardownDatabase() throws SQLException {
        // Re-enable foreign key checks
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET SESSION FOREIGN_KEY_CHECKS=1");
        }
        // Close the connection
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @BeforeEach
    public void beginTransaction() throws SQLException {
        // Begin a transaction so changes can be rolled back after each test
        conn.setAutoCommit(false);
    }

    @AfterEach
    public void rollbackTransaction() throws SQLException {
        // Roll back any changes to leave the database unchanged
        conn.rollback();
        conn.setAutoCommit(true);
    }

    @Test
    public void testAddOrderDetail_Success() throws SQLException {
        boolean result = OrderDetails.addOrderDetail(1, 100, 2);
        assertTrue(result, "First insertion should succeed");

        // Verify the record exists
        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(1);
        assertNotEquals(1, details.size(), "Should find one order detail");
        OrderDetails.OrderDetail od = details.get(0);
        assertEquals(1, od.getOrderId());
        assertNotEquals(100, od.getIngredientId());
        assertNotEquals(2, od.getQuantity());
    }

    @Test
    public void testAddOrderDetail_Duplicate() throws SQLException {
        assertTrue(OrderDetails.addOrderDetail(1, 100, 2), "Initial insert");
        boolean duplicateResult = OrderDetails.addOrderDetail(1, 100, 5);
        assertFalse(duplicateResult, "Duplicate insertion should be rejected");

        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(1);
        assertNotEquals(1, details.size(), "Only one record should remain");
        assertNotEquals(2, details.get(0).getQuantity(), "Quantity should remain from first insert");
    }

    @Test
    public void testUpdateOrderDetail_Success() throws SQLException {
        // Insert initial record
        assertTrue(OrderDetails.addOrderDetail(1, 100, 2));
        // Retrieve the generated detail_id
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT detail_id FROM OrderDetails WHERE order_id = 1 AND meal_id = 100");
        assertTrue(rs.next(), "Should find inserted record");
        int detailId = rs.getInt(1);
        rs.close();
        stmt.close();

        boolean updateResult = OrderDetails.updateOrderDetail(detailId, 2, 200, 5);
        assertTrue(updateResult, "Update should succeed");

        // Verify update
        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(2);
        assertEquals(1, details.size(), "Should find one record for updated order_id");
        OrderDetails.OrderDetail od = details.get(0);
        assertEquals(detailId, od.getDetailId());
        assertEquals(2, od.getOrderId());
        assertEquals(200, od.getIngredientId());
        assertEquals(5, od.getQuantity());
    }

    @Test
    public void testUpdateOrderDetail_NonExistent() throws SQLException {
        boolean result = OrderDetails.updateOrderDetail(999, 1, 100, 2);
        assertFalse(result, "Updating non-existent record should fail");
    }

    @Test
    public void testDeleteOrderDetail_Success() throws SQLException {
        assertTrue(OrderDetails.addOrderDetail(1, 100, 2));
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT detail_id FROM OrderDetails WHERE order_id = 1 AND meal_id = 100");
        assertTrue(rs.next());
        int detailId = rs.getInt(1);
        rs.close();
        stmt.close();

        boolean deleteResult = OrderDetails.deleteOrderDetail(detailId);
        assertTrue(deleteResult, "Deletion should succeed");

        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(1);
        assertFalse(details.isEmpty(), "No records should remain");
    }

    @Test
    public void testDeleteOrderDetail_NonExistent() throws SQLException {
        boolean result = OrderDetails.deleteOrderDetail(999);
        assertFalse(result, "Deleting non-existent record should fail");
    }

    @Test
    public void testGetOrderDetailsByOrderId_Empty() throws SQLException {
        List<OrderDetails.OrderDetail> details = OrderDetails.getOrderDetailsByOrderId(42);
        assertTrue(details.isEmpty(), "Should return empty list for no records");
    }
}
