package Christian.auca.rw.component;

import Christian.auca.rw.swing.Button;
import Christian.auca.rw.swing.MyPasswordField;
import Christian.auca.rw.swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import Christian.auca.rw.model.User;
import Christian.auca.rw.dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;




/**
 *
 * @author B Christian
 */

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
    }

private void initRegister() {
    register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]10[]25[]push"));
    JLabel label = new JLabel("Create Account");
    label.setFont(new Font("Century Gothic", Font.BOLD, 40));
    label.setForeground(new Color(7, 164, 121));
    register.add(label);

    MyTextField txtUser = new MyTextField();
    txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/user.png")));
    txtUser.setHint("Name");
    txtUser.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(txtUser, "w 60%");

    MyTextField txtEmail = new MyTextField();
    txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/mail.png")));
    txtEmail.setHint("Email");
    txtEmail.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(txtEmail, "w 60%");

    MyTextField txtPhone = new MyTextField();
    txtPhone.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/phone.png")));
    txtPhone.setHint("Phone Number");
    txtPhone.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(txtPhone, "w 60%");

    MyPasswordField txtPass = new MyPasswordField();
    txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/pass.png")));
    txtPass.setHint("Password");
    txtPass.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(txtPass, "w 60%");

    MyTextField txtAddress = new MyTextField();
    txtAddress.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/address.png")));
    txtAddress.setHint("Address");
    txtAddress.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(txtAddress, "w 60%");

    JLabel genderLabel = new JLabel("Gender:");
    genderLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(genderLabel, "split 2, gapright 10");

    String[] genderOptions = { "Male", "Female" };
    JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
    genderComboBox.setFont(new Font("Century Gothic", Font.PLAIN, 20));
    register.add(genderComboBox, "wrap");




    Button cmd = new Button();
    cmd.setBackground(new Color(7, 164, 121));
    cmd.setForeground(new Color(250, 250, 250));
    cmd.setFont(new Font("Century Gothic", Font.BOLD, 20));
    cmd.setText("SIGN UP");
    register.add(cmd, "w 40%, h 40");
    
    
    
cmd.addActionListener(new ActionListener() {
    
    public void actionPerformed(ActionEvent e) {
        // Validate inputs
      String name = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPass.getPassword());
        String phoneNumber = txtPhone.getText().trim(); // Updated to txtPhone
        String address = txtAddress.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();

        // Perform validations
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long and contain letters, numbers, and symbols.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // All validations passed, insert data into the database
            User newUser = new User();
            newUser.setUsername(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setPhone_number(phoneNumber);
            newUser.setAddress(address);
             newUser.setGender(gender); // Set the gender

            // Call the method to insert the user into the database
            UserDao userDao = new UserDao();
            int rowsAffected = userDao.insert(newUser);
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});
}




// Validation methods
private boolean isValidEmail(String email) {
    // Implement email validation logic, return true if valid, false otherwise
    return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
}

private boolean isValidPhoneNumber(String phoneNumber) {
    // Implement phone number validation logic, return true if valid, false otherwise
    return phoneNumber.matches("\\d{10}");
}

private boolean isValidPassword(String password) {
    // Implement password validation logic, return true if valid, false otherwise
    return password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
}



   private void initLogin() {
    login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
    JLabel label = new JLabel("Sign In");
    label.setFont(new Font("Century Gothic", Font.BOLD, 40));  // Set font size to 20 and font family to Century Gothic
    label.setForeground(new Color(7, 164, 121));
    login.add(label);
    MyTextField txtEmail = new MyTextField();
    txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/mail.png")));
    txtEmail.setHint("Email");
    txtEmail.setFont(new Font("Century Gothic", Font.PLAIN, 20));  // Set font size to 20 and font family to Century Gothic
    login.add(txtEmail, "w 60%");
    MyPasswordField txtPass = new MyPasswordField();
    txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/Christian/auca/rw/icon/pass.png")));
    txtPass.setHint("Password");
    txtPass.setFont(new Font("Century Gothic", Font.PLAIN, 20));  // Set font size to 20 and font family to Century Gothic
    login.add(txtPass, "w 60%");
    JButton cmdForget = new JButton("Forgot your password ?");
    cmdForget.setForeground(new Color(100, 100, 100));
    cmdForget.setFont(new Font("Century Gothic", Font.PLAIN, 12));  // Set font size to 12 and font family to Century Gothic
    cmdForget.setContentAreaFilled(false);
    cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
    login.add(cmdForget);
    Button cmd = new Button();
    cmd.setBackground(new Color(7, 164, 121));
    cmd.setForeground(new Color(250, 250, 250));
    cmd.setFont(new Font("Century Gothic", Font.BOLD, 20));  // Set font size to 20 and font family to Century Gothic
    cmd.setText("SIGN IN");
    login.add(cmd, "w 40%, h 40");
}


    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));
        login.setPreferredSize(new java.awt.Dimension(640, 300));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));
        register.setPreferredSize(new java.awt.Dimension(640, 300));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
