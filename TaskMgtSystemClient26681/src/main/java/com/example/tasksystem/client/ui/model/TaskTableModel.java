package com.example.tasksystem.client.ui.model;

import com.example.tasksystem.shared.entities.Task;
import com.example.tasksystem.shared.entities.User;
import com.example.tasksystem.shared.entities.Project;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {

    private final List<Task> tasks;
    private final String[] columnNames = {
            "ID", "Title", "Status", "Priority", "Due Date", "Assignee", "Project"
    };
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public TaskTableModel(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void setTasks(List<Task> newTasks) {
        this.tasks.clear();
        this.tasks.addAll(newTasks);
        fireTableDataChanged();
    }

    public Task getTaskAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tasks.size()) {
            return tasks.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return task.getId();
            case 1:
                return task.getTaskTitle();
            case 2:
                return task.getStatus();
            case 3:
                return task.getPriority();
            case 4:
                return task.getDueDate() != null ? task.getDueDate().format(DATE_FORMATTER) : "N/A";
            case 5:
                User assignee = task.getAssignee();
                return assignee != null ? assignee.toString() : "Unassigned"; // Uses User.toString()
            case 6:
                Project project = task.getProject();
                return project != null ? project.toString() : "N/A"; // Uses Project.toString()
            default:
                return null;
        }
    }

    // Optional: To make cells non-editable by default
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
