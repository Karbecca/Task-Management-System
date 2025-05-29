package com.example.tasksystem.shared.entities; // Changed package

import jakarta.persistence.*; // This will be unused on client, but kept for simplicity of copying
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
// Assuming Task and Project will also be in com.example.tasksystem.shared.entities
// No need for specific imports if they are in the same package.

@Entity // JPA annotations might be irrelevant on client unless client also uses JPA with these
@Table(name = "users") // Same as above
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    private String otp; // Should this be transient for client? For now, keeping it.

    private LocalDateTime otpTimestamp; // Same as above

    // For RMI, client might not need full object graphs unless specifically requested.
    // These will be null if not populated by server for RMI calls.
    // The client-side Task/Project classes will be in this same 'shared.entities' package.
    @OneToMany(mappedBy = "assignee")
    private Set<Task> tasks;

    @ManyToMany(mappedBy = "members")
    private Set<Project> projects;

    // Default constructor
    public User() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpTimestamp() {
        return otpTimestamp;
    }

    public void setOtpTimestamp(LocalDateTime otpTimestamp) {
        this.otpTimestamp = otpTimestamp;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
