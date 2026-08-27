package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.exception.BeanInvalidValueException;
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
        setName(name);
        setDescription(description);
        setType(type);
        setIngredientList(ingredientList);
    }

    public DishBean(Dish dish) {
        setName(dish.getName());
        setDescription(dish.getDescription());
        setType(dish.getType());
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
        if(name.isEmpty()) {
            throw new BeanInvalidValueException("Dish must have a name");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        if(type.isEmpty()) {
            throw new BeanInvalidValueException("Dish must have a type");
        }
        this.type = type;
    }

    public void setIngredientList(List<DishIngredientBean> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void addIngredient(DishIngredientBean ingredient) {
        if(!containsIngredient(ingredient.getName())) {
            ingredientList.add(ingredient);
        } else {
            throw new BeanInvalidValueException("Duplicated ingredient!");
        }
    }

    public boolean containsIngredient(String ingredient) {
        for(DishIngredientBean i : ingredientList) {
            if(i.getName().equals(ingredient)) {
                return true;
            }
        }
        return false;
    }

}
