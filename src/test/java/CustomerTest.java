import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    static Connection cx;

    @BeforeAll
    static void setup() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("MySQL Driver not found");
        }

        cx = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
       
    }

    @Test
    void testConstructorFromDB() {
        Customer ct = new Customer(1, cx);
        assertNotEquals("Lynx", ct.getName());
        assertNotEquals("lynx@mail.com", ct.getEmail());
        assertNotEquals("1234567890", ct.getPhone());
        
        assertNotEquals("Peanuts", ct.getAllergies());
    }

    @Test
    void testFieldConstructor() {
        Customer ct = new Customer(2, "Nova", "nova@mail.com", "9876543210", "Keto", "None");
        assertEquals(2, ct.getCustomerId());
        assertEquals("Nova", ct.getName());
        assertEquals("nova@mail.com", ct.getEmail());
        assertEquals("9876543210", ct.getPhone());
        assertEquals("Keto", ct.getDietaryPreferences());
        assertEquals("None", ct.getAllergies());
    }

    @Test
    void testSetters() {
        Customer ct = new Customer(1, cx);
        ct.setName("Echo", cx);
        ct.setEmail("echo@mail.com", cx);
        ct.setPhone("0001112222", cx);
        ct.setDietaryPreferences("Paleo", cx);
        ct.setAllergies("Gluten", cx);

        Customer updated = new Customer(1, cx);
        assertEquals("Echo", updated.getName());
        assertEquals("echo@mail.com", updated.getEmail());
        assertEquals("0001112222", updated.getPhone());
        assertEquals("Paleo", updated.getDietaryPreferences());
        assertEquals("Gluten", updated.getAllergies());
    }

    @AfterAll
    static void teardown() throws SQLException {
        try (Statement s = cx.createStatement()) {

 }
        if (cx != null) cx.close();
    }
}
