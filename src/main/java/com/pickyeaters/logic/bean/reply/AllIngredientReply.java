package com.pickyeaters.logic.bean.reply;

import java.util.List;

public class AllIngredientReply {
    private final List<String> allIngredientList;
    public AllIngredientReply(List<String> allIngredientList) {
        this.allIngredientList = allIngredientList;
    }

    public List<String> getAllIngredientList() {
        return allIngredientList;
    }
}
