package com.pickyeaters.logic.model;

import java.util.List;

public class EatingPreference {
    private List<Ingredient> ingredientList;
    private List<ExcludedGroup> groupList;
    private List<Allergen> allergenList;

    public EatingPreference(List<Ingredient> ingredientList, List<ExcludedGroup> groupList, List<Allergen> allergyList) {
        setIngredientList(ingredientList);
        setGroupList(groupList);
        setAllergyList(allergyList);
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Allergen> getAllergenList() {
        return allergenList;
    }

    public List<ExcludedGroup> getGroupList() {
        return groupList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setAllergyList(List<Allergen> allergenList) {
        this.allergenList = allergenList;
    }

    public void setGroupList(List<ExcludedGroup> groupList) {
        this.groupList = groupList;
    }

    private static boolean checkListHasIngredient(List<Ingredient> ingredientList, Ingredient ingredient) {
        for (Ingredient i : ingredientList) {
            if(i.getID().equals(ingredient.getID())) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkListHasAllergen(List<Allergen> allergenList, Allergen allergen) {
        for (Allergen i : allergenList) {
            if(i.getID().equals(allergen.getID())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDish(Dish dish) {
        for(Ingredient i : dish.getIngredientList()) {
            if(checkListHasIngredient(ingredientList, i)) {
                return false;
            }
            for(ExcludedGroup e : groupList) {
                if(checkListHasIngredient(e.getIngredientList(),i)) {
                    return false;
                }
            }
            for(Allergen a : i.getAllergenList()) {
                if (checkListHasAllergen(allergenList, a)) {
                    return false;
                }
            }
        }
        return true;
    }
}
