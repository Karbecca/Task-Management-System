package com.example.tasksystem.server.rmi.services;

import com.example.tasksystem.server.dao.*;
import com.example.tasksystem.server.model.*;
import com.example.tasksystem.server.rmi.interfaces.TaskService;
import org.hibernate.Hibernate; // For potential explicit initialization

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class TaskServiceImpl extends UnicastRemoteObject implements TaskService {

    private final AdminDAO adminDAO;
    private final AppUserDAO appUserDAO;
    private final TaskDAO taskDAO;
    private final TaskAssignmentDAO taskAssignmentDAO;

    // private static final long OTP_VALIDITY_DURATION_MINUTES = 5; // Not part of new interface design

    public TaskServiceImpl() throws RemoteException {
        super(); // Calls UnicastRemoteObject constructor
        this.adminDAO = new AdminDAOImpl();
        this.appUserDAO = new AppUserDAOImpl();
        this.taskDAO = new TaskDAOImpl();
        this.taskAssignmentDAO = new TaskAssignmentDAOImpl();
    }

    // --- Helper for Admin Auth (Placeholder) ---
    private Admin getPerformingAdmin(Integer performingAdminId) throws RemoteException {
        if (performingAdminId == null) {
            throw new RemoteException("Performing admin ID cannot be null for this operation.");
        }
        return adminDAO.findById(performingAdminId)
                .orElseThrow(() -> new RemoteException("Performing admin not found or not authorized. ID: " + performingAdminId));
    }
    
    private void checkSuperAdmin(Admin admin) throws RemoteException {
        if (admin.getRole() != RoleEnum.SUPERADMIN) {
            throw new RemoteException("This operation is restricted to SUPERADMIN role.");
        }
    }


    // --- Admin Authentication & Management ---
    @Override
    public Admin adminLogin(String adminUsername, String password) throws RemoteException {
        if (adminUsername == null || adminUsername.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new RemoteException("Admin username and password cannot be empty.");
        }
        Optional<Admin> adminOptional = adminDAO.findByAdminName(adminUsername);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // IMPORTANT: Plain text password. Replace with hashed password comparison in a real app.
            if (admin.getAdminPass().equals(password)) {
                return admin;
            }
        }
        throw new RemoteException("Invalid admin username or password.");
    }

    // --- AppUser Management by Admin ---
    @Override
    public void createAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (appUser == null) {
            throw new RemoteException("AppUser object cannot be null.");
        }
        // Further validation (e.g., unique username/email) should ideally be in DAO or service layer
        appUserDAO.save(appUser);
    }

    @Override
    public AppUser getAppUserById(Integer userId, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (userId == null) throw new RemoteException("AppUser ID cannot be null.");
        return appUserDAO.findById(userId)
                .orElseThrow(() -> new RemoteException("AppUser not found with ID: " + userId));
    }

    @Override
    public List<AppUser> getAllAppUsers(Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        return appUserDAO.findAll();
    }

    @Override
    public void updateAppUser(AppUser appUser, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (appUser == null || appUser.getUserId() == null) {
            throw new RemoteException("AppUser or AppUser ID cannot be null for update.");
        }
        appUserDAO.findById(appUser.getUserId())
            .orElseThrow(() -> new RemoteException("AppUser not found for update with ID: " + appUser.getUserId()));
        appUserDAO.update(appUser);
    }

    // --- AppUser Authentication ---
    @Override
    public AppUser appUserLogin(String username, String password) throws RemoteException {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new RemoteException("AppUser username and password cannot be empty.");
        }
        Optional<AppUser> appUserOptional = appUserDAO.findByUsername(username);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            // IMPORTANT: Plain text password.
            if (appUser.getPassword().equals(password)) {
                return appUser;
            }
        }
        throw new RemoteException("Invalid app user username or password.");
    }

    // --- Task Management by Admin ---
    @Override
    public void createTask(Task task, Integer adminCreatorId) throws RemoteException {
        Admin creator = getPerformingAdmin(adminCreatorId);
        if (task == null) {
            throw new RemoteException("Task object cannot be null.");
        }
        task.setCreatedByAdmin(creator);
        // taskId and createdAt are handled by @PrePersist in Task entity
        taskDAO.save(task);
    }

    @Override
    public Task getTaskById(UUID taskId, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (taskId == null) throw new RemoteException("Task ID cannot be null.");
        return taskDAO.findById(taskId)
                .orElseThrow(() -> new RemoteException("Task not found with ID: " + taskId));
    }

    @Override
    public List<Task> getAllTasksCreatedByAdmin(Integer adminCreatorId) throws RemoteException {
        getPerformingAdmin(adminCreatorId); // Ensures admin exists
        return taskDAO.findByAdminCreatorId(adminCreatorId);
    }

    @Override
    public List<Task> getAllTasksSystemWide(Integer performingAdminId) throws RemoteException {
        Admin admin = getPerformingAdmin(performingAdminId);
        checkSuperAdmin(admin); // Example of role-based authorization
        return taskDAO.findAll();
    }

    @Override
    public void updateTask(Task task, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (task == null || task.getTaskId() == null) {
            throw new RemoteException("Task or Task ID cannot be null for update.");
        }
        Task existingTask = taskDAO.findById(task.getTaskId())
            .orElseThrow(() -> new RemoteException("Task not found for update with ID: " + task.getTaskId()));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCategory(task.getCategory());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        
        if (task.getCreatedByAdmin() != null && task.getCreatedByAdmin().getAdminId() != null) {
             Admin newCreator = adminDAO.findById(task.getCreatedByAdmin().getAdminId())
                .orElseThrow(() -> new RemoteException("New creator admin not found with ID: " + task.getCreatedByAdmin().getAdminId()));
            existingTask.setCreatedByAdmin(newCreator);
        } else {
             throw new RemoteException("Task creator admin (createdByAdmin) cannot be null or have a null ID during update.");
        }
        
        taskDAO.update(existingTask);
    }

    // --- Task Assignment Management by Admin ---
    @Override
    public void assignTaskToAppUser(TaskAssignment assignment, Integer assigningAdminId) throws RemoteException {
        Admin assigningAdmin = getPerformingAdmin(assigningAdminId);
        if (assignment == null) throw new RemoteException("TaskAssignment object cannot be null.");
        if (assignment.getTask() == null || assignment.getTask().getTaskId() == null) {
            throw new RemoteException("Task to be assigned must be specified with a valid Task ID.");
        }
        if (assignment.getAssignedAppUser() == null || assignment.getAssignedAppUser().getUserId() == null) {
            throw new RemoteException("AppUser to assign the task to must be specified with a valid User ID.");
        }


        Task task = taskDAO.findById(assignment.getTask().getTaskId())
                .orElseThrow(() -> new RemoteException("Task to assign not found. ID: " + assignment.getTask().getTaskId()));
        AppUser appUser = appUserDAO.findById(assignment.getAssignedAppUser().getUserId())
                .orElseThrow(() -> new RemoteException("AppUser to assign to not found. ID: " + assignment.getAssignedAppUser().getUserId()));

        assignment.setTask(task);
        assignment.setAssignedAppUser(appUser);
        assignment.setAssignedByAdmin(assigningAdmin);
        // assignmentId and assignedAt are handled by @PrePersist in TaskAssignment entity
        taskAssignmentDAO.save(assignment);
    }

    @Override
    public TaskAssignment getTaskAssignmentById(UUID assignmentId, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (assignmentId == null) throw new RemoteException("Assignment ID cannot be null.");
        return taskAssignmentDAO.findById(assignmentId)
                .orElseThrow(() -> new RemoteException("TaskAssignment not found with ID: " + assignmentId));
    }

    @Override
    public List<TaskAssignment> getAssignmentsForTask(UUID taskId, Integer performingAdminId) throws RemoteException {
        getPerformingAdmin(performingAdminId); // Authorization placeholder
        if (taskId == null) throw new RemoteException("Task ID cannot be null.");
        return taskAssignmentDAO.findByTaskId(taskId);
    }
    
    @Override
    public List<TaskAssignment> getAssignmentsByAdmin(Integer assigningAdminId) throws RemoteException {
        getPerformingAdmin(assigningAdminId); // Ensures admin exists
        return taskAssignmentDAO.findByAssignedByAdminId(assigningAdminId);
    }

    // --- Task Management by AppUser ---
    @Override
    public List<TaskAssignment> getMyAssignedTasks(Integer appUserId) throws RemoteException {
        if (appUserId == null) {
            throw new RemoteException("AppUser ID cannot be null.");
        }
        appUserDAO.findById(appUserId) // Ensure user exists
                .orElseThrow(() -> new RemoteException("AppUser not found with ID: " + appUserId));
        return taskAssignmentDAO.findByAppUserId(appUserId);
    }

    @Override
    public void updateTaskAssignmentStatus(UUID assignmentId, boolean completed, Integer appUserId) throws RemoteException {
        if (assignmentId == null || appUserId == null) {
            throw new RemoteException("Assignment ID or AppUser ID cannot be null.");
        }
        TaskAssignment assignment = taskAssignmentDAO.findById(assignmentId)
                .orElseThrow(() -> new RemoteException("TaskAssignment not found with ID: " + assignmentId));

        if (assignment.getAssignedAppUser() == null || !assignment.getAssignedAppUser().getUserId().equals(appUserId)) {
            throw new RemoteException("User not authorized to update this task assignment status.");
        }

        assignment.setCompleted(completed);
        taskAssignmentDAO.update(assignment);
    }
}
