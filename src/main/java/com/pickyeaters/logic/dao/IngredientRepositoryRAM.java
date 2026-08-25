package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.exception.GenericRepositoryException;
import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.ExcludedGroup;
import com.pickyeaters.logic.model.Ingredient;
import com.pickyeaters.logic.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientRepositoryRAM implements IngredientRepository {
    private Logger logger;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<ExcludedGroup> excludedGroupList = new ArrayList<>();
    private List<Allergen> allergenList = new ArrayList<>();

    public IngredientRepositoryRAM(Logger logger) {
        this.logger = logger;

        Allergen a1 = new Allergen("1", "Allergen 1");
        Allergen a2 = new Allergen("2", "Allergen 2");
        Allergen a3 = new Allergen("3", "Allergen 3");
        Allergen a4 = new Allergen("4", "Allergen 4");

        allergenList.add(a1);
        allergenList.add(a2);
        allergenList.add(a3);
        allergenList.add(a4);

        List<Allergen> la1 = new ArrayList<>();
        la1.add(a1);
        la1.add(a2);
        List<Allergen> la2 = new ArrayList<>();
        la2.add(a3);
        la2.add(a4);
        List<Allergen> la3 = new ArrayList<>();
        la3.add(a1);
        la3.add(a4);
        List<Allergen> la4 = new ArrayList<>();
        la4.add(a3);
        List<Allergen> la5 = new ArrayList<>();

        Ingredient i1 = new Ingredient("1", "Ingredient 1", la1, false, false);
        Ingredient i2 = new Ingredient("2", "Ingredient 2", la2, true, true);
        Ingredient i3 = new Ingredient("3", "Ingredient 3", la3, false, false);
        Ingredient i4 = new Ingredient("4", "Ingredient 4", la4, true, false);
        Ingredient i5 = new Ingredient("5", "Ingredient 5", la5, false, true);

        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i4);
        ingredientList.add(i5);

        List<Ingredient> li1 = new ArrayList<>();
        li1.add(i2);
        List<Ingredient> li2 = new ArrayList<>();
        li1.add(i5);

        excludedGroupList.add(new ExcludedGroup("1", ExcludedGroup.GROUP_NAME_PREGNANT, li1));
        excludedGroupList.add(new ExcludedGroup("2", ExcludedGroup.GROUP_NAME_HALAL, li2));
        excludedGroupList.add(new ExcludedGroup("3", ExcludedGroup.GROUP_NAME_CARNIVORE, li2));
        excludedGroupList.add(new ExcludedGroup("4", ExcludedGroup.GROUP_NAME_PESCATARIAN, li2));
        excludedGroupList.add(new ExcludedGroup("5", ExcludedGroup.GROUP_NAME_VEGETARIAN, li2));
        excludedGroupList.add(new ExcludedGroup("6", ExcludedGroup.GROUP_NAME_KOSHER, li2));
        excludedGroupList.add(new ExcludedGroup("7", ExcludedGroup.GROUP_NAME_VEGAN, li2));
    }

    public Optional<Ingredient> findIngredientByName(String ingredientName) {
        for(Ingredient i : ingredientList) {
            if(i.getName().equals(ingredientName)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Optional<Allergen> findAllergenByName(String allergenName) {
        for(Allergen i : allergenList) {
            if(i.getName().equals(allergenName)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup) {
        return findExcludedGroupByName(excludedGroup.getName()).orElseThrow().getIngredientList();
    }

    @Override
    public List<Allergen> findAllergenListByIngredientID(String ingredientID) {
        for(Ingredient i : ingredientList) {
            if(i.getID().equals(ingredientID)) {
                return i.getAllergenList();
            }
        }
        GenericRepositoryException e = new GenericRepositoryException("Cannot find ingredient: " + ingredientID);
        logger.error(e.getMessage(), e);
        throw e;
    }

    @Override
    public List<Ingredient> findIngredientListByExcludedGroupID(String egID) {
        for(ExcludedGroup i : excludedGroupList) {
            if(i.getID().equals(egID)) {
                return i.getIngredientList();
            }
        }
        GenericRepositoryException e = new GenericRepositoryException("Cannot find Excluded Group: " + egID);
        logger.error(e.getMessage(), e);
        throw e;
    }


    public Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName) {
        for(ExcludedGroup i : excludedGroupList) {
            if(i.getName().equals(excludedGroupName)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public List<String> allIngredientName() {
        List<String> ingredientNameList = new ArrayList<>();
        for(Ingredient i : ingredientList) {
            ingredientNameList.add(i.getName());
        }
        return ingredientNameList;
    }

    public List<String> allAllergenName() {
        List<String> allergenNameList = new ArrayList<>();
        for(Allergen i : allergenList) {
            allergenNameList.add(i.getName());
        }
        return allergenNameList;
    }

    public List<Allergen> findAllergenByIngredient(Ingredient ingredient) {
        return findAllergenListByIngredientID(ingredient.getID());
    }
}
