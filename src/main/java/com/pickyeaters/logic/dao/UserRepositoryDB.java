package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.factory.UserFactory;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;

public class UserRepositoryDB implements UserRepository {
    private final DatabaseController database;
    private final Logger logger;
    private final UserFactory userFactory;

    public UserRepositoryDB(Logger logger, DatabaseController database, UserFactory userFactory) {
        this.logger = logger;
        this.database = database;
        this.userFactory = userFactory;
    }

    public Optional<User> getUserByEmail(String email) {
        try {
            DatabaseController.Query query = database.query("CALL userinfo(?, ?, ?, ?, ?, ?)");
            query.setString(email);
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String id = query.getString().orElseThrow();
            String password = query.getString().orElseThrow();
            String type = query.getString().orElseThrow();
            String firstname = query.getString().orElseThrow();
            String lastname = query.getString().orElseThrow();

            query.close();


            return Optional.of(userFactory.createUser(
                    id, email, password, firstname, lastname, type
            ));
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByID(String id) {
        try {
            DatabaseController.Query query = database.query(
                    "CALL get_user_by_id(?,?,?,?,?,?)"
            );
            query.setString(id);
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String email = query.getString().orElseThrow();
            String password = query.getString().orElseThrow();
            String firstname = query.getString().orElseThrow();
            String lastname = query.getString().orElseThrow();
            String type = query.getString().orElseThrow();
            query.close();

            return Optional.of(userFactory.createUser(
                    id, email, password, firstname, lastname, type
            ));
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }  catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public void editUser(User user) {
        try {
            DatabaseController.Query query = database.query(
                    "UPDATE \"User\" SET email=?,firstname=?, lastname=? WHERE id=?::uuid"
            );
            query.setString(user.getEmail());
            query.setString(user.getFirstname());
            query.setString(user.getLastname());
            query.setString(user.getID());
            query.execute();
            query.close();
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }
    }
}
