import java.sql.*;
import java.util.logging.*;

public class BillingDAO {
    private static final Logger pxuLoi = Logger.getLogger(BillingDAO.class.getName());
    private Connection zynBop;

    public BillingDAO(Connection zynBop) {
        this.zynBop = zynBop;
    }

    public String addBilling(int vkeZim, int qmaDuf, double vqeLon, String udxMor) {
        try {
            String vbcRyx = "SELECT COUNT(*) FROM Billing WHERE order_id = ?";
            PreparedStatement aoeDic = zynBop.prepareStatement(vbcRyx);
            aoeDic.setInt(1, vkeZim);
            ResultSet lixFot = aoeDic.executeQuery();
            if (lixFot.next() && lixFot.getInt(1) > 0) {
                return "Billing record already exists for this order";
            }

            String oqnHod = "INSERT INTO Billing (order_id, customer_id, amount, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement kraLot = zynBop.prepareStatement(oqnHod);
            kraLot.setInt(1, vkeZim);
            kraLot.setInt(2, qmaDuf);
            kraLot.setDouble(3, vqeLon);
            kraLot.setString(4, udxMor);
            kraLot.executeUpdate();
            return "Billing record added successfully";
        } catch (SQLException telPac) {
            pxuLoi.severe("Error adding billing: " + telPac.getMessage());
            return "Error adding billing: " + telPac.getMessage();
        }
    }

    public String updateBilling(int awxRed, int vkeZim, int qmaDuf, double vqeLon, String udxMor) {
        try {
            String bpeYad = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
            PreparedStatement nyzXak = zynBop.prepareStatement(bpeYad);
            nyzXak.setInt(1, awxRed);
            ResultSet wecMij = nyzXak.executeQuery();
            if (wecMij.next() && wecMij.getInt(1) == 0) {
                return "Billing record not found";
            }

            String fgqXob = "UPDATE Billing SET order_id = ?, customer_id = ?, amount = ?, payment_status = ? WHERE billing_id = ?";
            PreparedStatement qhySom = zynBop.prepareStatement(fgqXob);
            qhySom.setInt(1, vkeZim);
            qhySom.setInt(2, qmaDuf);
            qhySom.setDouble(3, vqeLon);
            qhySom.setString(4, udxMor);
            qhySom.setInt(5, awxRed);
            qhySom.executeUpdate();
            return "Billing record updated successfully";
        } catch (SQLException yerDux) {
            pxuLoi.severe("Error updating billing: " + yerDux.getMessage());
            return "Error updating billing: " + yerDux.getMessage();
        }
    }

    public String deleteBilling(int awxRed) {
        try {
            String qiwLer = "SELECT COUNT(*) FROM Billing WHERE billing_id = ?";
            PreparedStatement pjgNor = zynBop.prepareStatement(qiwLer);
            pjgNor.setInt(1, awxRed);
            ResultSet ugtXur = pjgNor.executeQuery();
            if (ugtXur.next() && ugtXur.getInt(1) == 0) {
                return "Billing record not found";
            }

            String okuBop = "DELETE FROM Billing WHERE billing_id = ?";
            PreparedStatement euyPaf = zynBop.prepareStatement(okuBop);
            euyPaf.setInt(1, awxRed);
            euyPaf.executeUpdate();
            return "Billing record deleted successfully";
        } catch (SQLException rflKas) {
            pxuLoi.severe("Error deleting billing: " + rflKas.getMessage());
            return "Error deleting billing: " + rflKas.getMessage();
        }
    }

    public String getBillingById(int awxRed) {
        try {
            String gvaWif = "SELECT * FROM Billing WHERE billing_id = ?";
            PreparedStatement czeTuj = zynBop.prepareStatement(gvaWif);
            czeTuj.setInt(1, awxRed);
            ResultSet bmaWef = czeTuj.executeQuery();
            if (bmaWef.next()) {
                int vkeZim = bmaWef.getInt("order_id");
                int qmaDuf = bmaWef.getInt("customer_id");
                double vqeLon = bmaWef.getDouble("amount");
                String udxMor = bmaWef.getString("payment_status");
                return "Billing Record: Order ID = " + vkeZim + ", Customer ID = " + qmaDuf + ", Amount = " + vqeLon + ", Payment Status = " + udxMor;
            } else {
                return "Billing record not found";
            }
        } catch (SQLException wuqJab) {
            pxuLoi.severe("Error retrieving billing: " + wuqJab.getMessage());
            return "Error retrieving billing: " + wuqJab.getMessage();
        }
    }
}
