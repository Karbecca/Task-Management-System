package Christian.auca.rw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Christian.auca.rw.model.User;

/**
 *
 * @author B Christian
 */
public class UserDao {
    // Global variable declaration
    private String jdbcUrl = "jdbc:postgresql://localhost:5432/task_management_system_db";
    private String dbUsername = "postgres";
    private String dbPasswd = "A$aprocky08";

    // Method to insert a user record
    public int insert(User user) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "INSERT INTO Users (username, email, phone_number, password, address, gender) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, user.getUsername());
                pst.setString(2, user.getEmail());
                pst.setString(3, user.getPhone_number());
                pst.setString(4, user.getPassword());
                pst.setString(5, user.getAddress());
                pst.setString(6, user.getGender());
                return pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0; // Return 0 on failure
        }
    }

    // Method to update a user record
    public int update(User user) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "UPDATE Users SET username = ?, email = ?, phone_number = ?, password = ?, address = ?, gender = ? WHERE user_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, user.getUsername());
                pst.setString(2, user.getEmail());
                pst.setString(3, user.getPhone_number());
                pst.setString(4, user.getPassword());
                pst.setString(5, user.getAddress());
                pst.setString(6, user.getGender());
                pst.setInt(7, user.getUser_id());
                return pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0; // Return 0 on failure
        }
    }

    // Method to delete a user record by ID
    public int delete(int userId) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "DELETE FROM Users WHERE user_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, userId);
                return pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0; // Return 0 on failure
        }
    }

    // Method to retrieve all user records
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "SELECT * FROM Users";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone_number(rs.getString("phone_number"));
                    user.setPassword(rs.getString("password"));
                    user.setAddress(rs.getString("address"));
                    user.setGender(rs.getString("gender"));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    // Method to retrieve a user record by ID
    public User getUserById(int userId) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "SELECT * FROM Users WHERE user_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone_number(rs.getString("phone_number"));
                    user.setPassword(rs.getString("password"));
                    user.setAddress(rs.getString("address"));
                    user.setGender(rs.getString("gender"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if user not found
    }

    // Method to retrieve a user record by email (for login)
    public User getUserByEmail(String email) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "SELECT * FROM Users WHERE email = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, email);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone_number(rs.getString("phone_number"));
                    user.setPassword(rs.getString("password"));
                    user.setAddress(rs.getString("address"));
                    user.setGender(rs.getString("gender"));
                return user;
                    }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
            return null; // Return null if user not found
    }
}
