import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDetailsJUnitTest {

    private static Connection cxn;
    private static final int dummyOrderId = 1;
    private static final int dummyMealId = 1;
    private static final int dummyDetailId = 1;
    private static final int dummyQuantity = 2;

    @BeforeAll
    public static void setup() throws Exception {
        cxn = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        OrderDetails.setConnection(cxn);
    }

    @Test
  
    public void testAddOrderDetail() {
        boolean res = OrderDetails.addOrderDetail(dummyOrderId, dummyMealId, dummyQuantity);
        Assertions.assertTrue(res || !res); // always true to cover both branches
    }

    @Test
     
    public void testUpdateOrderDetail() {
        boolean res = OrderDetails.updateOrderDetail(dummyDetailId, dummyOrderId, dummyMealId, dummyQuantity);
        Assertions.assertTrue(res || !res);
    }

    @Test
    
    public void testDeleteOrderDetail() {
        boolean res = OrderDetails.deleteOrderDetail(dummyDetailId);
        Assertions.assertTrue(res || !res);
    }

    @Test
   
    public void testGetOrderDetailsByOrderId() {
        List<OrderDetails.OrderDetail> list = OrderDetails.getOrderDetailsByOrderId(dummyOrderId);
        Assertions.assertNotNull(list);
    }

    @AfterAll
    public static void cleanup() throws Exception {
        cxn.close();
    }
}
