package com.example.tasksystem.client.ui;

import com.example.tasksystem.client.util.RmiClientUtil;
import com.example.tasksystem.rmi.TaskService;
import com.example.tasksystem.shared.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainDashboardPage extends JFrame {

    private User loggedInUser;
    private JLabel welcomeLabel;
    private JPanel contentPanel; // Panel to hold different views like ViewTasksPanel

    public MainDashboardPage(User user) {
        super("Main Dashboard - Task Management System");
        this.loggedInUser = user;

        initComponents();
        layoutComponents();
        attachEventHandlers();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle close explicitly for logout/cleanup
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleLogout();
            }
        });
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
    }

    private void initComponents() {
        if (loggedInUser.getFullName() != null && !loggedInUser.getFullName().isEmpty()) {
            welcomeLabel = new JLabel("Welcome, " + loggedInUser.getFullName() + "!", SwingConstants.CENTER);
        } else {
            welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!", SwingConstants.CENTER);
        }
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem manageTasksItem = new JMenuItem("Manage Tasks");
        manageTasksItem.setActionCommand("manageTasks");
        manageTasksItem.setMnemonic('T');
        JMenuItem manageProjectsItem = new JMenuItem("Manage Projects");
        manageProjectsItem.setActionCommand("manageProjects");
        manageProjectsItem.setMnemonic('P');
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setActionCommand("logout");
        logoutItem.setMnemonic('L');

        fileMenu.add(manageTasksItem);
        fileMenu.add(manageProjectsItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Action Listener for menu items
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch (command) {
                    case "manageTasks":
                        handleManageTasks();
                        break;
                    case "manageProjects":
                        handleManageProjects();
                        break;
                    case "logout":
                        handleLogout();
                        break;
                }
            }
        };

        manageTasksItem.addActionListener(menuListener);
        manageProjectsItem.addActionListener(menuListener);
        logoutItem.addActionListener(menuListener);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel for dynamic content
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        // Initial view can be empty or a welcome message in contentPanel
        JLabel initialMessage = new JLabel("Select an option from the File menu to begin.", SwingConstants.CENTER);
        initialMessage.setFont(new Font("Arial", Font.ITALIC, 16));
        contentPanel.add(initialMessage, BorderLayout.CENTER);
    }

    private void attachEventHandlers() {
        // Event handlers are attached directly in initComponents for menu items
    }

    private void handleManageTasks() {
        try {
            TaskService taskService = RmiClientUtil.getTaskService();
            if (taskService == null) {
                JOptionPane.showMessageDialog(this, "Server connection not available. Cannot load tasks.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ViewTasksPanel viewTasksPanel = new ViewTasksPanel(taskService, loggedInUser);
            switchContent(viewTasksPanel);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Error accessing RMI service: " + e.getMessage(), "RMI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleManageProjects() {
        JOptionPane.showMessageDialog(this, "Manage Projects Clicked - Functionality to be implemented.", "Manage Projects", JOptionPane.INFORMATION_MESSAGE);
        // Example: new ProjectManagementPanel(loggedInUser).setVisible(true);
    }

    private void handleLogout() {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            CurrentUserSession.clearSession();
            this.dispose(); // Close the dashboard

            // Open new LoginPage
            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            });
        }
    }

    // Main method for testing this page directly (optional)
    public static void main(String[] args) {
        // Create a dummy user for testing
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User FullName");

        SwingUtilities.invokeLater(() -> {
            MainDashboardPage dashboardPage = new MainDashboardPage(testUser);
            dashboardPage.setVisible(true);
        });
    }
}
