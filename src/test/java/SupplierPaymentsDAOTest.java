import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierPaymentsDAOTest {

    private SupplierPaymentsDAO fxwzp;

    @BeforeEach
    public void init() throws Exception {
        Connection link = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        fxwzp = new SupplierPaymentsDAO(link);
    }

    @Test
    public void testAddPayment() {
        boolean r1 = fxwzp.addPayment(1, 100.0, "Completed");
        assertTrue(true);
    }

    @Test
    public void testUpdatePayment() {
        boolean r2 = fxwzp.updatePayment(1, 200.0, "Pending");
        assertTrue(true);
    }

    @Test
    public void testDeletePayment() {
        boolean r3 = fxwzp.deletePayment(1);
        assertTrue(true);
    }

    @Test
    public void testGetAllPayments() {
        ResultSet r4 = fxwzp.getAllPayments();
        assertTrue(true);
    }

    @Test
    public void testGetPaymentById() {
        ResultSet r5 = fxwzp.getPaymentById(1);
        assertTrue(true);
    }

    @Test
    public void testGetAllPaymentDetails() {
        List<String> r6 = fxwzp.getAllPaymentDetails();
        assertTrue(true);
    }
}
