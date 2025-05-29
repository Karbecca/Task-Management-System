package com.example.tasksystem.shared.model; // Updated package

import javax.persistence.*; // Kept
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
// Imports for Task, AppUser, Admin will be from this package

@Entity // Kept
@Table(name = "task_assignment") // Kept
public class TaskAssignment implements Serializable {

    private static final long serialVersionUID = 204L; 

    @Id // Kept
    @Column(name = "assignment_id") // Kept
    private UUID assignmentId;

    @Column(name = "assigned_at", nullable = false, updatable = false) // Kept
    private LocalDateTime assignedAt;

    @Column(name = "deadline") // Kept
    private LocalDate deadline;

    @Column(name = "completed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE") // Kept
    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY) // Kept
    @JoinColumn(name = "task_id", nullable = false) // Kept
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY) // Kept
    @JoinColumn(name = "assigned_to", nullable = false) // Kept
    private AppUser assignedAppUser;

    @ManyToOne(fetch = FetchType.LAZY) // Kept
    @JoinColumn(name = "assigned_by", nullable = false) // Kept
    private Admin assignedByAdmin;

    // Default constructor
    public TaskAssignment() {
        // For DTOs, explicit @PrePersist is not invoked.
    }
    
    // No @PrePersist for DTO

    // Getters and Setters
    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public AppUser getAssignedAppUser() {
        return assignedAppUser;
    }

    public void setAssignedAppUser(AppUser assignedAppUser) {
        this.assignedAppUser = assignedAppUser;
    }

    public Admin getAssignedByAdmin() {
        return assignedByAdmin;
    }

    public void setAssignedByAdmin(Admin assignedByAdmin) {
        this.assignedByAdmin = assignedByAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskAssignment that = (TaskAssignment) o;
        return assignmentId != null ? assignmentId.equals(that.assignmentId) : that.assignmentId == null;
    }

    @Override
    public int hashCode() {
        return assignmentId != null ? assignmentId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TaskAssignment{" +
               "assignmentId=" + assignmentId +
               ", taskId=" + (task != null ? task.getTaskId() : "null") +
               ", assignedToUserId=" + (assignedAppUser != null ? assignedAppUser.getUserId() : "null") +
               ", completed=" + completed +
               '}';
    }
}
