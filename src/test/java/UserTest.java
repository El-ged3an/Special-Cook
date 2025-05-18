import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

class UserTest {

    @Test
    void testAddUser() {
        User u = new User(-1, "dummy", "dummy", "role");
        String result = User.addUser(u);
        assertNotNull(result);
    }

    @Test
    void testUpdateUser() {
        User u = new User(-1, "dummy", "dummy", "role");
        String result = User.updateUser(u);
        assertNotNull(result);
    }

    @Test
    void testDeleteUser() {
        String result = User.deleteUser(-1);
        assertNotNull(result);
    }

    @Test
    void testGetUserById() {
        User u = User.getUserById(-1);
        // Can be null or user, just ensure no exception thrown and callable
        assertTrue(u == null || u instanceof User);
    }

    @Test
    void testPasswordSetterGetter() {
        User u = new User(0, "x", "p", "r");
        u.setPassword("newpass");
        assertEquals("newpass", u.getPassword());
    }
}
