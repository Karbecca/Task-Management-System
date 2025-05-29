package com.example.tasksystem.client.ui;

import com.example.tasksystem.client.util.RmiClientUtil;
import com.example.tasksystem.rmi.TaskService;
import com.example.tasksystem.shared.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField otpField;
    private JButton requestOtpButton;
    private JButton loginButton;
    private JLabel statusLabel; // For showing brief status messages

    private TaskService taskService;

    public LoginPage() {
        super("Login - Task Management System");
        try {
            this.taskService = RmiClientUtil.getTaskService();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Failed to connect to server: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            // Optionally, disable UI components or exit if server connection is critical at this stage
            // For now, we'll let the user try, and actions will fail.
        }


        initComponents();
        layoutComponents();
        attachEventHandlers();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 250); // Adjusted size
        setLocationRelativeTo(null); // Center on screen
    }

    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        otpField = new JTextField(10);
        requestOtpButton = new JButton("Request OTP");
        requestOtpButton.setMnemonic('R');
        loginButton = new JButton("Login");
        loginButton.setMnemonic('L');
        statusLabel = new JLabel("Please enter your credentials.", SwingConstants.CENTER);
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username/Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username/Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(passwordField, gbc);

        // OTP
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("OTP:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // OTP field smaller
        add(otpField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Button next to OTP field
        add(requestOtpButton, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3; // Span across all columns
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Don't stretch button
        add(loginButton, gbc);
        
        // Status Label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, gbc);
    }

    private void attachEventHandlers() {
        requestOtpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRequestOtp();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleRequestOtp() {
        String emailOrUsername = usernameField.getText().trim();
        if (emailOrUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Username/Email to request OTP.", "Input Required", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("Username/Email required for OTP.");
            return;
        }

        if (taskService == null) {
             JOptionPane.showMessageDialog(this, "Cannot request OTP: Server not connected.", "Error", JOptionPane.ERROR_MESSAGE);
             statusLabel.setText("Server connection error.");
             return;
        }

        try {
            statusLabel.setText("Requesting OTP...");
            String otp = taskService.generateOtp(emailOrUsername);
            // In a real app, OTP is sent via email/SMS. For this demo, we show it.
            JOptionPane.showMessageDialog(this, "OTP generated (for demo): " + otp + "\nPlease enter it in the OTP field.", "OTP Generated", JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("OTP sent/displayed. Check OTP field.");
            otpField.setText(otp); // Populate OTP field for convenience in demo
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error requesting OTP: " + ex.getMessage(), "OTP Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("OTP request failed: " + ex.getMessage());
            // ex.printStackTrace();
        }
    }

    private void handleLogin() {
        String emailOrUsername = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String otp = otpField.getText().trim();

        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username/Email and Password cannot be empty.", "Input Required", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("Username/Email and Password required.");
            return;
        }
        
        if (otp.isEmpty()) {
            // Depending on policy, OTP might be optional if not recently requested or if feature is off
            // For now, let's make it "required" if an OTP was generated for this session (though we don't track session here yet)
            // Or simply, if OTP field is empty, try login without OTP.
            // For this demo, let's assume OTP is required if field has content, or if some flag was set.
            // The current TaskService implies OTP is verified after login.
            // For a simpler flow for now: if OTP field is empty, don't try to verify it.
        }

        if (taskService == null) {
             JOptionPane.showMessageDialog(this, "Cannot login: Server not connected.", "Error", JOptionPane.ERROR_MESSAGE);
             statusLabel.setText("Server connection error.");
             return;
        }

        try {
            statusLabel.setText("Logging in...");
            User user = taskService.login(emailOrUsername, password);
            
            if (user != null) {
                // If OTP field is not empty, verify it.
                if (!otp.isEmpty()) {
                    statusLabel.setText("Verifying OTP...");
                    boolean otpVerified = taskService.verifyOtp(emailOrUsername, otp); // Assuming username/email is used for OTP user lookup
                    if (otpVerified) {
                        CurrentUserSession.setLoggedInUser(user);
                        JOptionPane.showMessageDialog(this, "Login and OTP verification successful!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                        statusLabel.setText("Login successful. Welcome " + user.getUsername() + "!");
                        // Proceed to the main application window
                        MainDashboardPage dashboard = new MainDashboardPage(user);
                        dashboard.setVisible(true);
                        this.dispose(); // Close login window
                    } else {
                        // This path might not be reached if verifyOtp throws exception on failure
                        JOptionPane.showMessageDialog(this, "OTP verification failed.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("OTP verification failed.");
                    }
                } else {
                     // Login without OTP verification (if OTP field was empty)
                    CurrentUserSession.setLoggedInUser(user);
                    JOptionPane.showMessageDialog(this, "Login successful (OTP not provided/verified)!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Login successful (no OTP). Welcome " + user.getUsername() + "!");
                    // Proceed to the main application window
                    MainDashboardPage dashboard = new MainDashboardPage(user);
                    dashboard.setVisible(true);
                    this.dispose(); // Close login window
                }
            } else {
                // Should not happen if login throws exception on failure, but as a fallback:
                JOptionPane.showMessageDialog(this, "Login failed. Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Login failed: Invalid credentials.");
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Login Error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Login error: " + ex.getMessage());
            // ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}
