package com.example.tasksystem.server.services;

import com.example.tasksystem.rmi.TaskService;
import com.example.tasksystem.server.dao.ProjectDAO;
import com.example.tasksystem.server.dao.TaskDAO;
import com.example.tasksystem.server.dao.UserDAO;
import com.example.tasksystem.server.dao.impl.ProjectDAOImpl;
import com.example.tasksystem.server.dao.impl.TaskDAOImpl;
import com.example.tasksystem.server.dao.impl.UserDAOImpl;
import com.example.tasksystem.server.entities.Project;
import com.example.tasksystem.server.entities.Task;
import com.example.tasksystem.server.entities.User;
import com.example.tasksystem.server.util.HibernateUtil; // For potential session management if needed directly
import org.hibernate.Hibernate; // For initializing lazy collections if needed

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
// import java.util.UUID; // Alternative for OTP or session tokens

public class TaskServiceImpl extends UnicastRemoteObject implements TaskService {
    private final UserDAO userDAO;
    private final ProjectDAO projectDAO;
    private final TaskDAO taskDAO;

    // OTP configuration
    private static final long OTP_VALIDITY_DURATION_MINUTES = 5;

    public TaskServiceImpl() throws RemoteException {
        super(); // Constructor for UnicastRemoteObject
        this.userDAO = new UserDAOImpl();
        this.projectDAO = new ProjectDAOImpl();
        this.taskDAO = new TaskDAOImpl();
    }

    @Override
    public User login(String usernameOrEmail, String password) throws RemoteException {
        Optional<User> userOptional = userDAO.findByUsername(usernameOrEmail);
        if (!userOptional.isPresent()) {
            userOptional = userDAO.findByEmail(usernameOrEmail);
        }

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // TODO: HIGHLY INSECURE - Replace with password hashing and secure comparison.
            if (user.getPassword().equals(password)) {
                // Initialize lazy collections if they are to be used by the client
                // Hibernate.initialize(user.getTasks());
                // Hibernate.initialize(user.getProjects());
                return user; // Login successful
            }
        }
        throw new RemoteException("Invalid username/email or password.");
    }

    @Override
    public String generateOtp(String emailOrPhone) throws RemoteException {
        // Assuming email for OTP for now
        Optional<User> userOptional = userDAO.findByEmail(emailOrPhone);
        if (!userOptional.isPresent()) {
            throw new RemoteException("User not found for OTP generation with email: " + emailOrPhone);
        }
        User user = userOptional.get();
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpTimestamp(LocalDateTime.now());
        userDAO.update(user);
        // For a real application, send OTP via email/SMS.
        return otp; // Returning OTP for testing/demo purposes.
    }

    @Override
    public boolean verifyOtp(String emailOrPhone, String otp) throws RemoteException {
        Optional<User> userOptional = userDAO.findByEmail(emailOrPhone); // Assuming email for OTP
        if (!userOptional.isPresent()) {
            throw new RemoteException("User not found for OTP verification with email: " + emailOrPhone);
        }
        User user = userOptional.get();
        if (user.getOtp() != null && user.getOtp().equals(otp)) {
            if (user.getOtpTimestamp() != null &&
                LocalDateTime.now().isBefore(user.getOtpTimestamp().plusMinutes(OTP_VALIDITY_DURATION_MINUTES))) {
                user.setOtp(null); // Clear OTP after successful verification
                user.setOtpTimestamp(null);
                userDAO.update(user);
                return true;
            } else {
                throw new RemoteException("OTP has expired.");
            }
        }
        throw new RemoteException("Invalid OTP.");
    }

    @Override
    public void createTask(Task task) throws RemoteException {
        try {
            if (task.getAssignee() != null && task.getAssignee().getId() != null) {
                User assignee = userDAO.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RemoteException("Assignee (User) not found with ID: " + task.getAssignee().getId()));
                task.setAssignee(assignee);
            } else {
                task.setAssignee(null); // Ensure assignee is null if not provided or ID is null
            }

            if (task.getProject() == null || task.getProject().getId() == null) {
                 throw new RemoteException("Project ID is required to create a task.");
            }
            Project project = projectDAO.findById(task.getProject().getId())
                .orElseThrow(() -> new RemoteException("Project not found with ID: " + task.getProject().getId()));
            task.setProject(project);
            
            taskDAO.save(task);
        } catch (Exception e) {
            throw new RemoteException("Error creating task: " + e.getMessage(), e);
        }
    }

    @Override
    public Task getTaskById(Long taskId) throws RemoteException {
        try {
            return taskDAO.findById(taskId).map(task -> {
                // Initialize lazy collections if needed by client
                // Hibernate.initialize(task.getAssignee()); 
                // Hibernate.initialize(task.getProject());
                return task;
            }).orElse(null);
        } catch (Exception e) {
            throw new RemoteException("Error getting task by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getAllTasks() throws RemoteException {
        try {
            return taskDAO.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error getting all tasks: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getTasksByProjectId(Long projectId) throws RemoteException {
        try {
            return taskDAO.findByProjectId(projectId);
        } catch (Exception e) {
            throw new RemoteException("Error getting tasks by project ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) throws RemoteException {
        try {
            return taskDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new RemoteException("Error getting tasks by user ID: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateTask(Task task) throws RemoteException {
        try {
            if (task.getId() == null) {
                throw new RemoteException("Task ID is required for update.");
            }
            Task existingTask = taskDAO.findById(task.getId())
                .orElseThrow(() -> new RemoteException("Task not found for update with ID: " + task.getId()));

            // Update fields from the provided task object
            existingTask.setTaskTitle(task.getTaskTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setPriority(task.getPriority());
            existingTask.setStatus(task.getStatus());

            if (task.getAssignee() != null && task.getAssignee().getId() != null) {
                User assignee = userDAO.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RemoteException("Assignee (User) not found for update with ID: " + task.getAssignee().getId()));
                existingTask.setAssignee(assignee);
            } else {
                existingTask.setAssignee(null);
            }

            if (task.getProject() != null && task.getProject().getId() != null) {
                Project project = projectDAO.findById(task.getProject().getId())
                    .orElseThrow(() -> new RemoteException("Project not found for update with ID: " + task.getProject().getId()));
                existingTask.setProject(project);
            } else {
                 throw new RemoteException("Project association cannot be null for a task during update.");
            }
            taskDAO.update(existingTask);
        } catch (Exception e) {
            throw new RemoteException("Error updating task: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteTask(Long taskId) throws RemoteException {
        try {
            Task task = taskDAO.findById(taskId)
                .orElseThrow(() -> new RemoteException("Task not found for deletion. ID: " + taskId));
            taskDAO.delete(task);
        } catch (Exception e) {
            throw new RemoteException("Error deleting task: " + e.getMessage(), e);
        }
    }

    @Override
    public void createProject(Project project) throws RemoteException {
        try {
            projectDAO.save(project);
        } catch (Exception e) {
            throw new RemoteException("Error creating project: " + e.getMessage(), e);
        }
    }

    @Override
    public Project getProjectById(Long projectId) throws RemoteException {
        try {
            return projectDAO.findById(projectId).map(p -> {
                // Initialize lazy collections if needed for RMI client
                // Hibernate.initialize(p.getMembers());
                // Hibernate.initialize(p.getTasks());
                return p;
            }).orElse(null);
        } catch (Exception e) {
            throw new RemoteException("Error getting project by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Project> getAllProjects() throws RemoteException {
        try {
            return projectDAO.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error getting all projects: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateProject(Project project) throws RemoteException {
        try {
            if (project.getId() == null) {
                throw new RemoteException("Project ID is required for update.");
            }
             Project existingProject = projectDAO.findById(project.getId())
                .orElseThrow(() -> new RemoteException("Project not found for update with ID: " + project.getId()));

            existingProject.setProjectName(project.getProjectName());
            existingProject.setDescription(project.getDescription());
            existingProject.setStartDate(project.getStartDate());
            existingProject.setEndDate(project.getEndDate());
            existingProject.setStatus(project.getStatus());
            // Member list updates should be handled by addUserToProject/removeUserFromProject
            projectDAO.update(existingProject);
        } catch (Exception e) {
            throw new RemoteException("Error updating project: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProject(Long projectId) throws RemoteException {
        try {
            Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new RemoteException("Project not found for deletion. ID: " + projectId));
            
            // Optional: Add logic to handle tasks associated with the project being deleted.
            // For example, disassociate them or delete them based on business rules.
            // List<Task> tasks = taskDAO.findByProjectId(projectId);
            // for (Task task : tasks) { /* delete or update task */ }

            projectDAO.delete(project);
        } catch (Exception e) {
            throw new RemoteException("Error deleting project: " + e.getMessage(), e);
        }
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws RemoteException {
        try {
            Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new RemoteException("Project not found. ID: " + projectId));
            User user = userDAO.findById(userId)
                .orElseThrow(() -> new RemoteException("User not found. ID: " + userId));
            
            // Initialize members collection if it's lazy and not yet initialized
            Hibernate.initialize(project.getMembers());
            project.getMembers().add(user);
            projectDAO.update(project);
        } catch (Exception e) {
            throw new RemoteException("Error adding user to project: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws RemoteException {
        try {
            Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new RemoteException("Project not found. ID: " + projectId));
            User user = userDAO.findById(userId)
                .orElseThrow(() -> new RemoteException("User not found. ID: " + userId));

            Hibernate.initialize(project.getMembers());
            project.getMembers().remove(user);
            projectDAO.update(project);
        } catch (Exception e) {
            throw new RemoteException("Error removing user from project: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<User> getUsersForProject(Long projectId) throws RemoteException {
        try {
            Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new RemoteException("Project not found. ID: " + projectId));
            // Eagerly fetch or initialize members if lazy
            Hibernate.initialize(project.getMembers());
            return new ArrayList<>(project.getMembers());
        } catch (Exception e) {
            throw new RemoteException("Error getting users for project: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error getting all users: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getTasksForReport() throws RemoteException {
        try {
            // This is a simplistic implementation. Real reports might need more complex queries/DTOs.
            return taskDAO.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error generating tasks report: " + e.getMessage(), e);
        }
    }
}
