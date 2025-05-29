package com.example.tasksystem.shared.entities; // Changed package

import jakarta.persistence.*; // Kept for structural similarity
import java.io.Serializable;
import java.time.LocalDate;
// User and Project will be in this same 'shared.entities' package.

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String taskTitle;

    private String description;

    private LocalDate dueDate;

    private String priority; // e.g., "High", "Medium", "Low"

    private String status; // e.g., "To Do", "In Progress", "Done"

    @ManyToOne
    @JoinColumn(name = "assignee_id") // Server-side JPA detail
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) // Server-side JPA detail
    private Project project;

    // Default constructor
    public Task() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
