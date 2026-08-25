package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.model.Dish;
import com.pickyeaters.logic.model.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class DishBean {
    private String name;
    private String description;
    private String type;
    private List<DishIngredientBean> ingredientList;

    public DishBean() {
        name = "";
        description = "";
        type = "";
        ingredientList = new ArrayList<>();
    }

    public DishBean(String name, String description, String type, List<DishIngredientBean> ingredientList) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.ingredientList = ingredientList;
    }

    public DishBean(Dish dish) {
        name = dish.getName();
        description = dish.getDescription();
        type = dish.getType();
        ingredientList = new ArrayList<>();
        for(Ingredient i : dish.getIngredientList()) {
            ingredientList.add(new DishIngredientBean(i.getName(), i.isOptional(), i.isCooked()));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public List<DishIngredientBean> getIngredientList() {
        return ingredientList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIngredientList(List<DishIngredientBean> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
