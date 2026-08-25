package com.pickyeaters.logic.view.eatingpreference;

import com.pickyeaters.logic.bean.EatingPreferenceBean;
import com.pickyeaters.logic.bean.reply.ShowEatingPreferenceReply;
import com.pickyeaters.logic.bean.request.AllAllergenRequest;
import com.pickyeaters.logic.bean.request.AllIngredientRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowEatingPreferenceRequest;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.controller.PickieController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;
import com.pickyeaters.logic.view.VirtualView;

import java.util.List;

abstract class EatingPrefererenceView extends VirtualView {
    protected final PickieController pickieController;
    protected final EatingPreferenceBean eatingPreference;
    protected final MenuController menuController;
    protected EatingPrefererenceView(Request baseRequest, PickieController pickieController, MenuController menuController) {
        super(baseRequest);
        this.pickieController = pickieController;
        this.menuController = menuController;

        ShowEatingPreferenceRequest request = new ShowEatingPreferenceRequest(baseRequest);
        ShowEatingPreferenceReply reply = pickieController.showEatingPreference(request).getValue();

        eatingPreference = reply.getEatingPreference();
    }

    public List<String> showExcludedIngredientList() {
        return eatingPreference.getDislikeIngredientList();
    }

    public List<String> showExcludedGroupList() {
        return eatingPreference.getExcludedGroupList();
    }

    public List<String> showAllergenList() {
        return eatingPreference.getAllergenList();
    }

    public List<String> showAllAllergen() {
        try {
            return pickieController.allAllergen(
                    new AllAllergenRequest(baseRequest)
            ).getValue().getAllergenList();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }

    public List<String> showAllIngredient() {
        try {
            return menuController.allIngredient(
                    new AllIngredientRequest(baseRequest)
            ).getValue().getAllIngredientList();

        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }

    public boolean isPregnant() {
        return eatingPreference.getExcludedGroupList().contains("PREGNANT");
    }

    public boolean isCarnivore() {
        return eatingPreference.getExcludedGroupList().contains("CARNIVORE");
    }

    public boolean isPescatarian() {
        return eatingPreference.getExcludedGroupList().contains("PESCATARIAN");
    }

    public boolean isVegetarian() {
        return eatingPreference.getExcludedGroupList().contains("VEGETARIAN");
    }

    public boolean isHalal() {
        return eatingPreference.getExcludedGroupList().contains("HALAL");
    }

    public boolean isKosher() {
        return eatingPreference.getExcludedGroupList().contains("KOSHER");
    }

    public boolean isVegan() {
        return eatingPreference.getExcludedGroupList().contains("VEGAN");
    }

}
