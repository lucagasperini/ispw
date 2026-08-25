package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.*;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PickieRepositoryDB implements PickieRepository {
    private final Logger logger;
    private final DatabaseController database;
    private final IngredientRepository ingredientRepository;

    public PickieRepositoryDB(Logger logger, DatabaseController database, IngredientRepository ingredientRepository) {
        this.logger = logger;
        this.database = database;
        this.ingredientRepository = ingredientRepository;
    }

    List<Ingredient> readDislikeIngredientList(String userID) {
        List<Ingredient> ingredientList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"User_ExcludedIngredient\" JOIN \"Ingredient\" AS i ON fk_ingredient=i.id WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();

        while (query.next()) {
            try {
                String id = query.getString().orElseThrow();
                String name = query.getString().orElseThrow();
                List<Allergen> allergenList = ingredientRepository.findAllergenListByIngredientID(id);
                ingredientList.add(new Ingredient(
                        id,
                        name,
                        allergenList,
                        false,
                        false
                ));
            } catch (NoSuchElementException ignored) {

            }
        }
        query.close();

        return ingredientList;
    }

    List<Allergen> readAllergenListUser(String userID) {
        List<Allergen> allergenList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"User_Allergen\" JOIN \"Allergen\" AS a ON fk_allergen=a.id WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();

        while (query.next()) {
            try {
                allergenList.add(new Allergen(
                        query.getString().orElseThrow(),
                        query.getString().orElseThrow()
                ));
            } catch (NoSuchElementException ignored) {

            }
        }
        query.close();

        return allergenList;
    }


    List<ExcludedGroup> readExcludedGroupListUser(String userID) {
        List<ExcludedGroup> excludedGroupList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"User_ExcludedGroup\" JOIN \"ExcludedGroup\" AS a ON fk_excluded_group=a.id WHERE fk_user = ?::uuid;"
        );
        query.setString(userID);
        query.execute();


        while (query.next()) {
            try {
                String id = query.getString().orElseThrow();
                String name = query.getString().orElseThrow();
                List<Ingredient> ingredientList = ingredientRepository.findIngredientListByExcludedGroupID(id);
                excludedGroupList.add(new ExcludedGroup(
                        id,
                        name,
                        ingredientList
                ));
            } catch (NoSuchElementException ignored){

            }
        }
        query.close();

        return excludedGroupList;
    }

    public Optional<EatingPreference> findEatingPreference(String userID) {
        try {
            List<Ingredient> ingredientList = readDislikeIngredientList(userID);
            List<Allergen> allergenList = readAllergenListUser(userID);
            List<ExcludedGroup> excludedGroupList = readExcludedGroupListUser(userID);

            return Optional.of(new EatingPreference(
                    ingredientList,
                    excludedGroupList,
                    allergenList
            ));

        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }
    }


    void createDislikeIngredient(String userID, Ingredient ingredient) {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_ExcludedIngredient\" (fk_user, fk_ingredient) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(ingredient.getID());
        query.execute();
        query.close();
    }

    void createAllergenListUser(String userID, Allergen allergen) {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_Allergen\" (fk_user, fk_allergen) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(allergen.getID());
        query.execute();
        query.close();
    }


    void createExcludedGroupListUser(String userID, ExcludedGroup excludedGroup) {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_ExcludedGroup\" (fk_user, fk_excluded_group) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(excludedGroup.getID());
        query.execute();
        query.close();
    }

    void deleteAllUserDislikeIngredient(String userID) {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_ExcludedIngredient\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

    void deleteAllUserAllergen(String userID) {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_Allergen\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

    void deleteAllUserExcludedGroup(String userID) {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_ExcludedGroup\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

    public void editEatingPreference(String userID, EatingPreference eatingPreference) {
        try {
            deleteAllUserAllergen(userID);
            deleteAllUserDislikeIngredient(userID);
            deleteAllUserExcludedGroup(userID);

            for(Allergen i : eatingPreference.getAllergenList()) {
                createAllergenListUser(userID, i);
            }

            for(Ingredient i : eatingPreference.getIngredientList()) {
                createDislikeIngredient(userID, i);
            }

            for(ExcludedGroup i : eatingPreference.getGroupList()) {
                createExcludedGroupListUser(userID, i);
            }

        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        }
    }
}
