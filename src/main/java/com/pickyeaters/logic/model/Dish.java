package com.pickyeaters.logic.model;

import java.util.List;

public abstract class Dish {
    public static final String TYPE_APPETIZER = "APPETIZER";
    public static final String TYPE_DRINK = "DRINK";
    public static final String TYPE_FIRST = "FIRST";
    public static final String TYPE_SECOND = "SECOND";
    public static final String TYPE_CONTOUR = "CONTOUR";
    public static final String TYPE_DESSERT = "DESSERT";

    private String id;
    private String name;
    private String description;
    private final List<Ingredient> ingredientList;


    protected Dish(String id, String name, String description, List<Ingredient> ingredientList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredientList = List.copyOf(ingredientList);
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public abstract String getType();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTypeAppetizer() {
        return getType().equals(Dish.TYPE_APPETIZER);
    }

    public boolean isTypeContour() {
        return getType().equals(Dish.TYPE_CONTOUR);
    }

    public boolean isTypeDessert() {
        return getType().equals(Dish.TYPE_DESSERT);
    }

    public boolean isTypeDrink() {
        return getType().equals(Dish.TYPE_DRINK);
    }

    public boolean isTypeFirst() {
        return getType().equals(Dish.TYPE_FIRST);
    }

    public boolean isTypeSecond() {
        return getType().equals(Dish.TYPE_SECOND);
    }
}
