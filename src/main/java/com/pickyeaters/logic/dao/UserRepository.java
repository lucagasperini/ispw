package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByID(String id);
    void editUser(User user);
}
