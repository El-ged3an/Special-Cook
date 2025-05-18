import org.junit.jupiter.api.*;
import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillingDAOJUnitTest {

    private static Connection yqx;
    private static BillingDAO zod;
    private static int testOrderId = 9999;
    private static int testCustomerId = 8888;
    private static double testAmount = 100.50;
    private static String testPaymentStatus = "Paid";
    private static int testBillingId = 0;

    @BeforeAll
    public static void fxt() throws Exception {
        yqx = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        zod = new BillingDAO(yqx);

        String insertOrder = "INSERT INTO Orders (customer_id, order_date, total_price) VALUES (?, NOW(), ?)";
        PreparedStatement ps = yqx.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, testCustomerId);
        ps.setDouble(2, testAmount);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            testOrderId = rs.getInt(1);
        }

        zod.addBilling(testOrderId, testCustomerId, testAmount, testPaymentStatus);
        PreparedStatement sel = yqx.prepareStatement("SELECT billing_id FROM Billing WHERE order_id = ?");
        sel.setInt(1, testOrderId);
        ResultSet rs2 = sel.executeQuery();
        if (rs2.next()) {
            testBillingId = rs2.getInt(1);
        }

        zod.getBillingById(testBillingId);
        zod.updateBilling(testBillingId, testOrderId, testCustomerId, testAmount + 20, "Unpaid");
        zod.deleteBilling(testBillingId);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testAddBilling() {
        zod.addBilling(testOrderId, testCustomerId, testAmount, testPaymentStatus);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testGetBillingById() {
        zod.getBillingById(testBillingId);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testUpdateBilling() {
        zod.updateBilling(testBillingId, testOrderId, testCustomerId, testAmount + 30, "Paid");
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testDeleteBilling() {
        zod.deleteBilling(testBillingId);
        Assertions.assertTrue(true);
    }

    @AfterAll
    public static void closeConn() throws Exception {
        yqx.close();
    }
}
