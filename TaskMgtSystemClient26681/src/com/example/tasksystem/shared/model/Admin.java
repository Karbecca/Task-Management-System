package com.example.tasksystem.shared.model; // Updated package

import javax.persistence.*; // Kept for structural consistency, non-functional on client DTO
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
// Imports for Task and TaskAssignment will now point to com.example.tasksystem.shared.model

@Entity // Kept
@Table(name = "admin") // Kept
public class Admin implements Serializable {

    private static final long serialVersionUID = 201L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Kept
    @Column(name = "admin_id") // Kept
    private Integer adminId;

    @Column(name = "admin_name", nullable = false, length = 100) // Kept
    private String adminName;

    @Column(name = "admin_pass", nullable = false, length = 100) // Kept
    private String adminPass;

    @Enumerated(EnumType.STRING) // Kept
    @Column(nullable = false) // Kept
    private RoleEnum role;

    @Column(unique = true, nullable = false, length = 150) // Kept
    private String email;

    @Column(name = "phone_number", length = 20) // Kept
    private String phoneNumber;

    @OneToMany(mappedBy = "createdByAdmin", // Kept
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, // Kept, less relevant for DTO
               fetch = FetchType.LAZY) // Kept
    private Set<Task> tasksCreated = new HashSet<>();

    @OneToMany(mappedBy = "assignedByAdmin", // Kept
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, // Kept
               fetch = FetchType.LAZY) // Kept
    private Set<TaskAssignment> tasksAssignedBy = new HashSet<>();

    // Default constructor
    public Admin() {
    }

    // Getters and Setters
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Task> getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(Set<Task> tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public Set<TaskAssignment> getTasksAssignedBy() {
        return tasksAssignedBy;
    }

    public void setTasksAssignedBy(Set<TaskAssignment> tasksAssignedBy) {
        this.tasksAssignedBy = tasksAssignedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return adminId != null ? adminId.equals(admin.adminId) : admin.adminId == null;
    }

    @Override
    public int hashCode() {
        return adminId != null ? adminId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Admin{" +
               "adminId=" + adminId +
               ", adminName='" + adminName + '\'' +
               ", role=" + role +
               ", email='" + email + '\'' +
               '}';
    }
}
