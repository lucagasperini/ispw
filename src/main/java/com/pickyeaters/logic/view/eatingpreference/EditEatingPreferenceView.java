package com.pickyeaters.logic.view.eatingpreference;

import com.pickyeaters.logic.bean.reply.EditEatingPreferenceReply;
import com.pickyeaters.logic.bean.request.EditEatingPreferenceRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.controller.PickieController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

public class EditEatingPreferenceView extends EatingPrefererenceView {
    public EditEatingPreferenceView(Request baseRequest, PickieController pickieController, MenuController menuController) {
        super(baseRequest, pickieController, menuController);
    }

    public void addDislikeIngredient(String ingredient) {
        eatingPreference.getDislikeIngredientList().add(ingredient);
        notifyAllObserver();
    }

    public void addExcludedGroup(String excludedGroup) {
        eatingPreference.getExcludedGroupList().add(excludedGroup);
        notifyAllObserver();
    }

    public void addAllergen(String allergen) {
        eatingPreference.getAllergenList().add(allergen);
        notifyAllObserver();
    }

    public void removeDislikeIngredient(String ingredient) {
        eatingPreference.getDislikeIngredientList().remove(ingredient);
        notifyAllObserver();
    }

    public void removeExcludedGroup(String excludedGroup) {
        eatingPreference.getExcludedGroupList().remove(excludedGroup);
        notifyAllObserver();
    }

    public void removeAllergen(String allergen) {
        eatingPreference.getAllergenList().remove(allergen);
        notifyAllObserver();
    }

    public void submit() {
        try {
            EditEatingPreferenceRequest request = new EditEatingPreferenceRequest(baseRequest, eatingPreference);
            pickieController.editEatingPreference(request).getValue();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }


    public void togglePregnant() {
        if(isPregnant()) {
            eatingPreference.getExcludedGroupList().remove("PREGNANT");
        } else {
            eatingPreference.getExcludedGroupList().add("PREGNANT");
        }
    }

    public void toggleCarnivore() {
        if(isCarnivore()) {
            eatingPreference.getExcludedGroupList().remove("CARNIVORE");
        } else {
            eatingPreference.getExcludedGroupList().add("CARNIVORE");
        }
    }

    public void togglePescatarian() {
        if(isPescatarian()) {
            eatingPreference.getExcludedGroupList().remove("PESCATARIAN");
        } else {
            eatingPreference.getExcludedGroupList().add("PESCATARIAN");
        }
    }

    public void toggleVegetarian() {
        if(isVegetarian()) {
            eatingPreference.getExcludedGroupList().remove("VEGETARIAN");
        } else {
            eatingPreference.getExcludedGroupList().add("VEGETARIAN");
        }
    }

    public void toggleHalal() {
        if(isHalal()) {
            eatingPreference.getExcludedGroupList().remove("HALAL");
        } else {
            eatingPreference.getExcludedGroupList().add("HALAL");
        }
    }

    public void toggleKosher() {
        if(isKosher()) {
            eatingPreference.getExcludedGroupList().remove("KOSHER");
        } else {
            eatingPreference.getExcludedGroupList().add("KOSHER");
        }
    }

    public void toggleVegan() {
        if(isVegan()) {
            eatingPreference.getExcludedGroupList().remove("VEGAN");
        } else {
            eatingPreference.getExcludedGroupList().add("VEGAN");
        }
    }
}
