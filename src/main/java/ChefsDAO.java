 
import java.sql.*;

public class ChefsDAO {
    private static final String URL = "jdbc:mysql://localhost:3308/SpecialCookDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void addChef(String nm, String spc) {
        String qry = "INSERT INTO Chefs (name, specialization) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(qry)) {
            ps.setString(1, nm);
            ps.setString(2, spc);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewChefs() {
        String qry = "SELECT * FROM Chefs";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(qry)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("chef_id") + ", Name: " + rs.getString("name") +
                        ", Specialization: " + rs.getString("specialization"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChef(int id, String nm, String spc) {
        String qry = "UPDATE Chefs SET name=?, specialization=? WHERE chef_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(qry)) {
            ps.setString(1, nm);
            ps.setString(2, spc);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteChef(int id) {
        String qry = "DELETE FROM Chefs WHERE chef_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(qry)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
