package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.bean.request.Request;

public class IngredientView extends VirtualView{
    private final DishIngredientBean ingredient;
    public IngredientView(Request baseRequest, DishIngredientBean ingredient) {
        super(baseRequest);
        this.ingredient = ingredient;
    }

    public String showName() {
        return ingredient.getName();
    }

    public boolean showIsCooked() {
        return ingredient.isCooked();
    }

    public boolean showIsOptional() {
        return ingredient.isOptional();
    }

    public void insertName(String name) {
        ingredient.setName(name);
    }

    public void toggleIsOptional() {
        ingredient.setOptional(!ingredient.isOptional());
    }

    public void toggleIsCooked() {
        ingredient.setCooked(!ingredient.isCooked());
    }

    public String showFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(ingredient.getName());
        if (ingredient.isCooked()) {
            sb.append(",");
            sb.append(i18n("PICKY_ADDDISLIKEDINGREDIENT_COOKINGMETHOD_COOKED"));
        }
        if (ingredient.isOptional()) {
            sb.append(",");
            sb.append(i18n("PICKY_ADDDISLIKEDINGREDIENT_OPTIONAL"));
        }
        return sb.toString();
    }
}
