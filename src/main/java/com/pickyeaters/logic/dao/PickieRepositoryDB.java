package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
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
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
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
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    //----------------------- PRIVATE METHOD SECTION -----------------------//

    List<Ingredient> readDislikeIngredientList(String userID) throws DatabaseControllerException {
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
                logger.warn("readDislikeIngredientList: Skip invalid element on database ");
            }
        }
        query.close();

        return ingredientList;
    }

    List<Allergen> readAllergenListUser(String userID) throws DatabaseControllerException {
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
                logger.warn("readAllergenListUser: Skip invalid element on database ");
            }
        }
        query.close();

        return allergenList;
    }


    List<ExcludedGroup> readExcludedGroupListUser(String userID) throws DatabaseControllerException{
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
                logger.warn("readExcludedGroupListUser: Skip invalid element on database ");
            }
        }
        query.close();

        return excludedGroupList;
    }

    private void createDislikeIngredient(String userID, Ingredient ingredient) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_ExcludedIngredient\" (fk_user, fk_ingredient) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(ingredient.getID());
        query.execute();
        query.close();
    }

    private void createAllergenListUser(String userID, Allergen allergen) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_Allergen\" (fk_user, fk_allergen) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(allergen.getID());
        query.execute();
        query.close();
    }


    private void createExcludedGroupListUser(String userID, ExcludedGroup excludedGroup) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "INSERT INTO \"User_ExcludedGroup\" (fk_user, fk_excluded_group) VALUES (?::uuid, ?::uuid)"
        );
        query.setString(userID);
        query.setString(excludedGroup.getID());
        query.execute();
        query.close();
    }

    private void deleteAllUserDislikeIngredient(String userID) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_ExcludedIngredient\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

    private void deleteAllUserAllergen(String userID) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_Allergen\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

    private void deleteAllUserExcludedGroup(String userID) throws DatabaseControllerException {
        DatabaseController.Query query = database.query(
                "DELETE FROM \"User_ExcludedGroup\" WHERE fk_user = ?::uuid"
        );
        query.setString(userID);
        query.execute();
        query.close();
    }

}
