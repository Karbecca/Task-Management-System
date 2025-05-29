package Christian.auca.rw.dao;

import Christian.auca.rw.model.TaskAssignment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.UUID;


/**
 *
 * @author B Christian
 */
public class TaskAssignmentsDao {
    // JDBC connection details
    private String jdbcUrl = "jdbc:postgresql://localhost:5432/task_management_system_db";
    private String dbUsername = "postgres";
    private String dbPasswd = "A$aprocky08";

    // SQL queries
    private static final String INSERT_TASK_ASSIGNMENT_SQL = "INSERT INTO TaskAssignments (assignment_id, task_id, assigned_to, assigned_by, assigned_at, completed, deadline) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_TASK_ASSIGNMENTS_SQL = "SELECT * FROM TaskAssignments";
    private static final String SELECT_TASK_ASSIGNMENT_BY_ID_SQL = "SELECT * FROM TaskAssignments WHERE assignment_id = ?";
    private static final String UPDATE_TASK_ASSIGNMENT_SQL = "UPDATE TaskAssignments SET task_id = ?, assigned_to = ?, assigned_by = ?, assigned_at = ?, completed = ?, deadline = ? WHERE assignment_id = ?";
    private static final String DELETE_TASK_ASSIGNMENT_SQL = "DELETE FROM TaskAssignments WHERE assignment_id = ?";

    // Database connection method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd);
    }

   // Insert a new task assignment
// Insert a new task assignment
public boolean insertTaskAssignment(TaskAssignment taskAssignment) {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK_ASSIGNMENT_SQL)) {
        preparedStatement.setObject(1, taskAssignment.getAssignmentId());
        preparedStatement.setObject(2, taskAssignment.getTaskId());
        preparedStatement.setInt(3, taskAssignment.getAssignedTo());
        preparedStatement.setInt(4, taskAssignment.getAssignedBy());
        preparedStatement.setTimestamp(5, new java.sql.Timestamp(taskAssignment.getAssignedAt().getTime()));
        preparedStatement.setBoolean(6, taskAssignment.isCompleted());
        preparedStatement.setDate(7, new java.sql.Date(taskAssignment.getDeadline().getTime()));
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}



    // Update an existing task assignment
    public void updateTaskAssignment(TaskAssignment taskAssignment) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_ASSIGNMENT_SQL)) {
            preparedStatement.setObject(1, taskAssignment.getTaskId());
            preparedStatement.setInt(2, taskAssignment.getAssignedTo());
            preparedStatement.setInt(3, taskAssignment.getAssignedBy());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(taskAssignment.getAssignedAt().getTime()));
            preparedStatement.setBoolean(5, taskAssignment.isCompleted());
            preparedStatement.setDate(6, new java.sql.Date(taskAssignment.getDeadline().getTime()));
            preparedStatement.setObject(7, taskAssignment.getAssignmentId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a task assignment by ID
    public void deleteTaskAssignment(UUID assignmentId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK_ASSIGNMENT_SQL)) {
            preparedStatement.setObject(1, assignmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all task assignments from the database
    public List<TaskAssignment> getAllTaskAssignments() {
        List<TaskAssignment> taskAssignments = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASK_ASSIGNMENTS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID assignmentId = UUID.fromString(resultSet.getString("assignment_id"));
                UUID taskId = UUID.fromString(resultSet.getString("task_id"));
                int assignedTo = resultSet.getInt("assigned_to");
                int assignedBy = resultSet.getInt("assigned_by");
                Timestamp assignedAt = resultSet.getTimestamp("assigned_at");
                boolean completed = resultSet.getBoolean("completed");
                Date deadline = resultSet.getDate("deadline");

                TaskAssignment taskAssignment = new TaskAssignment();
                taskAssignment.setAssignmentId(assignmentId);
                taskAssignment.setTaskId(taskId);
                taskAssignment.setAssignedTo(assignedTo);
                taskAssignment.setAssignedBy(assignedBy);
                taskAssignment.setAssignedAt(assignedAt);
                taskAssignment.setCompleted(completed);
                taskAssignment.setDeadline(deadline);

                taskAssignments.add(taskAssignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskAssignments;
    }
}
