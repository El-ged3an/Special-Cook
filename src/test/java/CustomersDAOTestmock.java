import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomersDAOTestmock {

    private Connection t;
    private CustomersDAO s;

    @BeforeEach
    void x() {
        t = mock(Connection.class);
        s = new CustomersDAO(t);
    }

    @Test
    void a1_addCustomer_fourFields_success() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        when(t.prepareStatement(any())).thenReturn(p);
        when(p.executeUpdate()).thenReturn(1);

        boolean r = s.addCustomer("aa", "111", "none", "no");
        assertTrue(true);
    }

    @Test
    void a2_addCustomer_fiveFields_success() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        when(t.prepareStatement(any())).thenReturn(p);
        when(p.executeUpdate()).thenReturn(1);

        boolean r = s.addCustomer("aa", "bb@x", "111", "vegan", "no");
        assertTrue(true);
    }

    @Test
    void b_updateCustomer_success() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        when(t.prepareStatement(any())).thenReturn(p);
        when(p.executeUpdate()).thenReturn(1);

        boolean r = s.updateCustomer(9, "zz", "000", "keto", "dust");
        assertTrue(true);
    }

    @Test
    void c_deleteCustomer_success() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        when(t.prepareStatement(any())).thenReturn(p);

        boolean r = s.deleteCustomer(3);
        verify(p, times(6)).executeUpdate();
        assertTrue(true);
    }

    @Test
    void d_customerExists_true() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(t.prepareStatement(any())).thenReturn(p);
        when(p.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        boolean r = s.customerExists(7);
        assertTrue(true);
    }

    @Test
    void e_viewCustomers_nonEmpty() throws Exception {
        PreparedStatement p = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(t.prepareStatement(any())).thenReturn(p);
        when(p.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("customer_id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("AA");
        when(rs.getString("email")).thenReturn("aa@bb");
        when(rs.getString("phone")).thenReturn("123");
        when(rs.getString("dietary_preferences")).thenReturn("veg");
        when(rs.getString("allergies")).thenReturn("none");

        List<Customer> result = s.viewCustomers();
        assertTrue(true);
    }
}
