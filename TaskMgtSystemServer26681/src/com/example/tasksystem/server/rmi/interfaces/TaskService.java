package com.example.tasksystem.server.rmi.interfaces;

import com.example.tasksystem.server.model.Admin;
import com.example.tasksystem.server.model.AppUser;
import com.example.tasksystem.server.model.Task;
import com.example.tasksystem.server.model.TaskAssignment;
// RoleEnum might not be directly passed in methods but used server-side from Admin object

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface TaskService extends Remote {

    // --- Admin Authentication & Management ---
    /**
     * Authenticates an Admin user.
     * @param adminUsername Corresponds to 'adminName' in the Admin entity.
     * @param password The admin's password.
     * @return The authenticated Admin object if successful, otherwise throws exception.
     * @throws RemoteException If login fails or communication error.
     */
    Admin adminLogin(String adminUsername, String password) throws RemoteException;

    // --- AppUser Management by Admin ---
    /**
     * Creates a new AppUser. Requires an authenticated Admin.
     * @param appUser The AppUser object to create.
     * @param performingAdminId The ID of the Admin performing this action.
     * @throws RemoteException If creation fails or permission denied.
     */
    void createAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException;

    /**
     * Retrieves an AppUser by their ID. Requires an authenticated Admin.
     * @param userId The ID of the AppUser to retrieve.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return The AppUser object, or null if not found (service implementation may throw exception).
     * @throws RemoteException If retrieval fails or permission denied.
     */
    AppUser getAppUserById(Integer userId, Integer performingAdminId) throws RemoteException;

    /**
     * Retrieves all AppUsers. Requires an authenticated Admin.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return A list of all AppUser objects.
     * @throws RemoteException If retrieval fails or permission denied.
     */
    List<AppUser> getAllAppUsers(Integer performingAdminId) throws RemoteException;

    /**
     * Updates an existing AppUser. Requires an authenticated Admin.
     * @param appUser The AppUser object with updated information.
     * @param performingAdminId The ID of the Admin performing this action.
     * @throws RemoteException If update fails or permission denied.
     */
    void updateAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException;

    // void deleteAppUser(Integer userId, Integer performingAdminId) throws RemoteException; // Optional

    // --- AppUser Authentication ---
    /**
     * Authenticates an AppUser.
     * @param username The AppUser's username.
     * @param password The AppUser's password.
     * @return The authenticated AppUser object if successful.
     * @throws RemoteException If login fails or communication error.
     */
    AppUser appUserLogin(String username, String password) throws RemoteException;

    // --- Task Management by Admin ---
    /**
     * Creates a new Task. The task object should have its 'createdByAdmin' field set.
     * @param task The Task object to create.
     * @param adminCreatorId The ID of the Admin creating this task (should match task.getCreatedByAdmin().getAdminId()).
     * @throws RemoteException If creation fails or permission denied.
     */
    void createTask(Task task, Integer adminCreatorId) throws RemoteException;

    /**
     * Retrieves a Task by its ID.
     * @param taskId The UUID of the Task.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return The Task object, or null if not found (service implementation may throw exception).
     * @throws RemoteException If retrieval fails or permission denied.
     */
    Task getTaskById(UUID taskId, Integer performingAdminId) throws RemoteException;

    /**
     * Retrieves all Tasks created by a specific Admin.
     * @param adminCreatorId The ID of the Admin.
     * @return A list of Tasks.
     * @throws RemoteException If retrieval fails.
     */
    List<Task> getAllTasksCreatedByAdmin(Integer adminCreatorId) throws RemoteException;

    /**
     * Retrieves all Tasks in the system. Intended for SUPERADMIN or high-level overview.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return A list of all Tasks.
     * @throws RemoteException If retrieval fails or permission denied.
     */
    List<Task> getAllTasksSystemWide(Integer performingAdminId) throws RemoteException;

    /**
     * Updates an existing Task.
     * @param task The Task object with updated information.
     * @param performingAdminId The ID of the Admin performing this action.
     * @throws RemoteException If update fails or permission denied.
     */
    void updateTask(Task task, Integer performingAdminId) throws RemoteException;

    // void deleteTask(UUID taskId, Integer performingAdminId) throws RemoteException; // Optional

    // --- Task Assignment Management by Admin ---
    /**
     * Assigns a Task to an AppUser. The TaskAssignment object should contain Task, AppUser, and AssignedByAdmin references or their IDs.
     * @param assignment The TaskAssignment object to create.
     * @param assigningAdminId The ID of the Admin performing the assignment.
     * @throws RemoteException If assignment fails or permission denied.
     */
    void assignTaskToAppUser(TaskAssignment assignment, Integer assigningAdminId) throws RemoteException;

    /**
     * Retrieves a specific TaskAssignment by its ID.
     * @param assignmentId The UUID of the TaskAssignment.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return The TaskAssignment object, or null if not found (service implementation may throw exception).
     * @throws RemoteException If retrieval fails or permission denied.
     */
    TaskAssignment getTaskAssignmentById(UUID assignmentId, Integer performingAdminId) throws RemoteException;

    /**
     * Retrieves all assignments for a specific Task.
     * @param taskId The UUID of the Task.
     * @param performingAdminId The ID of the Admin performing this action.
     * @return A list of TaskAssignments.
     * @throws RemoteException If retrieval fails or permission denied.
     */
    List<TaskAssignment> getAssignmentsForTask(UUID taskId, Integer performingAdminId) throws RemoteException;
    
    /**
     * Retrieves all assignments made by a specific Admin.
     * @param assigningAdminId The ID of the Admin.
     * @return A list of TaskAssignments.
     * @throws RemoteException If retrieval fails.
     */
    List<TaskAssignment> getAssignmentsByAdmin(Integer assigningAdminId) throws RemoteException;

    // void updateTaskAssignmentByAdmin(TaskAssignment assignment, Integer performingAdminId) throws RemoteException;
    // void unassignTask(UUID assignmentId, Integer performingAdminId) throws RemoteException; // Optional

    // --- Task Management by AppUser ---
    /**
     * Retrieves all tasks assigned to the currently logged-in AppUser.
     * @param appUserId The ID of the AppUser.
     * @return A list of TaskAssignments.
     * @throws RemoteException If retrieval fails.
     */
    List<TaskAssignment> getMyAssignedTasks(Integer appUserId) throws RemoteException;

    /**
     * Allows an AppUser to update the completion status of their own task assignment.
     * @param assignmentId The UUID of the TaskAssignment.
     * @param completed The new completion status.
     * @param appUserId The ID of the AppUser performing this action (for verification).
     * @throws RemoteException If update fails or permission denied.
     */
    void updateTaskAssignmentStatus(UUID assignmentId, boolean completed, Integer appUserId) throws RemoteException;

}
