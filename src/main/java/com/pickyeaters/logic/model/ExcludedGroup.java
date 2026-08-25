package com.pickyeaters.logic.model;

import java.util.List;

public class ExcludedGroup {
    private String id;
    private String name;
    private List<Ingredient> ingredientList;

    public ExcludedGroup(String id, String name, List<Ingredient> ingredientList) {
        setID(id);
        setName(name);
        setIngredientList(ingredientList);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
