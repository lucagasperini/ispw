package com.pickyeaters.logic.bean;

import com.pickyeaters.logic.model.Allergen;

public class DishAllergenBean {
    private String name;

    public DishAllergenBean(String name) {
        this.name = name;
    }

    public DishAllergenBean(Allergen allergen) {
        this.name = allergen.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
