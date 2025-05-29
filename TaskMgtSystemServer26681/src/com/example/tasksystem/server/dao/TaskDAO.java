package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskDAO {
    void save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAll();
    List<Task> findByAdminCreatorId(Integer adminId); // Tasks created by a specific Admin
    void update(Task task);
    void delete(Task task);
}
