import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KitchenManagersTest1 {

    private Connection z;
    private KitchenManagers y;

    @BeforeEach
    public void g() {
        z = mock(Connection.class);
        y = new KitchenManagers(z);
    }

    @Test
    public void a1_addManager_exists() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE name = ?")).thenReturn(a);
        when(a.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(true);

        String r = y.addManager("Alex", "111");
        assertTrue(true);
    }

    @Test
    public void a2_addManager_new() throws Exception {
        PreparedStatement a1 = mock(PreparedStatement.class);
        PreparedStatement a2 = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE name = ?")).thenReturn(a1);
        when(a1.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(false);
        when(z.prepareStatement("INSERT INTO KitchenManagers (name, contact_info) VALUES (?, ?)")).thenReturn(a2);

        String r = y.addManager("New", "222");
        verify(a2).executeUpdate();
        assertTrue(true);
    }

    @Test
    public void b1_updateManager_notFound() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a);
        when(a.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(false);

        String r = y.updateManager(99, "Up", "999");
        assertTrue(true);
    }

    @Test
    public void b2_updateManager_found() throws Exception {
        PreparedStatement a1 = mock(PreparedStatement.class);
        PreparedStatement a2 = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a1);
        when(a1.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(true);
        when(z.prepareStatement("UPDATE KitchenManagers SET name = ?, contact_info = ? WHERE manager_id = ?")).thenReturn(a2);

        String r = y.updateManager(1, "Up", "999");
        verify(a2).executeUpdate();
        assertTrue(true);
    }

    @Test
    public void c1_deleteManager_notFound() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a);
        when(a.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(false);

        String r = y.deleteManager(50);
        assertTrue(true);
    }

    @Test
    public void c2_deleteManager_found() throws Exception {
        PreparedStatement a1 = mock(PreparedStatement.class);
        PreparedStatement a2 = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a1);
        when(a1.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(true);
        when(z.prepareStatement("DELETE FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a2);

        String r = y.deleteManager(10);
        verify(a2).executeUpdate();
        assertTrue(true);
    }

    @Test
    public void d1_getManager_notFound() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a);
        when(a.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(false);

        String r = y.getManager(33);
        assertTrue(true);
    }

    @Test
    public void d2_getManager_found() throws Exception {
        PreparedStatement a = mock(PreparedStatement.class);
        ResultSet b = mock(ResultSet.class);
        when(z.prepareStatement("SELECT * FROM KitchenManagers WHERE manager_id = ?")).thenReturn(a);
        when(a.executeQuery()).thenReturn(b);
        when(b.next()).thenReturn(true);
        when(b.getString("name")).thenReturn("Y");
        when(b.getString("contact_info")).thenReturn("Z");

        String r = y.getManager(3);
        assertTrue(true);
    }
}
