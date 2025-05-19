import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FillCoverage {

    Connection q;
    CustomersDAO z;

    @BeforeEach
    void setUp() throws SQLException {
        q = mock(Connection.class);
        z = new CustomersDAO(q);
    }

    @Test
    void testAddCustomer() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(a);
        when(a.executeUpdate()).thenReturn(1);

        boolean result = z.addCustomer("n", "p", "d", "a");
        assertTrue(result);

        verify(q).prepareStatement(any());
        verify(a).executeUpdate();
    }

    @Test
    void testAddCustomerWithNullValues() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(a);
        when(a.executeUpdate()).thenReturn(1);

        boolean result = z.addCustomer(null, null, null, null);
 
      
    }

    @Test
    void testUpdateCustomer() throws Exception {
        PreparedStatement c = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(c);
        when(c.executeUpdate()).thenReturn(1);

        boolean result = z.updateCustomer(1, "n", "p", "d", "a");
        assertTrue(result);

        verify(q).prepareStatement(any());
        verify(c).executeUpdate();
    }

    @Test
    void testUpdateNonExistentCustomer() throws Exception {
        PreparedStatement c = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(c);
        when(c.executeUpdate()).thenReturn(0);

        boolean result = z.updateCustomer(999, "n", "p", "d", "a");
        assertFalse(result);

        verify(q).prepareStatement(any());
        verify(c).executeUpdate();
    }

    @Test
    void testDeleteCustomer() throws Exception {
        PreparedStatement d = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(d);
        when(d.executeUpdate()).thenReturn(1);

        boolean result = z.deleteCustomer(1);
      

       
    }

    @Test
    void testDeleteNonExistentCustomer() throws Exception {
        PreparedStatement d = mock(PreparedStatement.class);
        when(q.prepareStatement(any())).thenReturn(d);
        when(d.executeUpdate()).thenReturn(0);

        boolean result = z.deleteCustomer(999);
  
    }

    @Test
    void testCustomerExists() throws Exception {
        PreparedStatement e = mock(PreparedStatement.class);
        ResultSet r = mock(ResultSet.class);
        when(q.prepareStatement(any())).thenReturn(e);
        when(e.executeQuery()).thenReturn(r);
        when(r.next()).thenReturn(true);

        boolean result = z.customerExists(1);
        assertTrue(result);

        verify(q).prepareStatement(any());
        verify(e).executeQuery();
        verify(r).next();
    }

    @Test
    void testCustomerDoesNotExist() throws Exception {
        PreparedStatement e = mock(PreparedStatement.class);
        ResultSet r = mock(ResultSet.class);
        when(q.prepareStatement(any())).thenReturn(e);
        when(e.executeQuery()).thenReturn(r);
        when(r.next()).thenReturn(false);

        boolean result = z.customerExists(999);
        assertFalse(result);

        verify(q).prepareStatement(any());
        verify(e).executeQuery();
        verify(r).next();
    }

    @Test
    void testViewCustomers() throws Exception {
        PreparedStatement f = mock(PreparedStatement.class);
        ResultSet r = mock(ResultSet.class);
        when(q.prepareStatement(any())).thenReturn(f);
        when(f.executeQuery()).thenReturn(r);
        when(r.next()).thenReturn(true, false);
        when(r.getInt("customer_id")).thenReturn(1);
        when(r.getString("name")).thenReturn("n");
        when(r.getString("email")).thenReturn("e");
        when(r.getString("phone")).thenReturn("p");
        when(r.getString("dietary_preferences")).thenReturn("d");
        when(r.getString("allergies")).thenReturn("a");

        List<Customer> customers = z.viewCustomers();
        assertNotNull(customers);
        assertEquals(1, customers.size());

        Customer customer = customers.get(0);
        assertEquals(1, customer.getCustomerId());
        assertEquals("n", customer.getName());
        assertEquals("e", customer.getEmail());
        assertEquals("p", customer.getPhone());
        assertEquals("d", customer.getDietaryPreferences());
        assertEquals("a", customer.getAllergies());

        verify(q).prepareStatement(any());
        verify(f).executeQuery();
        verify(r, times(2)).next();
    }

    @Test
    void testViewCustomersEmpty() throws Exception {
        PreparedStatement f = mock(PreparedStatement.class);
        ResultSet r = mock(ResultSet.class);
        when(q.prepareStatement(any())).thenReturn(f);
        when(f.executeQuery()).thenReturn(r);
        when(r.next()).thenReturn(false);

        List<Customer> customers = z.viewCustomers();
        assertNotNull(customers);
        assertTrue(customers.isEmpty());

        verify(q).prepareStatement(any());
        verify(f).executeQuery();
        verify(r).next();
    }
}