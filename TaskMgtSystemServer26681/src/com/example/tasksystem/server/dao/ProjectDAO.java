package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectDAO {
    void save(Project project);
    Optional<Project> findById(Long id);
    List<Project> findAll();
    void update(Project project);
    void delete(Project project);
    // Optional<Project> findByProjectName(String projectName);
}
