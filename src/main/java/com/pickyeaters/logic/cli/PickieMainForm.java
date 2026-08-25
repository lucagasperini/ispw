package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;

import java.util.Map;

public class PickieMainForm extends VirtualForm {
    public PickieMainForm(Application app) {
        super(app,"Home");
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    public boolean request(String request) {
        switch (request) {
            case "preference", "p":
                EatingPreferenceForm eatingPreferenceForm = new EatingPreferenceForm(app);
                eatingPreferenceForm.show();
                return true;
            case "find", "f":
                FindRestaurantForm findRestaurantForm = new FindRestaurantForm(app);
                findRestaurantForm.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
            [preference, p]
            [find, f]""";
    }


}
