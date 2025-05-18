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
        cx = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
        crud = new InventoryCRUD(cx);

        try (Statement s = cx.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Inventory (" +
                    "inventory_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "ingredient_id INT, " +
                    "supplier_id INT, " +
                    "stock_level INT, " +
                    "last_restocked TIMESTAMP)");

            s.executeUpdate("CREATE TABLE IF NOT EXISTS Ingredients (" +
                    "ingredient_id INT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "stock_quantity INT, " +
                    "unit VARCHAR(20), " +
                    "price DOUBLE)");

            s.executeUpdate("DELETE FROM Inventory");
            s.executeUpdate("DELETE FROM Ingredients");
            s.executeUpdate("INSERT INTO Ingredients (ingredient_id, name, stock_quantity, unit, price) VALUES (1, 'Salt', 100, 'kg', 2.5)");
        }
    }

    @Test
    void testAddInventory() throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String msg = crud.addInventory(1, 1, 50, ts);
        assertEquals("Inventory added successfully.", msg);

        // Try adding duplicate
        String msg2 = crud.addInventory(1, 1, 20, ts);
        assertEquals("Inventory entry already exists for the given ingredient and supplier.", msg2);
    }

    @Test
    void testUpdateInventory() throws SQLException {
        // Insert initial record
        Timestamp ts1 = new Timestamp(System.currentTimeMillis());
        crud.addInventory(1, 2, 30, ts1);

        // Get inventory_id of inserted record
        int invId = -1;
        try (PreparedStatement ps = cx.prepareStatement("SELECT inventory_id FROM Inventory WHERE ingredient_id = 1 AND supplier_id = 2")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) invId = rs.getInt("inventory_id");
        }

        Timestamp ts2 = new Timestamp(System.currentTimeMillis() + 10000);
        String msg = crud.updateInventory(invId, 1, 2, 60, ts2);
        assertEquals("Inventory updated successfully.", msg);

        // Check updated values
        try (PreparedStatement ps = cx.prepareStatement("SELECT stock_level, last_restocked FROM Inventory WHERE inventory_id = ?")) {
            ps.setInt(1, invId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals(60, rs.getInt("stock_level"));
                assertEquals(ts2, rs.getTimestamp("last_restocked"));
            } else {
                fail("Updated inventory not found");
            }
        }
    }

    @Test
    void testDeleteInventory() throws SQLException {
        // Insert record
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        crud.addInventory(1, 3, 40, ts);

        int invId = -1;
        try (PreparedStatement ps = cx.prepareStatement("SELECT inventory_id FROM Inventory WHERE ingredient_id = 1 AND supplier_id = 3")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) invId = rs.getInt("inventory_id");
        }

        String msg = crud.deleteInventory(invId);
        assertEquals("Inventory deleted successfully.", msg);

        // Try deleting again
        String msg2 = crud.deleteInventory(invId);
        assertEquals("Inventory entry not found.", msg2);
    }

    @Test
    void testGetInventoryList() throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        crud.addInventory(1, 4, 70, ts);

        List<String> list = crud.getInventoryList();
        assertTrue(list.stream().anyMatch(s -> s.contains("Ingredient ID: 1") && s.contains("Supplier ID: 4")));
    }

    @Test
    void testGetInventoryListforai() throws SQLException {
        List<String> ingredients = crud.getInventoryListforai();
        assertTrue(ingredients.stream().anyMatch(s -> s.contains("Salt") && s.contains("ID: 1")));
    }

    @AfterAll
    static void teardown() throws SQLException {
        try (Statement s = cx.createStatement()) {
            s.executeUpdate("DELETE FROM Inventory");
            s.executeUpdate("DELETE FROM Ingredients");
        }
        if (cx != null) cx.close();
    }
}
