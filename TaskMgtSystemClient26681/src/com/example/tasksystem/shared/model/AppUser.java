package com.example.tasksystem.shared.model; // Updated package

import javax.persistence.*; // Kept
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
// Import for TaskAssignment will be from this package

@Entity // Kept
@Table(name = "app_user") // Kept
public class AppUser implements Serializable {

    private static final long serialVersionUID = 202L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Kept
    @Column(name = "user_id") // Kept
    private Integer userId;

    @Column(nullable = false, length = 100) // Kept
    private String username;

    @Column(unique = true, nullable = false, length = 150) // Kept
    private String email;

    @Column(name = "phone_number", length = 20) // Kept
    private String phoneNumber;

    @Column(nullable = false, length = 100) // Kept
    private String password;

    @Column(columnDefinition = "TEXT") // Kept
    private String address;

    @Column(length = 10) // Kept
    private String gender;

    @OneToMany(mappedBy = "assignedAppUser", // Kept
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, // Kept
               fetch = FetchType.LAZY) // Kept
    private Set<TaskAssignment> taskAssignments = new HashSet<>();

    // Default constructor
    public AppUser() {
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<TaskAssignment> getTaskAssignments() {
        return taskAssignments;
    }

    public void setTaskAssignments(Set<TaskAssignment> taskAssignments) {
        this.taskAssignments = taskAssignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return userId != null ? userId.equals(appUser.userId) : appUser.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return username != null ? username : "User ID: " + userId;
        // Or more detailed:
        // return "AppUser{" +
        //        "userId=" + userId +
        //        ", username='" + username + '\'' +
        //        ", email='" + email + '\'' +
        //        '}';
    }
}
