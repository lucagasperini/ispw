package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.ExcludedGroup;
import com.pickyeaters.logic.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Optional<Ingredient> findIngredientByName(String ingredientName);
    Optional<Allergen> findAllergenByName(String allergenName);
    List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup);
    List<Allergen> findAllergenListByIngredientID(String ingredientID);
    List<Ingredient> findIngredientListByExcludedGroupID(String egID);
    //TODO: Is not out of scope?
    Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName);
    List<String> allIngredientName();
    List<String> allAllergenName();
    List<Allergen> findAllergenByIngredient(Ingredient ingredient);
}
