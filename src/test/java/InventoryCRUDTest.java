import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryCRUDTest {

    static Connection cx;
    static InventoryCRUD crud;

    @BeforeAll
    static void setup() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("MySQL Driver not found");
        }
        cx = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "your_username", "your_password");
        crud = new InventoryCRUD(cx);
    }

    @Test
    void testAddInventory() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String msg = crud.addInventory(9999, 9999, 9999, ts);
        assertTrue(msg != null && !msg.isEmpty());
    }

    @Test
    void testUpdateInventory() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String msg = crud.updateInventory(-1, 9999, 9999, 9999, ts);
        assertTrue(msg != null && !msg.isEmpty());
    }

    @Test
    void testDeleteInventory() {
        String msg = crud.deleteInventory(-1);
        assertTrue(msg != null && !msg.isEmpty());
    }

    @Test
    void testGetInventoryList() {
        List<String> list = crud.getInventoryList();
        assertNotNull(list);
    }

    @Test
    void testGetInventoryListforai() {
        List<String> ingredients = crud.getInventoryListforai();
        assertNotNull(ingredients);
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (cx != null) cx.close();
    }
}
