package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.*;
import com.pickyeaters.logic.bean.reply.ShowMenuReply;
import com.pickyeaters.logic.bean.reply.ShowRestaurantReply;
import com.pickyeaters.logic.bean.request.RemoveDishRequest;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.bean.request.ShowMenuRequest;
import com.pickyeaters.logic.bean.request.ShowRestaurantRequest;
import com.pickyeaters.logic.controller.MenuController;
import com.pickyeaters.logic.controller.RestaurantController;
import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.ResultErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuView extends VirtualView {
    private final MenuController controller;

    private final Map<String, DishBean> dishMap;


    public MenuView(Request baseRequest, MenuController menuController,
                    RestaurantController restaurantController, String restaurantID) throws GenericViewException{
        super(baseRequest);
        controller = menuController;
        try {
            if(restaurantID.isEmpty()) {
                ShowRestaurantReply showRestaurantReply =
                        restaurantController.showRestaurant(
                                new ShowRestaurantRequest(baseRequest, "")
                        ).getValue();
                restaurantID = showRestaurantReply.getID();
            }
            ShowMenuRequest request = new ShowMenuRequest(baseRequest, restaurantID);
            ShowMenuReply showMenuReply = controller.showMenu(request).getValue();
            dishMap = showMenuReply.getDishMap();

        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_FETCH");
        }
    }


    private DishBean getDish(String id) {
        return dishMap.get(id);
    }

    public List<String> showIngredientList(String id) {
        List<String> ingredientList = new ArrayList<>();
        DishBean dish = dishMap.get(id);
        for(DishIngredientBean i: dish.getIngredientList()) {
            ingredientList.add(i.getName());
        }
        return ingredientList;
    }

    public List<String> showDishID() {
        return List.copyOf(dishMap.keySet());
    }

    public String showName(String vid) {
        return getDish(vid).getName();
    }

    public String showDescription(String vid) {
        return getDish(vid).getDescription();
    }

    public String showType(String vid) {
        return i18n("DISH_TYPE_" + getDish(vid).getType());
    }


    public void submitRemoveDish(String dishID) {
        try {
            RemoveDishRequest request = new RemoveDishRequest(baseRequest, dishID);
            controller.removeDish(request).getValue();
            notifyAllObserver();
        } catch (ResultErrorException e) {
            throw new GenericViewException(e.getMessage(), "FAILED_SAVE");
        }
    }

}
