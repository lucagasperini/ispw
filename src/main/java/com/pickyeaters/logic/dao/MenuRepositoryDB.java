package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.factory.DishFactory;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MenuRepositoryDB implements MenuRepository {
    private final Logger logger;
    private final DatabaseController database;
    private final DishFactory dishFactory;
    private final IngredientRepository ingredientRepository;

    public MenuRepositoryDB(Logger logger, DatabaseController database, IngredientRepository ingredientRepository, DishFactory dishFactory) {
        this.logger = logger;
        this.database = database;
        this.ingredientRepository = ingredientRepository;
        this.dishFactory = dishFactory;
    }

    public List<Dish> findMenuByRestaurantID(String restaurantID) {
        try {
            return readSetMenuByRestaurantID(restaurantID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public void addDish(String restaurantID, Dish dish) {
        try {
            createDish(restaurantID, dish);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public void removeDish(String dishID) {
        try {
            deleteDishIngredient(dishID);
            deleteDish(dishID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public Optional<Dish> findDishByID(String dishID) {
        try {
            return readDishByID(dishID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public void editDish(Dish dish) {
        try {
            updateDish(dish);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }
    ///////////////////////////////////////////////// PRIVATE METHOD ////////////////////////////////////////////////////////////////

    private List<Ingredient> readSetIngredientByDishID(String dishID) throws DatabaseControllerException {
        List<Ingredient> ingredientList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name, cooked, optional FROM \"Dish_Ingredient\" JOIN \"Ingredient\" AS i ON fk_ingredient=i.id WHERE fk_dish=?::uuid"
        );
        query.setString(dishID);

        query.execute();

        while (query.next()) {
            try {
                String ingredientID = query.getString().orElseThrow();
                String ingredientName = query.getString().orElseThrow();
                boolean isCooked = query.getBoolean();
                boolean isOptional = query.getBoolean();

                List<Allergen> allergenList = ingredientRepository.findAllergenListByIngredientID(ingredientID);
                ingredientList.add(new Ingredient(
                        ingredientID,
                        ingredientName,
                        allergenList,
                        isCooked,
                        isOptional
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("readIngredientListByDishID: Skip invalid element on database ");
            }
        }
        query.close();

        return ingredientList;
    }


    private List<Dish> readSetMenuByRestaurantID(String restaurantID) throws DatabaseControllerException {
        List<Dish> dishList = new ArrayList<>();

        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name, description, type FROM \"Dish\" WHERE fk_restaurant = ?::uuid"
        );
        query.setString(restaurantID);

        query.execute();

        while(query.next()) {
            try {
                String dishID = query.getString().orElseThrow();
                String dishName = query.getString().orElseThrow();
                String dishDescription = query.getString().orElseThrow();
                String dishType = query.getString().orElseThrow();

                List<Ingredient> ingredientList = readSetIngredientByDishID(dishID);

                dishList.add(dishFactory.createDish(
                        dishID,
                        dishName,
                        dishDescription,
                        dishType,
                        ingredientList
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("findMenuByRestaurantID: Skip invalid element on database ");
            }
        }
        query.close();

        return dishList;
    }

    private void createDishIngredient(String dishID, Ingredient ingredient) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"Dish_Ingredient\" (fk_ingredient, fk_dish, cooked, optional) VALUES (?::uuid, ?::uuid, ?, ?)"
        );
        query.setString(ingredient.getID());
        query.setString(dishID);
        query.setBoolean(ingredient.isCooked());
        query.setBoolean(ingredient.isOptional());
        query.execute();
        query.close();
    }

    private Optional<String> readDishID(String restaurantID, String dishName) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "CALL get_dish_id_by_name(?,?,?)"
        );
        query.setString(restaurantID);
        query.setString(dishName);
        query.registerOutString();

        query.execute();
        Optional<String> dishID = query.getString();
        query.close();
        return dishID;
    }

    private void createDish(String restaurantID, Dish dish) throws DatabaseControllerException {
        try {
            DatabaseController.Query query = database.query(
                    "INSERT INTO \"Dish\" (name, description, type, fk_restaurant) VALUES (?, ?, ?, ?::uuid)"
            );
            query.setString(dish.getName());
            query.setString(dish.getDescription());
            query.setString(dish.getType());
            query.setString(restaurantID);
            query.execute();
            query.close();

            String dishID = readDishID(restaurantID, dish.getName()).orElseThrow();
            for (Ingredient i : dish.getIngredientList()) {
                createDishIngredient(dishID, i);
            }
        } catch (NoSuchElementException e) {
            throw new GenericRepositoryException("Cannot read dish id");
        }
    }


    private void deleteDishIngredient(String dishID) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"Dish_Ingredient\" WHERE fk_dish = ?::uuid"
        );

        query.setString(dishID);
        query.execute();
        query.close();
    }


    private void deleteDish(String dishID) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"Dish\" WHERE id = ?::uuid"
        );

        query.setString(dishID);
        query.execute();
        query.close();
    }

    private Optional<Dish> readDishByID(String dishID) throws DatabaseControllerException{
        try{
            DatabaseController.Query query = database.query(
                    "CALL get_dish_by_id(?,?,?,?)"
            );
            query.setString(dishID);
            query.registerOutString();
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String dishName = query.getString().orElseThrow();
            String dishDescription = query.getString().orElseThrow();
            String dishType = query.getString().orElseThrow();

            List<Ingredient> ingredientList = readSetIngredientByDishID(dishID);

            Dish dish = dishFactory.createDish(
                    dishID,
                    dishName,
                    dishDescription,
                    dishType,
                    ingredientList
            );

            query.close();

            return Optional.ofNullable(dish);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void updateDish(Dish dish) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "UPDATE \"Dish\" SET name=?, description=?, type=? WHERE id=?::uuid"
        );
        query.setString(dish.getName());
        query.setString(dish.getDescription());
        query.setString(dish.getType());
        query.setString(dish.getID());
        query.execute();

        deleteDishIngredient(dish.getID());
        for (Ingredient i : dish.getIngredientList()) {
            createDishIngredient(dish.getID(), i);
        }
    }
}
