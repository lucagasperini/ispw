package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.model.Allergen;
import com.pickyeaters.logic.model.ExcludedGroup;
import com.pickyeaters.logic.model.Ingredient;

import java.util.List;
import java.util.Optional;

public class IngredientRepositoryFS implements IngredientRepository {

    public Optional<Ingredient> findIngredient(DishIngredientBean ingredientBean) {
        throw new NotImplementedException();
    }

    public Optional<Ingredient> findIngredient(String ingredientName) {
        throw new NotImplementedException();
    }

    public List<Ingredient> findIngredientList(List<DishIngredientBean> ingredientBeanList) {
        throw new NotImplementedException();
    }

    public Optional<Allergen> findAllergen(String allergenName) {
        throw new NotImplementedException();
    }

    public List<Ingredient> findIngredientListByExcludedGroup(ExcludedGroup excludedGroup) {
        throw new NotImplementedException();
    }

    public List<Allergen> findAllergenListByIngredientID(String ingredientID) {
        throw new NotImplementedException();
    }

    public List<Ingredient> findIngredientListByExcludedGroupID(String excludedGroupID) {
        throw new NotImplementedException();
    }


    public Optional<ExcludedGroup> findExcludedGroupByName(String excludedGroupName) {
        throw new NotImplementedException();
    }

    public List<String> allIngredientName() {
        throw new NotImplementedException();
    }

    public List<String> allAllergenName() {
        throw new NotImplementedException();
    }

    public List<Allergen> findAllergenByIngredient(Ingredient ingredient) {
        throw new NotImplementedException();
    }
}
