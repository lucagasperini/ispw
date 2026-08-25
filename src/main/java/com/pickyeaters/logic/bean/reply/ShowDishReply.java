package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.bean.DishAllergenBean;
import com.pickyeaters.logic.bean.DishBean;
import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.bean.request.Request;

import java.util.List;

public class ShowDishReply {

    private final List<DishAllergenBean> allergenList;

    private final DishBean dish;

    public ShowDishReply(DishBean dish, List<DishAllergenBean> allergenList) {
        this.dish = dish;
        this.allergenList = List.copyOf(allergenList);
    }

    public DishBean getDish() {
        return dish;
    }

    /**
     *
     * @return immutable list of allergens
     */
    public List<DishAllergenBean> getAllergenList() {
        return allergenList;
    }

}
