package com.example.tasksystem.shared.model; // Updated package

import javax.persistence.*; // Kept
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
// Imports for Admin and TaskAssignment will be from this package

@Entity // Kept
@Table(name = "task") // Kept
public class Task implements Serializable {

    private static final long serialVersionUID = 203L; 

    @Id // Kept
    @Column(name = "task_id") // Kept
    private UUID taskId;

    @Column(nullable = false, length = 200) // Kept
    private String title;

    @Column(columnDefinition = "TEXT") // Kept
    private String description;

    @Column(length = 100) // Kept
    private String category;

    @Column(length = 50) // Kept
    private String priority;

    @Column(length = 50) // Kept
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false) // Kept
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY) // Kept
    @JoinColumn(name = "created_by", nullable = false) // Kept
    private Admin createdByAdmin;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // Kept
    private Set<TaskAssignment> assignments = new HashSet<>();

    // Default constructor
    public Task() {
        // For DTOs, explicit @PrePersist is not invoked automatically by client.
        // If client needs to create a new Task DTO with these set, it should do so explicitly.
        // Server-side @PrePersist will handle it for entities saved to DB.
    }

    // No @PrePersist for DTO, these are set by server or when creating DTO from server data

    // Getters and Setters
    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Admin getCreatedByAdmin() {
        return createdByAdmin;
    }

    public void setCreatedByAdmin(Admin createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    public Set<TaskAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<TaskAssignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId != null ? taskId.equals(task.taskId) : task.taskId == null;
    }

    @Override
    public int hashCode() {
        return taskId != null ? taskId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return title != null ? title : "Task ID: " + taskId;
        // Or more detailed:
        // return "Task{" +
        //        "taskId=" + taskId +
        //        ", title='" + title + '\'' +
        //        ", status='" + status + '\'' +
        //        '}';
    }
}
