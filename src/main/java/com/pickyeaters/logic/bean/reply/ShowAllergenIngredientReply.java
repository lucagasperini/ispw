package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.DishAllergenBean;

import java.util.List;

public class ShowAllergenIngredientReply {
    private final List<DishAllergenBean> allergenList;
    public ShowAllergenIngredientReply(List<DishAllergenBean> allergenList) {
        this.allergenList = allergenList;
    }

    public List<DishAllergenBean> getAllergenList() {
        return allergenList;
    }
}
