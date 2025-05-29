package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.entities.Task;
import java.util.List;
import java.util.Optional;

public interface TaskDAO {
    void save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByProjectId(Long projectId);
    List<Task> findByUserId(Long userId);
    void update(Task task);
    void delete(Task task);
}
