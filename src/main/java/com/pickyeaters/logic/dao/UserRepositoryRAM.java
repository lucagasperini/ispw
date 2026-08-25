package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.Pickie;
import com.pickyeaters.logic.model.Restaurateur;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryRAM implements UserRepository {
    private final Logger logger;
    private final List<User> userList = new ArrayList<>();

    public UserRepositoryRAM(Logger logger) {
        this.logger = logger;
        userList.add(new Restaurateur("1", "lucaR", "luca", "Luca", "Bianchi"));
        userList.add(new Pickie("2", "lucaP", "luca", "Luca", "Rossi"));
        userList.add(new Pickie("3", "testP", "test", "Marco", "Bianchi"));
        userList.add(new Restaurateur("4", "testR", "test", "Marco", "Rossi"));
        userList.add(new Restaurateur("5", "test", "test", "Giuseppe", "Verdi"));
    }

    public Optional<User> getUserByEmail(String email) {
        for(User user : userList) {
            if(user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserByID(String id) {
        for(User user : userList) {
            if(user.getID().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void editUser(User user) {
        for(User i : userList) {
            if(i.getID().equals(user.getID())) {
                i.setEmail(user.getEmail());
                i.setLastname(user.getLastname());
                i.setFirstname(user.getFirstname());
            }
        }
    }
}
