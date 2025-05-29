package com.example.tasksystem.rmi;

import com.example.tasksystem.server.entities.User;
import com.example.tasksystem.server.entities.Project;
import com.example.tasksystem.server.entities.Task;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaskService extends Remote {

    // Authentication
    User login(String usernameOrEmail, String password) throws RemoteException;
    String generateOtp(String emailOrPhone) throws RemoteException; // Returns OTP or a success/failure message
    boolean verifyOtp(String emailOrPhone, String otp) throws RemoteException;

    // Task Management
    void createTask(Task task) throws RemoteException;
    Task getTaskById(Long taskId) throws RemoteException;
    List<Task> getAllTasks() throws RemoteException;
    List<Task> getTasksByProjectId(Long projectId) throws RemoteException;
    List<Task> getTasksByUserId(Long userId) throws RemoteException; // Tasks assigned to a user
    void updateTask(Task task) throws RemoteException;
    void deleteTask(Long taskId) throws RemoteException;

    // Project Management
    void createProject(Project project) throws RemoteException;
    Project getProjectById(Long projectId) throws RemoteException;
    List<Project> getAllProjects() throws RemoteException;
    void updateProject(Project project) throws RemoteException;
    void deleteProject(Long projectId) throws RemoteException;
    void addUserToProject(Long projectId, Long userId) throws RemoteException;
    void removeUserFromProject(Long projectId, Long userId) throws RemoteException;
    List<User> getUsersForProject(Long projectId) throws RemoteException;

    // User Management (limited scope for client use)
    List<User> getAllUsers() throws RemoteException; // e.g., for assigning tasks

    // Reporting
    List<Task> getTasksForReport() throws RemoteException; // A generic method to get tasks for reporting
}
