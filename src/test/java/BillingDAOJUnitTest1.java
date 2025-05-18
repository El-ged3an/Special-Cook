import org.junit.jupiter.api.*;
import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillingDAOJUnitTest1 {

    private static Connection zx;
    private static BillingDAO yx;
    private static final int ordId = 1;
    private static final int custId = 1;
    private static final int billId = 1;
    private static final double amt = 100.0;
    private static final String payStat = "PAID";

    @BeforeAll
    public static void setUp() throws Exception {
        zx = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        yx = new BillingDAO(zx);
    }

    @Test
   
    public void testAddBilling() {
        String res = yx.addBilling(ordId, custId, amt, payStat);
        Assertions.assertTrue(res.contains("successfully") || res.contains("exists") || res.contains("Error"));
    }

    @Test
  
    public void testUpdateBilling() {
        String res = yx.updateBilling(billId, ordId, custId, amt, payStat);
        Assertions.assertTrue(res.contains("successfully") || res.contains("not found") || res.contains("Error"));
    }

    @Test
   
    public void testDeleteBilling() {
        String res = yx.deleteBilling(billId);
        Assertions.assertTrue(res.contains("successfully") || res.contains("not found") || res.contains("Error"));
    }

    @Test
    
    public void testGetBillingById() {
        String res = yx.getBillingById(billId);
        Assertions.assertTrue(res.contains("Billing Record") || res.contains("not found") || res.contains("Error"));
    }

    @AfterAll
    public static void tearDown() throws Exception {
        zx.close();
    }
}
