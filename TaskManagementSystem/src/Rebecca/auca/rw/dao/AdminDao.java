package Christian.auca.rw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Christian.auca.rw.model.Admin;
import Christian.auca.rw.model.RoleEnum;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author B Christian
 */

public class AdminDao {
    // Global variable declaration
    private String jdbcUrl = "jdbc:postgresql://localhost:5432/task_management_system_db";
    private String dbUsername = "postgres";
    private String dbPasswd = "A$aprocky08";

    // Method to insert an admin record
public int insert(Admin admin) {
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        // Check if the email already exists
        if (emailExists(admin.getEmail(), con)) {
            return -1; // Return -1 to indicate that the email already exists
        }
        
        String sql = "INSERT INTO Admins (admin_name, admin_pass, role, email, phonenumber) VALUES (?, ?, ?::RoleEnum, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, admin.getAdmin_name());
            pst.setString(2, admin.getAdmin_pass());
            pst.setString(3, admin.getRole().toString().toUpperCase()); // Use uppercase for RoleEnum
            pst.setString(4, admin.getEmail()); // Set the email parameter
            pst.setString(5, admin.getPhoneNumber()); // Set the phone number parameter
            return pst.executeUpdate();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return 0; // Return 0 on failure
    }
}

private boolean emailExists(String email, Connection con) throws SQLException {
    String sql = "SELECT COUNT(*) AS count FROM Admins WHERE email = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int count = rs.getInt("count");
            return count > 0; // Return true if count is greater than 0, indicating that the email exists
        }
    }
    return false; // Return false if no email found
}



// Method to update an admin record
public int update(Admin admin) {
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        String sql = "UPDATE Admins SET admin_name = ?, admin_pass = ?, role = ?::RoleEnum, email = ?, phonenumber = ? WHERE admin_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, admin.getAdmin_name());
            pst.setString(2, admin.getAdmin_pass());
            pst.setString(3, admin.getRole().toString().toUpperCase()); // Use uppercase for RoleEnum
            pst.setString(4, admin.getEmail()); // Set the email parameter
            pst.setString(5, admin.getPhoneNumber()); // Set the phone number parameter
            pst.setInt(6, admin.getAdmin_id());
            return pst.executeUpdate();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return 0; // Return 0 on failure
    }
}



   // Method to delete an admin record by ID, excluding SUPERADMIN role
public int delete(int adminId) {
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        // Check if the admin to be deleted is not a SUPERADMIN
        Admin adminToDelete = getAdminById(adminId);
        if (adminToDelete != null && adminToDelete.getRole() != RoleEnum.SUPERADMIN) {
            String sql = "DELETE FROM Admins WHERE admin_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, adminId);
                return pst.executeUpdate();
            }
        } else if (adminToDelete != null && adminToDelete.getRole() == RoleEnum.SUPERADMIN) {
            return -1; // Return -1 if trying to delete a SUPERADMIN role
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return 0; // Return 0 if deletion fails or if the admin is not found
}


   // Method to retrieve all admin records
public List<Admin> getAllAdmins() {
    List<Admin> admins = new ArrayList<>();
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        String sql = "SELECT * FROM Admins";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setAdmin_id(rs.getInt("admin_id"));
                admin.setAdmin_name(rs.getString("admin_name"));
                admin.setAdmin_pass(rs.getString("admin_pass"));
                admin.setEmail(rs.getString("email")); // Set the email from the database
                admin.setPhoneNumber(rs.getString("phonenumber")); // Set the phone number from the database
                String roleString = rs.getString("role");
                admin.setRole(RoleEnum.valueOf(roleString));

                admins.add(admin);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return admins;
}

public void mygetAllAdmins(DefaultTableModel model) {
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        String sql = "SELECT admin_id,  admin_name, role, email, phonenumber FROM Admins";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            
            while (rs.next()) {
                Object[] data = new Object[cols];
                for(int i =0; i<cols; i++){
                    data[i]=rs.getObject(i+1);
                }
                model.addRow(data);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}



// Method to retrieve an admin record by ID
public Admin getAdminById(int adminId) {
    Admin admin = null;
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        String sql = "SELECT * FROM Admins WHERE admin_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, adminId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                admin = new Admin();
                admin.setAdmin_id(rs.getInt("admin_id"));
                admin.setAdmin_name(rs.getString("admin_name"));
                admin.setAdmin_pass(rs.getString("admin_pass"));
                String roleString = rs.getString("role");
                admin.setRole(RoleEnum.valueOf(roleString));
                admin.setEmail(rs.getString("email")); // Assuming email is also fetched from the database
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return admin; // Return null if admin not found or an Admin object if found
}





    
    public int getAdminCount() {
        int count = 0;
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
            String sql = "SELECT COUNT(*) FROM Admins";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1); // Get the count from the first column of the result set
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }
    
    
    // Method to log in an admin
public Admin loginAdmin(String email, String password) {
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd)) {
        String sql = "SELECT * FROM Admins WHERE email = ? AND admin_pass = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdmin_id(rs.getInt("admin_id"));
                admin.setAdmin_name(rs.getString("admin_name"));
                admin.setAdmin_pass(rs.getString("admin_pass"));
                String roleString = rs.getString("role");
                admin.setRole(RoleEnum.valueOf(roleString));

                return admin; // Return the admin if login is successful
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return null; // Return null if login fails
}

}


