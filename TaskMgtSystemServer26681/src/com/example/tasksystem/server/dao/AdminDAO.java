package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminDAO {
    void save(Admin admin);
    Optional<Admin> findById(Integer id);
    Optional<Admin> findByAdminName(String adminName); // Changed from findByUsername to match entity field
    Optional<Admin> findByEmail(String email);
    List<Admin> findAll();
    void update(Admin admin);
    void delete(Admin admin);
}
