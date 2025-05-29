package com.example.tasksystem.client.ui;

import com.example.tasksystem.rmi.TaskService;
import com.example.tasksystem.shared.entities.Project;
import com.example.tasksystem.shared.entities.Task;
import com.example.tasksystem.shared.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Vector;

public class TaskEditorDialog extends JDialog {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField; // Example: YYYY-MM-DD
    private JComboBox<String> priorityComboBox;
    private JComboBox<String> statusComboBox;
    private JComboBox<User> assigneeComboBox;
    private JComboBox<Project> projectComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    private TaskService taskService;
    private User loggedInUser;
    private Task taskToEdit;
    private boolean saveSuccess = false;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    public TaskEditorDialog(Frame owner, String title, boolean modal,
                            TaskService taskService, User loggedInUser, Task taskToEdit) {
        super(owner, title, modal);
        this.taskService = taskService;
        this.loggedInUser = loggedInUser;
        this.taskToEdit = taskToEdit;

        initComponents();
        layoutComponents();
        populateComboBoxes();
        populateFieldsForEdit();
        attachEventHandlers();

        pack();
        setLocationRelativeTo(owner);
        // setSize(500, 400); // Or use pack() and then adjust if needed
    }

    private void initComponents() {
        titleField = new JTextField(30);
        descriptionArea = new JTextArea(5, 30);
        dueDateField = new JTextField(10); // For "YYYY-MM-DD"

        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        statusComboBox = new JComboBox<>(new String[]{"To Do", "In Progress", "Done"});

        assigneeComboBox = new JComboBox<>(); // Will be populated from DB
        projectComboBox = new JComboBox<>();  // Will be populated from DB

        saveButton = new JButton("Save");
        saveButton.setMnemonic('S');
        cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('C');
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST; add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        add(new JScrollPane(descriptionArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0.0; // Reset

        // Due Date
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; add(new JLabel("Due Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; add(dueDateField, gbc);

        // Priority
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; add(priorityComboBox, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2; add(statusComboBox, gbc);

        // Assignee
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Assignee:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; add(assigneeComboBox, gbc);

        // Project
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Project:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2; add(projectComboBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);
    }

    private void populateComboBoxes() {
        try {
            if (taskService == null) {
                 JOptionPane.showMessageDialog(this, "Task service not available. Cannot load users/projects.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // Populate Assignee ComboBox
            List<User> users = taskService.getAllUsers();
            assigneeComboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(users)));

            // Populate Project ComboBox
            List<Project> projects = taskService.getAllProjects();
            projectComboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(projects)));

        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error loading users or projects: " + e.getMessage(), "RMI Error", JOptionPane.ERROR_MESSAGE);
            // Disable save if essential data can't be loaded? Or allow save with nulls if appropriate.
            saveButton.setEnabled(false);
        }
    }

    private void populateFieldsForEdit() {
        if (taskToEdit != null) {
            titleField.setText(taskToEdit.getTaskTitle());
            descriptionArea.setText(taskToEdit.getDescription());
            if (taskToEdit.getDueDate() != null) {
                dueDateField.setText(taskToEdit.getDueDate().format(DATE_FORMATTER));
            }
            priorityComboBox.setSelectedItem(taskToEdit.getPriority());
            statusComboBox.setSelectedItem(taskToEdit.getStatus());

            // Select assignee in ComboBox
            if (taskToEdit.getAssignee() != null) {
                for (int i = 0; i < assigneeComboBox.getModel().getSize(); i++) {
                    if (assigneeComboBox.getModel().getElementAt(i).getId().equals(taskToEdit.getAssignee().getId())) {
                        assigneeComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                assigneeComboBox.setSelectedItem(null); // Or set to a "None" user if you add one
            }


            // Select project in ComboBox
            if (taskToEdit.getProject() != null) {
                 for (int i = 0; i < projectComboBox.getModel().getSize(); i++) {
                    if (projectComboBox.getModel().getElementAt(i).getId().equals(taskToEdit.getProject().getId())) {
                        projectComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } else {
            // Set defaults for new task, e.g., loggedInUser as assignee if applicable
            if (loggedInUser != null) {
                 for (int i = 0; i < assigneeComboBox.getModel().getSize(); i++) {
                    if (assigneeComboBox.getModel().getElementAt(i).getId().equals(loggedInUser.getId())) {
                        assigneeComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
    
    public boolean isSaveSuccess() {
        return saveSuccess;
    }

    private void attachEventHandlers() {
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleSave() {
        // 1. Validation
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task Title must not be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dueDateStr = dueDateField.getText().trim();
        LocalDate dueDate = null;
        if (!dueDateStr.isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Due Date format. Please use YYYY-MM-DD.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (priorityComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Priority must be selected.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String priority = (String) priorityComboBox.getSelectedItem();

        if (statusComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Status must be selected.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String status = (String) statusComboBox.getSelectedItem();
        
        User selectedAssignee = (User) assigneeComboBox.getSelectedItem();
        // Optional Enhancement: Enforce Assignee selection
        if (selectedAssignee == null) {
            JOptionPane.showMessageDialog(this, "Assignee must be selected.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Project selectedProject = (Project) projectComboBox.getSelectedItem();
        if (selectedProject == null) {
            JOptionPane.showMessageDialog(this, "Project must be selected.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Collect Data & Create/Update Task Object
        Task task = (taskToEdit == null) ? new Task() : taskToEdit;
        task.setTaskTitle(title);
        task.setDescription(descriptionArea.getText().trim());
        task.setDueDate(dueDate);
        task.setPriority(priority);
        task.setStatus(status);
        task.setAssignee(selectedAssignee); // Can be null if not selected and allowed
        task.setProject(selectedProject); // Project is mandatory

        // 3. RMI Call
        try {
            if (taskService == null) {
                 JOptionPane.showMessageDialog(this, "Task service not available. Cannot save task.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            if (taskToEdit == null) {
                taskService.createTask(task);
                JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                taskService.updateTask(task);
                JOptionPane.showMessageDialog(this, "Task updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            saveSuccess = true;
            dispose(); // Close dialog on successful save
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error saving task: " + ex.getMessage(), "RMI Error", JOptionPane.ERROR_MESSAGE);
            // ex.printStackTrace();
        } catch (Exception ex) { // Catch other potential runtime exceptions from server logic if not wrapped in RemoteException
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // ex.printStackTrace();
        }
    }
}
