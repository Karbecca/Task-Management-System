package com.example.tasksystem.client.ui;

import com.example.tasksystem.client.ui.model.TaskTableModel;
import com.example.tasksystem.rmi.TaskService;
import com.example.tasksystem.shared.entities.Task;
import com.example.tasksystem.shared.entities.User;
// PDF Export
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
// Excel Export
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewTasksPanel extends JPanel {

    private TaskService taskService;
    private User loggedInUser;
    private TaskTableModel taskTableModel;
    private JTable taskTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addTaskButton;
    private JButton editTaskButton;
    private JButton deleteTaskButton;
    private JButton exportPdfButton;
    private JButton exportExcelButton;

    private List<Task> currentDisplayedTasks; // To store the list currently in the table (filtered or all)


    public ViewTasksPanel(TaskService taskService, User loggedInUser) {
        this.taskService = taskService;
        this.loggedInUser = loggedInUser;
        // this.allFetchedTasks = new ArrayList<>(); // Replaced by currentDisplayedTasks
        this.currentDisplayedTasks = new ArrayList<>();


        initComponents();
        layoutComponents();
        attachEventHandlers();

        // Initial load of tasks
        loadTasks();
    }

    private void initComponents() {
        taskTableModel = new TaskTableModel(new ArrayList<>());
        taskTable = new JTable(taskTableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setAutoCreateRowSorter(true); // Enable sorting

        searchField = new JTextField(25);
        searchButton = new JButton("Search");
        searchButton.setMnemonic('S');
        refreshButton = new JButton("Refresh");
        refreshButton.setMnemonic('R');
        addTaskButton = new JButton("Add Task");
        addTaskButton.setMnemonic('A');
        editTaskButton = new JButton("Edit Task");
        editTaskButton.setMnemonic('E');
        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setMnemonic('D');
        exportPdfButton = new JButton("Export to PDF");
        exportPdfButton.setMnemonic('P');
        exportExcelButton = new JButton("Export to Excel");
        exportExcelButton.setMnemonic('X');
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10)); // Add some gaps

        // Top panel for search, refresh, and exports
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search Task:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(exportPdfButton);
        topPanel.add(exportExcelButton);

        // Table in the center
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Bottom panel for action buttons (Add, Edit, Delete)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addTaskButton);
        bottomPanel.add(editTaskButton);
        bottomPanel.add(deleteTaskButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void loadTasks() {
        try {
            if (taskService == null) {
                JOptionPane.showMessageDialog(this, "Task service is not available.", "Error", JOptionPane.ERROR_MESSAGE);
                currentDisplayedTasks = new ArrayList<>(); // Ensure it's not null
                taskTableModel.setTasks(currentDisplayedTasks);
                return;
            }
            currentDisplayedTasks = taskService.getAllTasks(); // Or any other default load
            taskTableModel.setTasks(currentDisplayedTasks);
            searchField.setText(""); 
        } catch (RemoteException e) {
            currentDisplayedTasks = new ArrayList<>();
            taskTableModel.setTasks(currentDisplayedTasks);
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage(), "RMI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void attachEventHandlers() {
        refreshButton.addActionListener(e -> loadTasks());
        searchButton.addActionListener(e -> handleSearch());
        exportPdfButton.addActionListener(e -> handleExportToPdf());
        exportExcelButton.addActionListener(e -> handleExportToExcel());

        // Add Task
        addTaskButton.addActionListener(e -> {
            TaskEditorDialog addTaskDialog = new TaskEditorDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    "Add New Task",
                    true,
                    taskService,
                    loggedInUser,
                    null // No task to edit, so it's an add operation
            );
            addTaskDialog.setVisible(true);
            if (addTaskDialog.isSaveSuccess()) {
                loadTasks(); // Refresh table if a task was added
            }
        });

        // Edit Task
        editTaskButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Task taskToEdit = taskTableModel.getTaskAt(selectedRow);
            if (taskToEdit == null) {
                JOptionPane.showMessageDialog(this, "Could not retrieve selected task details.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Need to pass the actual TaskService object
            TaskEditorDialog editTaskDialog = new TaskEditorDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    "Edit Task - ID: " + taskToEdit.getId(),
                    true,
                    taskService,
                    loggedInUser,
                    taskToEdit
            );
            editTaskDialog.setVisible(true);
            if (editTaskDialog.isSaveSuccess()) {
                loadTasks(); // Refresh table if a task was edited
            }
        });

        // Delete Task
        deleteTaskButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Task taskToDelete = taskTableModel.getTaskAt(selectedRow);
             if (taskToDelete == null) {
                JOptionPane.showMessageDialog(this, "Could not retrieve selected task details for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete task: '" + taskToDelete.getTaskTitle() + "' (ID: " + taskToDelete.getId() + ")?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    taskService.deleteTask(taskToDelete.getId());
                    JOptionPane.showMessageDialog(this, "Task deleted successfully.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTasks(); // Refresh table
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting task: " + ex.getMessage(), "RMI Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void handleSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            taskTableModel.setTasks(currentDisplayedTasks); // Show all if search is empty
            return;
        }
        // Perform search on the 'currentDisplayedTasks' if you want to filter the current view,
        // or 'allFetchedTasks' if you want to search from the master list loaded at refresh.
        // For simplicity, let's assume currentDisplayedTasks holds the full list unless filtered.
        // If you always want to search the full original list, use 'allFetchedTasks' (after ensuring it's populated).
        // For now, let's assume loadTasks() populates currentDisplayedTasks with the full list.
        List<Task> filteredTasks = currentDisplayedTasks.stream()
            .filter(task ->
                (task.getTaskTitle() != null && task.getTaskTitle().toLowerCase().contains(searchTerm)) ||
                (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchTerm)) ||
                (task.getStatus() != null && task.getStatus().toLowerCase().contains(searchTerm)) ||
                (task.getPriority() != null && task.getPriority().toLowerCase().contains(searchTerm)) ||
                (task.getAssignee() != null && task.getAssignee().toString().toLowerCase().contains(searchTerm)) ||
                (task.getProject() != null && task.getProject().toString().toLowerCase().contains(searchTerm))
            )
            .collect(Collectors.toList());
        taskTableModel.setTasks(filteredTasks); // Update table with filtered results
    }

    private void handleExportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Tasks to Excel");
        fileChooser.setSelectedFile(new File("tasks.xlsx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xlsx)", "xlsx"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure extension
            if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".xlsx");
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Tasks");

            // Header Row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < taskTableModel.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(taskTableModel.getColumnName(i));
            }

            // Data Rows
            for (int i = 0; i < taskTableModel.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < taskTableModel.getColumnCount(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = taskTableModel.getValueAt(i, j);
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }

            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                workbook.write(fos);
                JOptionPane.showMessageDialog(this, "Tasks exported to Excel successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks to Excel: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private void handleExportToPdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Tasks to PDF");
        fileChooser.setSelectedFile(new File("tasks.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Document (*.pdf)", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
             if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
            }

            Document document = new Document(PageSize.A4.rotate()); // Landscape for more columns
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                PdfWriter.getInstance(document, fos);
                document.open();

                document.add(new Paragraph("Task List Report"));
                document.add(new Paragraph(" ")); // Empty line

                PdfPTable pdfTable = new PdfPTable(taskTableModel.getColumnCount());
                pdfTable.setWidthPercentage(100);

                // Header Cells
                for (int i = 0; i < taskTableModel.getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(taskTableModel.getColumnName(i)));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(Color.LIGHT_GRAY);
                    pdfTable.addCell(headerCell);
                }

                // Data Cells
                for (int i = 0; i < taskTableModel.getRowCount(); i++) {
                    for (int j = 0; j < taskTableModel.getColumnCount(); j++) {
                        Object value = taskTableModel.getValueAt(i, j);
                        pdfTable.addCell(value != null ? value.toString() : "");
                    }
                }
                document.add(pdfTable);
                JOptionPane.showMessageDialog(this, "Tasks exported to PDF successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);

            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks to PDF: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }
        }
    }
}
