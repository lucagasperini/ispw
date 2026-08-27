package com.pickyeaters.logic.view.dish;

import com.pickyeaters.logic.bean.DishAllergenBean;
import com.pickyeaters.logic.bean.DishIngredientBean;
import com.pickyeaters.logic.bean.request.AllIngredientRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowAllergenIngredientRequest;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.exception.BeanInvalidValueException;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

import java.util.ArrayList;
import java.util.List;

abstract class EditableDishView extends ReadableDishView {

    protected List<String> allIngredientList;
    protected List<String> allTypeList;
    protected EditableDishView(Request baseRequest, MenuController menuController) throws GenericViewException {
        super(baseRequest, menuController);

        allTypeList = new ArrayList<>();
        allTypeList.add("APPETIZER");
        allTypeList.add("DRINK");
        allTypeList.add("FIRST");
        allTypeList.add("SECOND");
        allTypeList.add("CONTOUR");
        allTypeList.add("DESSERT");

        try {
            allIngredientList = List.copyOf(menuController.allIngredient(
                    new AllIngredientRequest(baseRequest)
            ).getValue().getAllIngredientList());

        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FETCH_ALL_INGREDIENT");
        }
    }

    public void insertName(String name) {
        this.dish.setName(name);
        notifyAllObserver();
    }

    public void insertDescription(String description) {
        this.dish.setDescription(description);
        notifyAllObserver();
    }

    public void selectType(String type) throws GenericViewException {
        if(!allTypeList.contains(type)) {
            throw new GenericViewException("This type doesn't exist! Name: " + type, "INVALID_DISH_TYPE");
        }
        this.dish.setType(type);
        notifyAllObserver();
    }

    public void addIngredient(String ingredient) throws GenericViewException {
        if(!allIngredientList.contains(ingredient)) {
            throw new GenericViewException("This ingredient doesn't exist! Name: " + ingredient, "INVALID_INGREDIENT");
        }
        try {
            dish.addIngredient(new DishIngredientBean(ingredient));
            notifyAllObserver();
        } catch (BeanInvalidValueException e) {
            throw new GenericViewException("This ingredient is duplicated! Name: " + ingredient, "INVALID_INGREDIENT");
        }
    }

    public void addIngredient(String ingredient, boolean optional, boolean cooked) throws GenericViewException {
        if(!allIngredientList.contains(ingredient)) {
            throw new GenericViewException("This ingredient doesn't exist! Name: " + ingredient, "INVALID_INGREDIENT");
        }
        try {
            dish.addIngredient(new DishIngredientBean(ingredient, optional, cooked));
            notifyAllObserver();
        } catch (BeanInvalidValueException e) {
            throw new GenericViewException("This ingredient is duplicated! Name: " + ingredient, "INVALID_INGREDIENT");
        }
    }

    public void removeIngredient(String ingredient) throws GenericViewException {
        for(DishIngredientBean i : dish.getIngredientList()) {
           if(i.getName().equals(ingredient)) {
               dish.getIngredientList().remove(i);
               notifyAllObserver();
               return;
           }
        }
        throw new GenericViewException("Ingredient " + ingredient + " is not present on dish " + dish.getName(), "");
    }


    public List<String> showAllIngredient() {
        return allIngredientList;
    }

    public List<String> showAllType() {
        return List.copyOf(allTypeList);
    }

    public List<String> showAllergenIngredient(String ingredientName) throws GenericViewException {
        try {
            List<DishAllergenBean> list = controller.showAllergenIngredient(
                    new ShowAllergenIngredientRequest(baseRequest, ingredientName)
            ).getValue().getAllergenList();

            List<String> outList = new ArrayList<>();
            for (DishAllergenBean a : list) {
                outList.add(a.getName());
            }
            return outList;

        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }
}
