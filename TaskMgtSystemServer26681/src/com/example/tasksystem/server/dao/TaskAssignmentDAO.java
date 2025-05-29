package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.TaskAssignment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskAssignmentDAO {
    void save(TaskAssignment taskAssignment);
    Optional<TaskAssignment> findById(UUID id);
    List<TaskAssignment> findAll();
    List<TaskAssignment> findByAppUserId(Integer userId);       // Assignments for a specific AppUser
    List<TaskAssignment> findByTaskId(UUID taskId);             // Assignments for a specific Task
    List<TaskAssignment> findByAssignedByAdminId(Integer adminId); // Assignments made by a specific Admin
    void update(TaskAssignment taskAssignment);
    void delete(TaskAssignment taskAssignment);
}
