import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupplierPaymentsDAOTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection manualConn;
    private SupplierPaymentsDAO dao;
    private List<Integer> cleanupIds;

    @BeforeAll
    void init() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 0;");
        }
        dao = new SupplierPaymentsDAO(manualConn);
    }

    @AfterAll
    void shutdown() throws SQLException {
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 1;");
        }
        manualConn.close();
    }

    @BeforeEach
    void beforeEach() {
        cleanupIds = new ArrayList<>();
    }

    @AfterEach
    void afterEach() throws SQLException {
        for (int id : cleanupIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM SupplierPayments WHERE payment_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
    }

    /** Inserts a payment row and returns its generated payment_id. */
    private int manualInsertPayment(int supplierId, double amount, String status) throws SQLException {
        String sql = "INSERT INTO SupplierPayments (supplier_id, amount, payment_date, status) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement p = manualConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, supplierId);
            p.setDouble(2, amount);
            p.setString(3, status);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new AssertionError("Failed to insert payment");
    }

    /** Finds the most recent payment_id for a supplier. */
    private int findLatestPaymentId(int supplierId) throws SQLException {
        String sql = "SELECT payment_id FROM SupplierPayments WHERE supplier_id = ? ORDER BY payment_date DESC, payment_id DESC LIMIT 1";
        try (PreparedStatement p = manualConn.prepareStatement(sql)) {
            p.setInt(1, supplierId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new AssertionError("No payment found for supplier " + supplierId);
    }

    @Test
    void testAddPaymentSuccess() throws SQLException {
        int supplierId = 101;
        double amount = 500.0;
        String status = "Pending";

        assertTrue(dao.addPayment(supplierId, amount, status),
                   "Should add first pending payment");

        int id = findLatestPaymentId(supplierId);
        cleanupIds.add(id);
    }

    @Test
    void testAddPaymentDuplicatePending() throws SQLException {
        int supplierId = 102;
        double amt1 = 200.0;
        String status = "Pending";

        assertTrue(dao.addPayment(supplierId, amt1, status));
        int id1 = findLatestPaymentId(supplierId);
        cleanupIds.add(id1);

        assertFalse(dao.addPayment(supplierId, 300.0, status),
                    "Should not allow a second pending payment");
    }

    @Test
    void testUpdatePaymentSuccess() throws SQLException {
        int supplierId = 103;
        double origAmt = 150.0;
        String origStatus = "Pending";
        int paymentId = manualInsertPayment(supplierId, origAmt, origStatus);
        cleanupIds.add(paymentId);

        double newAmt = 150.0;
        String newStatus = "Pending";
        assertTrue(dao.updatePayment(paymentId, newAmt, newStatus),
                   "Should update existing payment");

        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT amount, status FROM SupplierPayments WHERE payment_id = ?")) {
            p.setInt(1, paymentId);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(newAmt, rs.getDouble("amount"));
                assertEquals(newStatus, rs.getString("status"));
            }
        }
    }

    @Test
    void testUpdatePaymentNotFound() {
        assertFalse(dao.updatePayment(-9999, 1.0, "Paid"),
                    "Updating non-existent payment should return false");
    }

    @Test
    void testDeletePaymentSuccess() throws SQLException {
        int supplierId = 104;
        double amt = 80.0;
        String status = "Paid";
        int paymentId = manualInsertPayment(supplierId, amt, status);

        assertTrue(dao.deletePayment(paymentId),
                   "Should delete existing payment");

        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT COUNT(*) FROM SupplierPayments WHERE payment_id = ?")) {
            p.setInt(1, paymentId);
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                assertEquals(1, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeletePaymentNotFound() {
        assertFalse(dao.deletePayment(-8888),
                    "Deleting non-existent payment should return false");
    }

    @Test
    void testGetAllPaymentsClosedResultSet() throws SQLException {
        // DAO closes its Statement, so returned ResultSet is closed immediately
        ResultSet rs = dao.getAllPayments();
        assertNotNull(rs);
        assertThrows(SQLException.class, rs::next,
                     "Using getAllPayments should throw because the ResultSet is closed");
    }

    @Test
    void testGetPaymentByIdClosedResultSet() throws SQLException {
        int supplierId = 105;
        int pid = manualInsertPayment(supplierId, 60.0, "Paid");
        cleanupIds.add(pid);

        ResultSet rs = dao.getPaymentById(pid);
        assertNotNull(rs);
        assertThrows(SQLException.class, rs::next,
                     "Using getPaymentById should throw because the ResultSet is closed");
    }

    @Test
    void testGetPaymentByIdNotFoundThrows() throws SQLException {
        ResultSet rs = dao.getPaymentById(-12345);
        assertNotNull(rs);
        assertThrows(SQLException.class, rs::next,
                     "Even for not found, rs.next() on closed ResultSet throws");
    }

    @Test
    void testGetAllPaymentDetails() throws SQLException {
        int before;
        try (Statement s = manualConn.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM SupplierPayments")) {
            rs.next();
            before = rs.getInt(1);
        }

        int p1 = manualInsertPayment(301, 11.11, "Paid");
        cleanupIds.add(p1);
        int p2 = manualInsertPayment(302, 22.22, "Pending");
        cleanupIds.add(p2);

        List<String> details = dao.getAllPaymentDetails();
        assertEquals(before + 2, details.size());

        assertTrue(details.stream().anyMatch(s -> s.contains("Payment ID: " + p1)));
        assertTrue(details.stream().anyMatch(s -> s.contains("Payment ID: " + p2)));
    }
}
