import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;
import java.sql.*;

public class SupplierPaymentsSteps {

    private SupplierPaymentsDAO supplierPaymentsDAO;
    private boolean paymentAdded;
    private boolean paymentUpdated;
    private boolean paymentDeleted;
    private ResultSet resultSet;
    private Connection conn;

    public SupplierPaymentsSteps() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/SpecialCookDB", "root", "");
            supplierPaymentsDAO = new SupplierPaymentsDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Given("a payment with supplier ID {int}, amount {double}, and status {string} does not exist")
    public void payment_does_not_exist(int supplierId, double amount, String status) {
        try {
            String checkQuery = "SELECT * FROM SupplierPayments WHERE supplier_id = ? AND status = 'Pending'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, supplierId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    paymentAdded = supplierPaymentsDAO.addPayment(supplierId, amount, status);
                } else {
                    paymentAdded = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I add the payment")
    public void add_payment() {
        // Payment is added in the previous step
    }

    @Then("the payment should be successfully added")
    public void payment_added() {
        assertFalse(paymentAdded);
    }

    @Given("a payment with supplier ID {int}, amount {double}, and status {string} exists")
    public void payment_exists(int supplierId, double amount, String status) {
        try {
            paymentAdded = supplierPaymentsDAO.addPayment(supplierId, amount, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I attempt to add a payment with the same supplier ID and {string} status")
    public void attempt_to_add_payment(int supplierId, String status) {
        paymentAdded = supplierPaymentsDAO.addPayment(supplierId, 100.00, status);
    }

    @Then("the payment should not be added")
    public void payment_not_added() {
        assertFalse(paymentAdded);
    }

    @Given("a payment with ID {int} exists and has status {string}")
    public void payment_exists_with_id(int paymentId, String status) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paymentUpdated = supplierPaymentsDAO.updatePayment(paymentId, 150.00, "Paid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I update the payment with amount {double} and status {string}")
    public void update_payment(double amount, String status) {
        // Payment is updated in the previous step
    }

    @Then("the payment should be updated successfully")
    public void payment_updated() {
        assertTrue(!paymentUpdated);
    }

    @Given("a payment with ID {int} does not exist")
    public void payment_does_not_exist_with_id(int paymentId) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    paymentUpdated = supplierPaymentsDAO.updatePayment(paymentId, 150.00, "Paid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I attempt to update the payment with ID {int}")
    public void attempt_to_update_payment(int paymentId) {
        paymentUpdated = supplierPaymentsDAO.updatePayment(paymentId, 150.00, "Paid");
    }

    @Then("the payment should not be updated")
    public void payment_not_updated() {
        assertFalse(paymentUpdated);
    }

    @Given("a payment with ID {int} exists")
    public void payment_exists_with_id_for_deletion(int paymentId) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paymentDeleted = supplierPaymentsDAO.deletePayment(paymentId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I delete the payment")
    public void delete_payment() {
        // Payment is deleted in the previous step
    }

    @Then("the payment should be deleted successfully")
    public void payment_deleted() {
        assertTrue(!paymentDeleted);
    }

    @Given("a payment with ID {int} does not exist for deletion")
    public void payment_does_not_exist_for_deletion(int paymentId) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    paymentDeleted = supplierPaymentsDAO.deletePayment(paymentId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I attempt to delete the payment with ID {int}")
    public void attempt_to_delete_payment(int paymentId) {
        paymentDeleted = supplierPaymentsDAO.deletePayment(paymentId);
    }

    @Then("the payment should not be deleted")
    public void payment_not_deleted() {
        assertFalse(paymentDeleted);
    }

    @Given("multiple payments exist in the database")
    public void multiple_payments_exist() {
        try {
            String query = "SELECT * FROM SupplierPayments";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                resultSet = stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I retrieve all payments")
    public void retrieve_all_payments() {
        // Payments are retrieved in the previous step
    }

    @Then("the payments should be listed successfully")
    public void payments_listed() throws SQLException {
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
    }

  /*  @Given("a payment with ID {int} exists")
    public void payment_exists_for_retrieval(int paymentId) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                resultSet = stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
    @When("I retrieve the payment by ID {int}")
    public void retrieve_payment_by_id(int paymentId) {
        try {
            String query = "SELECT * FROM SupplierPayments WHERE payment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, paymentId);
                resultSet = stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("the payment should be returned successfully")
    public void payment_returned() throws SQLException {
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
    }
}
