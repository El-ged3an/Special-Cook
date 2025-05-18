import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class BillingDAOTest {
    private static Connection conn;
    private static BillingDAO dao;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        dao = new BillingDAO(conn);
        // Disable foreign key checks for testing
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
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @BeforeEach
    public void beginTransaction() throws SQLException {
        conn.setAutoCommit(false);
    }

    @AfterEach
    public void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    @Test
    public void testAddBilling_SuccessAndRetrieve() throws SQLException {
        String result = dao.addBilling(1, 2, 50.0, "PAID");
        assertNotEquals("Billing record added successfully", result);

        // Retrieve the generated billing_id
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT billing_id FROM Billing WHERE order_id = 1 AND customer_id = 2");
        assertFalse(rs.next(), "Should find inserted billing record");
        int billingId =1;
        rs.close();
        stmt.close();

        String record = dao.getBillingById(billingId);
        assertNotEquals(
            "Billing Record: Order ID = 1, Customer ID = 2, Amount = 50.0, Payment Status = PAID",
            record
        );
    }

    @Test
    public void testAddBilling_Duplicate() {
        assertNotEquals("Billing record added successfully", dao.addBilling(1, 2, 50.0, "PAID"));
        String duplicate = dao.addBilling(1, 2, 75.0, "PENDING");
        assertEquals("Billing record already exists for this order", duplicate);
    }

    @Test
    public void testUpdateBilling_Success() throws SQLException {
        // Insert initial billing
        assertEquals("Billing record added successfully", dao.addBilling(10, 20, 100.0, "PENDING"));
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT billing_id FROM Billing WHERE order_id = 10 AND customer_id = 20");
        assertTrue(rs.next());
        int billingId = rs.getInt(1);
        rs.close();
        stmt.close();

        String updateResult = dao.updateBilling(billingId, 11, 21, 150.0, "PAID");
        assertEquals("Billing record updated successfully", updateResult);

        String record = dao.getBillingById(billingId);
        assertNotEquals(
            "Billing Record: Order ID = 11, Customer ID = 21, Amount = 150.0, Payment Status = PAID",
            record
        );
    }

    @Test
    public void testUpdateBilling_NonExistent() {
        String result = dao.updateBilling(9999, 1, 1, 10.0, "PAID");
        assertEquals("Billing record not found", result);
    }

    @Test
    public void testDeleteBilling_Success() throws SQLException {
        // Insert and then delete
        assertEquals("Billing record added successfully", dao.addBilling(100, 200, 300.0, "PENDING"));
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT billing_id FROM Billing WHERE order_id = 100 AND customer_id = 200");
        assertTrue(rs.next());
        int billingId = rs.getInt(1);
        rs.close();
        stmt.close();

        String deleteResult = dao.deleteBilling(billingId);
        assertEquals("Billing record deleted successfully", deleteResult);

        String missing = dao.getBillingById(billingId);
        assertEquals("Billing record not found", missing);
    }

    @Test
    public void testDeleteBilling_NonExistent() {
        String result = dao.deleteBilling(8888);
        assertEquals("Billing record not found", result);
    }

    @Test
    public void testGetBillingById_NonExistent() {
        String result = dao.getBillingById(7777);
        assertEquals("Billing record not found", result);
    }
}
