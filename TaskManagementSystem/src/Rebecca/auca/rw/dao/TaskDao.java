/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Christian.auca.rw.dao;

/**
 *
 * @author B Christian
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import Christian.auca.rw.model.Task;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;

public class TaskDao {
    private String jdbcUrl = "jdbc:postgresql://localhost:5432/task_management_system_db";
    private String dbUsername = "postgres";
    private String dbPasswd = "A$aprocky08";

    private static final String INSERT_TASK_SQL = "INSERT INTO Tasks (task_id, title, description, category, priority, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TASK_SQL = "UPDATE Tasks SET title=?, description=?, category=?, priority=?, status=? WHERE task_id=?";
    private static final String DELETE_TASK_SQL = "DELETE FROM Tasks WHERE task_id=?";
    private static final String SELECT_ALL_TASKS_SQL = "SELECT * FROM Tasks";
    private static final String SELECT_TASK_BY_ID_SQL = "SELECT * FROM Tasks WHERE task_id=?";

    protected Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPasswd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

   public boolean insertTask(Task task) {
    boolean success = false;
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK_SQL)) {
        // Assuming task.getTaskId() returns a UUID
        preparedStatement.setObject(1, task.getTaskId(), java.sql.Types.OTHER);

        preparedStatement.setString(2, task.getTitle());
        preparedStatement.setString(3, task.getDescription());
        preparedStatement.setString(4, task.getCategory());
        preparedStatement.setString(5, task.getPriority());
        preparedStatement.setString(6, task.getStatus());
        preparedStatement.setInt(7, task.getCreatedBy());
      
        int rowsAffected = preparedStatement.executeUpdate();
        success = rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return success;
}


    public void updateTask(Task task) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_SQL)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getCategory());
            preparedStatement.setString(4, task.getPriority());
            preparedStatement.setString(5, task.getStatus());
            preparedStatement.setObject(6, task.getTaskId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(UUID taskId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK_SQL)) {
            preparedStatement.setObject(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID taskId = (UUID) resultSet.getObject("task_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                String priority = resultSet.getString("priority");
                String status = resultSet.getString("status");
                int createdBy = resultSet.getInt("created_by");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                Task task = new Task();
                task.setTaskId(taskId);
                task.setTitle(title);
                task.setDescription(description);
                task.setCategory(category);
                task.setPriority(priority);
                task.setStatus(status);
                task.setCreatedBy(createdBy);
                task.setCreatedAt(createdAt);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public Task getTaskById(UUID taskId) {
        Task task = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASK_BY_ID_SQL)) {
            preparedStatement.setObject(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                String priority = resultSet.getString("priority");
                String status = resultSet.getString("status");
                int createdBy = resultSet.getInt("created_by");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                task = new Task();
                task.setTaskId(taskId);
                task.setTitle(title);
                task.setDescription(description);
                task.setCategory(category);
                task.setPriority(priority);
                task.setStatus(status);
                task.setCreatedBy(createdBy);
                task.setCreatedAt(createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public void task_display(DefaultTableModel model) {
        try{
            Connection con = DriverManager.getConnection( jdbcUrl, dbUsername, dbPasswd);
            String sql= " select t.task_id, t.title, p.assigned_to, p.deadline,t.priority, t.Status \n" +
"from tasks t JOIN taskassignments p ON t.task_id=p.task_id";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int cols = rsmd.getColumnCount();
            while(rs.next()){
                Object[] data = new Object[cols];
                for(int i=0; i<cols; i++){
                    data[i] = rs.getObject(i+1);
                }
                model.addRow(data);
            }
        }catch(Exception e){
            System.out.println("Error in task display");
            e.printStackTrace();
            
        }
    }
}