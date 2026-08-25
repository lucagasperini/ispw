package com.pickyeaters.logic.bean;

import java.util.List;

public class EatingPreferenceBean {
    List<String> dislikeIngredientList;
    List<String> allergenList;
    List<String> excludedGroupList;

    public EatingPreferenceBean(List<String> dislikeIngredientList,
                                List<String> allergenList, List<String> excludedGroupList) {
        this.dislikeIngredientList = dislikeIngredientList;
        this.allergenList = allergenList;
        this.excludedGroupList = excludedGroupList;
    }

    public List<String> getDislikeIngredientList() {
        return dislikeIngredientList;
    }

    public List<String> getAllergenList() {
        return allergenList;
    }

    public List<String> getExcludedGroupList() {
        return excludedGroupList;
    }
}
