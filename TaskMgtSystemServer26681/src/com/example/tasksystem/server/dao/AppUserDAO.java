package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.model.AppUser;
import java.util.List;
import java.util.Optional;

public interface AppUserDAO {
    void save(AppUser appUser);
    Optional<AppUser> findById(Integer id);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    List<AppUser> findAll();
    void update(AppUser appUser);
    void delete(AppUser appUser);
}
