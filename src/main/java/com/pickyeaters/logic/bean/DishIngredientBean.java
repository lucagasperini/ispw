package com.pickyeaters.logic.bean;

public class DishIngredientBean {
    private String name;
    private boolean optional;
    private boolean cooked;

    public DishIngredientBean(String name, boolean optional, boolean cooked) {
        this.name = name;
        this.optional = optional;
        this.cooked = cooked;
    }

    public DishIngredientBean(String name) {
        this.name = name;
        this.optional = false;
        this.cooked = false;
    }

    public String getName() {
        return name;
    }

    public boolean isCooked() {
        return cooked;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
