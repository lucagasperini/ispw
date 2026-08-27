package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.Dish;
import com.pickyeaters.logic.model.Restaurant;
import com.pickyeaters.logic.model.Restaurateur;
import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RestaurantRepositoryDB implements RestaurantRepository{
    private final Logger logger;
    private DatabaseController database;
    private UserRepository userRepository;
    private MenuRepository menuRepository;

    public RestaurantRepositoryDB(Logger logger, DatabaseController database, UserRepository userRepository, MenuRepository menuRepository) {
        this.logger = logger;
        this.database = database;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    public Optional<Restaurant> findRestaurantByOwner(String userID) {
        try {
            return readRestaurantByOwner(userID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public void editRestaurantByOwner(String restaurateurID, Restaurant restaurant) {
        try {
            updateRestaurantByOwner(restaurateurID, restaurant);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<Restaurant> findRestaurantByCity(String city) {
        try {
            return readSetRestaurantByCity(city);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<String> allCity() {
        try {
            return readSetAllCity();
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    //----------------------- PRIVATE METHOD SECTION -----------------------//


    public Optional<Restaurant> readRestaurantByOwner(String userID) throws DatabaseControllerException {
        try {
            User user = userRepository.getUserByID(userID).orElseThrow();
            if (!(user instanceof Restaurateur)) {
                GenericRepositoryException ex = new GenericRepositoryException("Cannot find restaurantID");
                logger.error(ex.getMessage(), ex);
                throw ex;
            }

            DatabaseController.Query query = database.query(
                    "CALL get_restaurant_by_owner_id(?,?,?,?,?,?)"
            );
            query.setString(userID);
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String restID = query.getString().orElseThrow();
            String restName = query.getString().orElseThrow();
            String restPhone = query.getString().orElseThrow();
            String restAddress = query.getString().orElseThrow();
            String restCity = query.getString().orElseThrow();
            query.close();

            List<Dish> menu = menuRepository.findMenuByRestaurantID(restID);

            return Optional.of(new Restaurant(
                    restID,
                    restName,
                    restPhone,
                    restAddress,
                    restCity,
                    (Restaurateur) user,
                    menu
            ));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void updateRestaurantByOwner(String restaurateurID, Restaurant restaurant) throws DatabaseControllerException {
        try {
            String restaurantID = findRestaurantByOwner(restaurateurID).orElseThrow().getID();

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
        } catch (NoSuchElementException e) {
            throw new GenericRepositoryException("Invalid restaurateurID provided: " + restaurateurID);
        }
    }

    private List<Restaurant> readSetRestaurantByCity(String city) throws DatabaseControllerException {
        List<Restaurant> restaurantList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT r.id, r.name, r.phone, r.address, u.id, u.email, u.firstname, u.lastname FROM \"User\" AS u JOIN \"Restaurant\" AS r ON fk_restaurant=r.id WHERE city = ?;"
        );
        query.setString(city);

        query.execute();

        while (query.next()) {
            try {
                String restID = query.getString().orElseThrow();
                String restName = query.getString().orElseThrow();
                String restPhone = query.getString().orElseThrow();
                String restAddress = query.getString().orElseThrow();
                String userID = query.getString().orElseThrow();
                String userEmail = query.getString().orElseThrow();
                String userFirstname = query.getString().orElseThrow();
                String userLastname = query.getString().orElseThrow();

                restaurantList.add(new Restaurant(
                        restID,
                        restName,
                        restPhone,
                        restAddress,
                        city,
                        new Restaurateur(
                                userID,
                                userEmail,
                                "",
                                userFirstname,
                                userLastname
                        )
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("findRestaurantByCity: Skip invalid element on database ");
            }
        }
        query.close();

        return restaurantList;
    }

    private List<String> readSetAllCity() throws DatabaseControllerException {
        List<String> cityList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT DISTINCT city FROM \"Restaurant\""
        );

        query.execute();

        while (query.next()) {
            try {
                cityList.add(query.getString().orElseThrow());
            } catch (NoSuchElementException ignored) {
                logger.warn("allCity: Skip invalid element on database ");
            }
        }
        query.close();

        return cityList;
    }

}
