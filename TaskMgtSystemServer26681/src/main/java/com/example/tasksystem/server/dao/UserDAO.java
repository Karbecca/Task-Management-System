package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // For OTP
    List<User> findAll();
    void update(User user);
    void delete(User user);
}
