package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;

import java.util.Map;

public class RestaurateurMainForm extends VirtualForm {
    public RestaurateurMainForm(Application app) {
        super(app,"Home");
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    public boolean request(String request) {
        switch (request) {
            case "restaurant", "r":
                RestaurantDetailsForm restaurantDetailsView = new RestaurantDetailsForm(app);
                restaurantDetailsView.show();
                return true;
            case "menu", "m":
                MenuDetailsForm menuDetailsForm = new MenuDetailsForm(app);
                menuDetailsForm.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
            [restaurant, r]
            [menu, m]""";
    }


}
