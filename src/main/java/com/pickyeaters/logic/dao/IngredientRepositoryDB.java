package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.controller.DatabaseController;
import com.pickyeaters.logic.exception.DatabaseControllerException;
import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.ExcludedGroup;
import com.pickyeaters.logic.model.Ingredient;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class IngredientRepositoryDB implements IngredientRepository {
    private final Logger logger;
    private final DatabaseController database;

    public IngredientRepositoryDB(Logger logger, DatabaseController database) {
        this.logger = logger;
        this.database = database;
    }

    public Optional<Ingredient> findIngredientByName(String ingredientName) {
        try {
            return readIngredientByName(ingredientName);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public Optional<Allergen> findAllergenByName(String allergenName) {
        try {
            return readAllergenByName(allergenName);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup) {
        return findIngredientListByExcludedGroupID(excludedGroup.getID());
    }

    public List<Allergen> findAllergenListByIngredientID(String ingredientID) {
        try {
            return readAllergenListByIngredientID(ingredientID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<Ingredient> findIngredientListByExcludedGroupID(String excludedGroupID) {
        try {
           return readSetIngredientByExcludedGroupID(excludedGroupID);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }


    public Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName) {
        try {
            return readExcludedGroupByName(excludedGroupName);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<String> allIngredientName() {
        try {
            return readSetAllIngredientName();
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<String> allAllergenName() {
        try {
            return readSetAllIngredientName();
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    public List<Allergen> findAllergenByIngredient(Ingredient ingredient) {
        try {
            return readSetAllergenByIngredient(ingredient);
        } catch (DatabaseControllerException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage());
        }
    }

    ///////////////////////////////////////////////// PRIVATE METHOD ////////////////////////////////////////////////////////////////

    private Optional<Ingredient> readIngredientByName(String ingredientName) throws DatabaseControllerException {
        try {
            DatabaseController.Query query = database.query("CALL get_ingredient_by_name(?,?,?)");
            query.setString(ingredientName);
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String id = query.getString().orElseThrow();
            String name = query.getString().orElseThrow();
            query.close();

            List<Allergen> allergenList = findAllergenListByIngredientID(id);
            return Optional.of(new Ingredient(id, name, allergenList, false, false));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private Optional<Allergen> readAllergenByName(String allergenName) throws DatabaseControllerException {
        try {
            DatabaseController.Query query = database.query("CALL get_allergen_by_name(?,?,?)");
            query.setString(allergenName);
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String id = query.getString().orElseThrow();
            String name = query.getString().orElseThrow();
            query.close();

            return Optional.of(new Allergen(id, name));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private Optional<ExcludedGroup> readExcludedGroupByName(String excludedGroupName) throws DatabaseControllerException {
        try {
            DatabaseController.Query query = database.query("CALL get_excluded_group_by_name(?,?,?)");
            query.setString(excludedGroupName);
            query.registerOutString();
            query.registerOutString();

            query.execute();
            String id = query.getString().orElseThrow();
            String name = query.getString().orElseThrow();
            query.close();

            List<Ingredient> ingredientList = findIngredientListByExcludedGroupID(id);
            return Optional.of(new ExcludedGroup(
                    id,
                    name,
                    ingredientList
            ));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }


    private List<Allergen> readAllergenListByIngredientID(String ingredientID) {
        List<Allergen> allergenList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"Ingredient_Allergen\" JOIN \"Allergen\" AS a ON fk_allergen=a.id WHERE fk_ingredient=?::uuid"
        );
        query.setString(ingredientID);

        query.execute();

        while (query.next()) {
            try {
                allergenList.add(new Allergen(
                        query.getString().orElseThrow(),
                        query.getString().orElseThrow()
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("findAllergenListByIngredientID: Skip invalid element on database ");
            }
        }
        query.close();

        return allergenList;
    }


    private List<Ingredient> readSetIngredientByExcludedGroupID(String egID) throws DatabaseControllerException {
        List<Ingredient> ingredientList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"ExcludedGroup_Ingredient\" JOIN \"Ingredient\" AS i ON fk_ingredient=i.id WHERE fk_excluded_group=?::uuid"
        );
        query.setString(egID);

        query.execute();

        while (query.next()) {
            try {
                String ingredientID = query.getString().orElseThrow();
                String ingredientName = query.getString().orElseThrow();
                List<Allergen> allergenList = findAllergenListByIngredientID(ingredientID);
                ingredientList.add(new Ingredient(
                        ingredientID,
                        ingredientName,
                        allergenList,
                        false,
                        false
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("readIngredientListByExcludedGroupID: Skip invalid element on database ");
            }
        }
        query.close();

        return ingredientList;
    }

    private List<String> readSetAllIngredientName() throws DatabaseControllerException {
        List<String> outList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet("SELECT name FROM \"Ingredient\"");
        query.execute();

        while (query.next()) {
            try {
                outList.add(query.getString().orElseThrow());
            } catch (NoSuchElementException ignored) {
                logger.warn("readAllIngredientName: Skip invalid element on database ");
            }
        }
        query.close();

        return outList;
    }

    private List<Allergen> readSetAllergenByIngredient(Ingredient ingredient) throws DatabaseControllerException {
        List<Allergen> outList = new ArrayList<>();
        DatabaseController.Query query = database.queryResultSet(
                "SELECT id, name FROM \"Ingredient_Allergen\" JOIN \"Allergen\" AS a ON fk_allergen=a.id WHERE fk_ingredient=?::uuid"
        );
        query.setString(ingredient.getID());

        query.execute();

        while (query.next()) {
            try {
                outList.add(new Allergen(
                        query.getString().orElseThrow(),
                        query.getString().orElseThrow()
                ));
            } catch (NoSuchElementException ignored) {
                logger.warn("readAllergenByIngredient: Skip invalid element on database ");
            }
        }
        query.close();

        return outList;
    }


}
