import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomersDAOTest {
    private static final String DB_URL      = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    private Connection manualConn;
    private CustomersDAO dao;
    private List<Integer> cleanupIds;

    @BeforeAll
    void init() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        manualConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        dao = new CustomersDAO(manualConn);

        // create stub tables so deleteCustomer doesn't fail
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS orders ("
                          + "order_id INT AUTO_INCREMENT PRIMARY KEY, "
                          + "customer_id INT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS orderdetails ("
                          + "orderdetails_id INT AUTO_INCREMENT PRIMARY KEY, "
                          + "order_id INT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS tasks ("
                          + "task_id INT AUTO_INCREMENT PRIMARY KEY, "
                          + "order_id INT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS billing ("
                          + "billing_id INT AUTO_INCREMENT PRIMARY KEY, "
                          + "customer_id INT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS notifications ("
                          + "notification_id INT AUTO_INCREMENT PRIMARY KEY, "
                          + "user_id INT)");
        }
    }

    @AfterAll
    void cleanupStubsAndClose() throws SQLException {
        // drop the stub tables
        try (Statement s = manualConn.createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS notifications");
            s.executeUpdate("DROP TABLE IF EXISTS billing");
            s.executeUpdate("DROP TABLE IF EXISTS tasks");
            s.executeUpdate("DROP TABLE IF EXISTS orderdetails");
            s.executeUpdate("DROP TABLE IF EXISTS orders");
        }
        if (manualConn != null && !manualConn.isClosed()) {
            manualConn.close();
        }
    }

    @BeforeEach
    void beforeEach() {
        cleanupIds = new ArrayList<>();
    }

    @AfterEach
    void afterEach() throws SQLException {
        // disable FK checks to clean up
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 0");
        }
        for (int id : cleanupIds) {
            try (PreparedStatement p = manualConn.prepareStatement(
                    "DELETE FROM Customers WHERE customer_id = ?")) {
                p.setInt(1, id);
                p.executeUpdate();
            }
        }
        try (Statement s = manualConn.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    // helper: find the ID of a customer by name
    private int findIdByName(String name) throws SQLException {
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT customer_id FROM Customers WHERE name = ?")) {
            p.setString(1, name);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new AssertionError("Customer not found: " + name);
    }

    @Test
    void testAddCustomerWithoutEmail() throws SQLException {
        String name = "CustNoEmail" + System.nanoTime();
        assertTrue(dao.addCustomer(name, "555-1234", "Vegan", "Peanuts"));

        int id = findIdByName(name);
        cleanupIds.add(id);

        assertTrue(dao.customerExists(id));
    }

    @Test
    void testAddCustomerWithEmail() throws SQLException {
        String name  = "CustWithEmail" + System.nanoTime();
        String email = name.toLowerCase() + "@test.local";
        assertTrue(dao.addCustomer(name, email, "555-5678", "Gluten-Free", "None"));

        int id = findIdByName(name);
        cleanupIds.add(id);

        assertTrue(dao.customerExists(id));
    }

    @Test
    void testAddDuplicateCustomer() throws SQLException {
        String name = "DupCust" + System.nanoTime();
        assertTrue(dao.addCustomer(name, "555-0000", "None", "None"));

        int id = findIdByName(name);
        cleanupIds.add(id);

        // inserting the same name again should return false
        assertFalse(dao.addCustomer(name, "x@x", "000", "None", "None"));
    }

    @Test
    void testUpdateCustomerSuccess() throws SQLException {
        String name = "UpdCust" + System.nanoTime();
        dao.addCustomer(name, "555-1111", "Keto", "Shellfish");
        int id = findIdByName(name);
        cleanupIds.add(id);

        assertTrue(dao.updateCustomer(id, "UpdatedName", "555-2222", "Paleo", "None"));

        // verify
        try (PreparedStatement p = manualConn.prepareStatement(
                "SELECT name, phone, dietary_preferences, allergies "
              + "FROM Customers WHERE customer_id = ?")) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("UpdatedName", rs.getString("name"));
                assertEquals("555-2222",   rs.getString("phone"));
                assertEquals("Paleo",      rs.getString("dietary_preferences"));
                assertEquals("None",       rs.getString("allergies"));
            }
        }
    }

    @Test
    void testUpdateNonexistentCustomer() {
        assertFalse(dao.updateCustomer(-99999, "X", "000", "None", "None"));
    }

    @Test
    void testDeleteCustomerSuccess() throws SQLException {
        String name = "DelCust" + System.nanoTime();
        dao.addCustomer(name, "555-3333", "Low-Carb", "None");
        int id = findIdByName(name);

        // delete via DAO (now stub tables exist, so no exception)
        assertTrue(dao.deleteCustomer(id));
        assertFalse(dao.customerExists(id));
    }

    @Test
    void testDeleteNonexistentCustomer() throws SQLException {
        // still returns true (no exception now)
        assertTrue(dao.deleteCustomer(-88888));
    }

    @Test
    void testCustomerExists() throws SQLException {
        String name = "ExistCust" + System.nanoTime();
        dao.addCustomer(name, "555-4444", "None", "None");
        int id = findIdByName(name);
        cleanupIds.add(id);

        assertTrue(dao.customerExists(id));
        assertFalse(dao.customerExists(-77777));
    }

    @Test
    void testViewCustomers() throws SQLException {
        var before = dao.viewCustomers();
        int baseCount = before.size();

        String n1 = "View1_" + System.nanoTime();
        String n2 = "View2_" + (System.nanoTime()+1);
        dao.addCustomer(n1, "v1@test", "555-0001", "None", "None");
        dao.addCustomer(n2, "555-0002", "Vegetarian", "None");
        int id1 = findIdByName(n1), id2 = findIdByName(n2);
        cleanupIds.add(id1);
        cleanupIds.add(id2);

        var after = dao.viewCustomers();
        assertEquals(baseCount + 2, after.size());

        Set<String> names = new HashSet<>();
        for (var c : after) names.add(c.getName());
        assertTrue(names.contains(n1));
        assertTrue(names.contains(n2));
    }
}
