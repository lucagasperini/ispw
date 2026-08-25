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
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public Optional<Allergen> findAllergenByName(String allergenName) {
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
        } catch (DatabaseControllerException e) {
            throw new GenericRepositoryException(e.getMessage());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup) {
        return findIngredientListByExcludedGroupID(excludedGroup.getID());
    }

    public List<Allergen> findAllergenListByIngredientID(String ingredientID) {
        try {
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
        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        }
    }

    public List<Ingredient> findIngredientListByExcludedGroupID(String egID) {
        try {
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
                    logger.warn("findIngredientListByExcludedGroupID: Skip invalid element on database ");
                }
            }
            query.close();

            return ingredientList;
        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        }
    }

    public Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName) {
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

        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public List<String> allIngredientName() {
        try {
            List<String> outList = new ArrayList<>();
            DatabaseController.Query query = database.queryResultSet("SELECT name FROM \"Ingredient\"");
            query.execute();

            while(query.next()) {
                try {
                    outList.add(query.getString().orElseThrow());
                } catch (NoSuchElementException ignored) {
                    logger.warn("allIngredientName: Skip invalid element on database ");
                }
            }
            query.close();

            return outList;
        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        }
    }

    public List<String> allAllergenName() {
        try {
            List<String> outList = new ArrayList<>();
            DatabaseController.Query query = database.queryResultSet("SELECT name FROM \"Allergen\"");
            query.execute();

            while (query.next()) {
                try {
                    outList.add(query.getString().orElseThrow());
                } catch (NoSuchElementException ignored) {
                    logger.warn("allAllergenName: Skip invalid element on database ");
                }
            }
            query.close();

            return outList;
        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        }
    }

    public List<Allergen> findAllergenByIngredient(Ingredient ingredient) {
        try {
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
                    logger.warn("findAllergenByIngredient: Skip invalid element on database ");
                }
            }
            query.close();

            return outList;
        } catch (DatabaseControllerException ex) {
            throw new GenericRepositoryException(ex.getMessage());
        }
    }

}
