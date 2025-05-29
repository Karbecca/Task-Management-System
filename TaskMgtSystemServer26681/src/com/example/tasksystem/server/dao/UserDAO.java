package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void update(User user);
    void delete(User user);
    // Consider adding: boolean usernameExists(String username);
    // Consider adding: boolean emailExists(String email);
}
