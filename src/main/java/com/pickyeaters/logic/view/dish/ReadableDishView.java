package com.pickyeaters.logic.view.dish;

import com.pickyeaters.logic.bean.DishAllergenBean;
import com.pickyeaters.logic.bean.DishBean;
import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.IngredientView;
import com.pickyeaters.logic.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

abstract class ReadableDishView extends VirtualView {

    protected final MenuController controller;

    protected DishBean dish;
    protected List<DishAllergenBean> allergenList = new ArrayList<>();

    public ReadableDishView(Request baseRequest, MenuController menuController) {
        super(baseRequest);

        this.controller = menuController;
    }

    public String showName() {
        return dish.getName();
    }

    public String showDescription() {
        return dish.getDescription();
    }

    public String showType() {
        if(dish.getType().isEmpty()) {
            return "";
        } else {
            return i18n("DISH_TYPE_" + dish.getType());
        }
    }

    public IngredientView displayIngredientView(String ingredientName) throws GenericViewException {
        for(DishIngredientBean i: dish.getIngredientList()) {
            if(i.getName().equals(ingredientName)) {
                return new IngredientView(baseRequest, i);
            }
        }
        throw new GenericViewException("Ingredient " + ingredientName + " is not present on dish " + dish.getName(), "");
    }

    public List<String> showIngredientNameList() {
        List<String> out = new ArrayList<>();
        for(DishIngredientBean i: dish.getIngredientList()) {
            out.add(i.getName());
        }
        return out;
    }

    public List<String> showAllergenList() {
        List<String> out = new ArrayList<>();
        for(DishAllergenBean i: allergenList) {
            out.add(i.getName());
        }
        return out;
    }
}
