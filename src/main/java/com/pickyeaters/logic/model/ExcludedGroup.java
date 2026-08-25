package com.pickyeaters.logic.model;

import java.util.ArrayList;
import java.util.List;

public class ExcludedGroup {
    private String ID;
    private String name;
    private List<Ingredient> ingredientList;

    public ExcludedGroup(String id, String name, List<Ingredient> ingredientList) {
        setID(id);
        setName(name);
        setIngredientList(ingredientList);
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
