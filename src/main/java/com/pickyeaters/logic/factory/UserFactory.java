package com.pickyeaters.logic.factory;

import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Admin;
import com.pickyeaters.logic.model.Pickie;
import com.pickyeaters.logic.model.Restaurateur;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

public class UserFactory {
    private final Logger logger;

    public UserFactory(Logger logger) {
        this.logger = logger;
    }

    public User createUser(String id, String email, String password, String firstname, String lastname, String type) {
        return switch (type) {
            case "REST" -> new Restaurateur(id, email, password, firstname, lastname);
            case "PICKIE" -> new Pickie(id, email, password, firstname, lastname);
            case "ADMIN" -> new Admin(id, email, password, firstname, lastname);
            default -> throw new NotImplementedException();
        };
    }
}
