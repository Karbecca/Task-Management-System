package com.example.tasksystem.shared.interfaces;

import com.example.tasksystem.shared.model.Admin;
import com.example.tasksystem.shared.model.AppUser;
import com.example.tasksystem.shared.model.Task;
import com.example.tasksystem.shared.model.TaskAssignment;
// RoleEnum might not be directly passed in methods but used server-side from Admin object

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface TaskService extends Remote {

    // --- Admin Authentication & Management ---
    Admin adminLogin(String adminUsername, String password) throws RemoteException;

    // --- AppUser Management by Admin ---
    void createAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException;
    AppUser getAppUserById(Integer userId, Integer performingAdminId) throws RemoteException;
    List<AppUser> getAllAppUsers(Integer performingAdminId) throws RemoteException;
    void updateAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException;

    // --- AppUser Authentication ---
    AppUser appUserLogin(String username, String password) throws RemoteException;

    // --- Task Management by Admin ---
    void createTask(Task task, Integer adminCreatorId) throws RemoteException;
    Task getTaskById(UUID taskId, Integer performingAdminId) throws RemoteException;
    List<Task> getAllTasksCreatedByAdmin(Integer adminCreatorId) throws RemoteException;
    List<Task> getAllTasksSystemWide(Integer performingAdminId) throws RemoteException;
    void updateTask(Task task, Integer performingAdminId) throws RemoteException;

    // --- Task Assignment Management by Admin ---
    void assignTaskToAppUser(TaskAssignment assignment, Integer assigningAdminId) throws RemoteException;
    TaskAssignment getTaskAssignmentById(UUID assignmentId, Integer performingAdminId) throws RemoteException;
    List<TaskAssignment> getAssignmentsForTask(UUID taskId, Integer performingAdminId) throws RemoteException;
    List<TaskAssignment> getAssignmentsByAdmin(Integer assigningAdminId) throws RemoteException;

    // --- Task Management by AppUser ---
    List<TaskAssignment> getMyAssignedTasks(Integer appUserId) throws RemoteException;
    void updateTaskAssignmentStatus(UUID assignmentId, boolean completed, Integer appUserId) throws RemoteException;
}
