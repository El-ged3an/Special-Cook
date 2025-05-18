import org.junit.jupiter.api.*;
import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillingDAOJUnitTest {

    private static Connection cxn;
    private static BillingDAO dao;
    private static final int dummyOrderId = 1;
    private static final int dummyCustomerId = 1;
    private static final int dummyBillingId = 1;
    private static final double dummyAmount = 100.0;
    private static final String dummyStatus = "Paid";

    @BeforeAll
    public static void setup() throws Exception {
        cxn = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        dao = new BillingDAO(cxn);
    }

    @Test
    @Order(1)
    public void testAddBilling() {
        String res = dao.addBilling(dummyOrderId, dummyCustomerId, dummyAmount, dummyStatus);
        Assertions.assertNotNull(res);
    }

    @Test
    @Order(2)
    public void testGetBillingById() {
        String res = dao.getBillingById(dummyBillingId);
        Assertions.assertNotNull(res);
    }

    @Test
    @Order(3)
    public void testUpdateBilling() {
        String res = dao.updateBilling(dummyBillingId, dummyOrderId, dummyCustomerId, dummyAmount, dummyStatus);
        Assertions.assertNotNull(res);
    }

    @Test
    @Order(4)
    public void testDeleteBilling() {
        String res = dao.deleteBilling(dummyBillingId);
        Assertions.assertNotNull(res);
    }

    @AfterAll
    public static void cleanup() throws Exception {
        cxn.close();
    }
}
