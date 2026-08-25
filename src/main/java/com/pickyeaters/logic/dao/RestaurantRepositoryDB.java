package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RestaurantRepositoryDB implements RestaurantRepository{
    private final Logger logger;
    private DatabaseController database;

    public RestaurantRepositoryDB(Logger logger, DatabaseController database) {
        this.logger = logger;
        this.database = database;
    }

    private Optional<String> readRestaurantIDFromUserID(String userID) {
        DatabaseController.Query query = database.query(
                "CALL get_restaurant_id_from_user(?,?)"
        );
        query.setString(userID);
        query.registerOutString();
        query.execute();
        Optional<String> restaurantID = query.getString();
        query.close();

        return restaurantID;
    }

    public Optional<Restaurant> findRestaurantByOwner(String restaurateurID) {
        try {
            String restaurantID = null;
            try {
                restaurantID = readRestaurantIDFromUserID(restaurateurID).orElseThrow();
            } catch (NoSuchElementException e) {
                throw new GenericRepositoryException("Cannot find restaurantID");
            }

            DatabaseController.Query query = database.query(
                    "CALL get_restaurant_by_owner(?,?,?,?,?)"
            );
            query.setString(restaurantID);
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.execute();
            String name = query.getString().orElseThrow();
            String phone = query.getString().orElseThrow();
            String address = query.getString().orElseThrow();
            String city = query.getString().orElseThrow();
            query.close();

            return Optional.of(new Restaurant(restaurantID, name, phone, address, city));
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public void editRestaurantByOwner(String restaurateurID, Restaurant restaurant) {
        try {
            String restaurantID = readRestaurantIDFromUserID(restaurateurID).orElseThrow();

            DatabaseController.Query query = database.query(
                    "UPDATE \"Restaurant\" set name=?, phone=?, address=?, city=? WHERE id = ?::uuid"
            );
            query.setString(restaurant.getName());
            query.setString(restaurant.getPhone());
            query.setString(restaurant.getAddress());
            query.setString(restaurant.getCity());
            query.setString(restaurantID);
            query.execute();
            query.close();
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new GenericRepositoryException("Invalid restaurateurID provided: " + restaurateurID);
        }
    }

    public List<Restaurant> findRestaurantByCity(String city) {
        try {
            List<Restaurant> restaurantList = new ArrayList<>();
            DatabaseController.Query query = database.queryResultSet(
                    "SELECT id, name, phone, address FROM \"Restaurant\" WHERE city = ?;"
            );
            query.setString(city);

            query.execute();

            while (query.next()) {
                try {
                    restaurantList.add(new Restaurant(
                            query.getString().orElseThrow(),
                            query.getString().orElseThrow(),
                            query.getString().orElseThrow(),
                            query.getString().orElseThrow(),
                            city
                    ));
                } catch (NoSuchElementException ignored) {

                }
            }
            query.close();

            return restaurantList;
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<String> allCity() {
        try {
            List<String> cityList = new ArrayList<>();
            DatabaseController.Query query = database.queryResultSet(
                    "SELECT DISTINCT city FROM \"Restaurant\""
            );

            query.execute();

            while (query.next()) {
                try {
                    cityList.add(query.getString().orElseThrow());
                } catch (NoSuchElementException ignored) {

                }
            }
            query.close();

            return cityList;
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }
    }

}
