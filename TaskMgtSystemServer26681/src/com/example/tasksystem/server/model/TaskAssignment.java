package com.example.tasksystem.server.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task_assignment")
public class TaskAssignment implements Serializable {

    private static final long serialVersionUID = 204L; // Unique ID for TaskAssignment

    @Id
    @Column(name = "assignment_id")
    private UUID assignmentId; // Application will generate this

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "completed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean completed = false; // Default value

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false) // Assuming assigned_to references app_user's user_id
    private AppUser assignedAppUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false) // Assuming assigned_by references admin's admin_id
    private Admin assignedByAdmin;

    // Default constructor
    public TaskAssignment() {
        // UUID and assignedAt are set by @PrePersist
    }
    
    @PrePersist
    public void onPrePersist() {
        if (this.assignmentId == null) {
            this.assignmentId = UUID.randomUUID();
        }
        if (this.assignedAt == null) {
            this.assignedAt = LocalDateTime.now();
        }
    }

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
