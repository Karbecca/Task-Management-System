package com.example.tasksystem.shared.entities; // Changed package

import jakarta.persistence.*; // Kept for structural similarity
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
// User and Task will be in this same 'shared.entities' package.

@Entity
@Table(name = "projects")
public class Project implements Serializable {

    private static final long serialVersionUID = 2L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String projectName;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status; // e.g., "Open", "In Progress", "Completed"

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;

    @ManyToMany
    @JoinTable(
        name = "project_members", // This detail is for server-side JPA
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    // Default constructor
    public Project() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }
}
