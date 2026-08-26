package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.ExcludedGroup;
import com.pickyeaters.logic.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Optional<Ingredient> findIngredient(DishIngredientBean ingredientBean);
    Optional<Ingredient> findIngredient(String ingredientName);
    List<Ingredient> findIngredientList(List<DishIngredientBean> ingredientBeanList);
    Optional<Allergen> findAllergen(String allergenName);
    List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup);
    List<Allergen> findAllergenListByIngredientID(String ingredientID);
    List<Ingredient> findIngredientListByExcludedGroupID(String egID);
    // Is out of scope?
    Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName);
    List<String> allIngredientName();
    List<String> allAllergenName();
    List<Allergen> findAllergenByIngredient(Ingredient ingredient);
}
