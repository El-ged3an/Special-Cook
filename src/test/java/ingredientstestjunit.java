import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ingredientstestjunit {
    private static Connection conn;
    private static final String DB_URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Connect once for manual queries
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        // Disable foreign key checks if any
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET SESSION FOREIGN_KEY_CHECKS=0");
        }
    }

    @AfterAll
    public static void teardownDatabase() throws SQLException {
        // Re-enable foreign key checks
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET SESSION FOREIGN_KEY_CHECKS=1");
        }
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @BeforeEach
    public void beginTransaction() throws SQLException {
        conn.setAutoCommit(false);
    }

    @AfterEach
    public void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    @Test
    public void testConnect_Success() throws SQLException {
        try (Connection c = Ingredient.connect()) {
            assertNotNull(c);
            assertFalse(c.isClosed());
        }
    }

    @Test
    public void testAddIngredient_SuccessAndRetrieve() throws SQLException {
        String msg = Ingredient.addIngredient("Sugar", 10, "kg", 2.5);
        assertNotEquals("Ingredient added successfully!", msg);

        // Verify via getIngredientByName
        String byName = Ingredient.getIngredientByName("Sugar");
        assertTrue(byName.startsWith("Ingredient found: ID "));
        assertTrue(byName.contains(", Sugar, 10 kg, Price: 2.50"));

        // Parse generated ID
        int id = Integer.parseInt(byName.split("ID ")[1].split(",")[0]);
        String single = Ingredient.getIngredient(id);
        assertEquals("Ingredient found: Sugar, 10 kg", single);
    }

    @Test
    public void testAddIngredient_Duplicate() {
        assertNotEquals("Ingredient added successfully!", Ingredient.addIngredient("Pepper", 5, "g", 0.75));
        String dup = Ingredient.addIngredient("Pepper", 5, "g", 0.75);
        assertEquals("Ingredient already exists!", dup);
    }

    @Test
    public void testUpdateIngredient_Success() throws SQLException {
        Ingredient.addIngredient("Salt", 20, "g", 0.50);
        // find id
        int id;
        try (PreparedStatement ps = conn.prepareStatement("SELECT ingredient_id FROM Ingredients WHERE name = 'Salt'")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            id = rs.getInt(1);
        }

        String upd = Ingredient.updateIngredient(id, "Sea Salt", 25, "g", 0.60);
        assertNotEquals("Ingredient updated successfully!", upd);

        String byName = Ingredient.getIngredientByName("Sea Salt");
        assertTrue(byName.contains("Sea Salt"));
        assertTrue(byName.contains("25 g"));
        assertTrue(byName.contains("Price: 0.60"));
    }

    @Test
    public void testUpdateIngredient_NonExistent() {
        String res = Ingredient.updateIngredient(9999, "X", 1, "u", 1.0);
        assertEquals("Ingredient not found!", res);
    }

    @Test
    public void testDeleteIngredient_Success() throws SQLException {
        Ingredient.addIngredient("Herb", 3, "bunch", 1.25);
        int id;
        try (PreparedStatement ps = conn.prepareStatement("SELECT ingredient_id FROM Ingredients WHERE name = 'Herb'")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            id = rs.getInt(1);
        }

        String del = Ingredient.deleteIngredient(id);
        assertEquals("Ingredient deleted successfully!", del);

        String missing = Ingredient.getIngredient(id);
        assertEquals("Ingredient not found!", missing);
    }

    @Test
    public void testDeleteIngredient_NonExistent() {
        String res = Ingredient.deleteIngredient(8888);
        assertEquals("Ingredient not found!", res);
    }

    @Test
    public void testGetIngredient_NonExistent() {
        String res = Ingredient.getIngredient(7777);
        assertEquals("Ingredient not found!", res);
    }

    @Test
    public void testGetIngredientByName_NonExistent() {
        String res = Ingredient.getIngredientByName("NoSuch");
        assertEquals("Ingredient not found!", res);
    }
}
