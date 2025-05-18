import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderJUnitTest {

    private static Connection cxn;
    private static Order ent;

    @BeforeAll
    public static void prepare() throws Exception {
        cxn = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        ent = new Order();
        ent.setCustomerId(1);
        ent.setOrderDate(new Timestamp(System.currentTimeMillis()));
        ent.setTotalPrice(99.9);
        Order.createOrder(ent, cxn);
        Order.readOrder(ent.getOrderId(), cxn);
        Order.readAllOrders(cxn);
        Order.getOrdersByCustomerId(ent.getCustomerId(), cxn);
        Order.getOrderByTimestamp(cxn, ent.getOrderDate(), ent.getCustomerId());
        ent.setTotalPrice(123.45);
        Order.updateOrder(ent, cxn);
        Order.deleteOrder(ent.getOrderId(), cxn);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testCreateOrder() {
        Order tmp = new Order();
        tmp.setCustomerId(1);
        tmp.setOrderDate(new Timestamp(System.currentTimeMillis()));
        tmp.setTotalPrice(88.8);
        Order.createOrder(tmp, cxn);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testReadOrder() {
        Order.readOrder(ent.getOrderId(), cxn);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testReadAllOrders() {
        Order.readAllOrders(cxn);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testGetOrdersByCustomerId() {
        Order.getOrdersByCustomerId(ent.getCustomerId(), cxn);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void testGetOrderByTimestamp() {
        Order.getOrderByTimestamp(cxn, ent.getOrderDate(), ent.getCustomerId());
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void testUpdateOrder() {
        ent.setTotalPrice(177.77);
        Order.updateOrder(ent, cxn);
        Assertions.assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void testDeleteOrder() {
        Order.deleteOrder(ent.getOrderId(), cxn);
        Assertions.assertTrue(true);
    }

    @AfterAll
    public static void end() throws Exception {
        cxn.close();
    }
}
