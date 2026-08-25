package com.pickyeaters.logic.bean.request;

public class ShowAllergenIngredientRequest extends Request {
    private final String ingredientName;
    public ShowAllergenIngredientRequest(Request request, String ingredientName) {
        super(request);
        this.ingredientName = ingredientName;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
