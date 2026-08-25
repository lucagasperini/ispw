package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.*;
import com.pickyeaters.logic.view.dish.AddDishView;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import com.pickyeaters.logic.view.dish.ShowDishView;

import java.util.List;
import java.util.Map;

public class MenuDetailsForm extends VirtualForm {
    public MenuDetailsForm(Application app) {
        super(app,"MenuDetails");
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    @Override
    protected boolean request(String request) {
        switch (request) {
            case "show", "s":
                showMenu();
                return true;
            case "change", "c":
                changeDish();
                return true;
            case "add", "a":
                addDish();
                return true;
            case "remove", "r":
                removeDish();
                return true;
            case "info", "i":
                infoDish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
                [show, s]
                [change, c]
                [add, a]
                [remove, r]
                [info, i]""";
    }

    private void showMenu() {
        MenuView view = app.displayMenuView();
        List<String> idList = view.showDishID();

        for(String i : idList) {
            printField("RESTAURATEUR_MANAGEMENUDETAILS_NAME", view.showName(i));
            printField("RESTAURATEUR_MANAGEMENUDETAILS_DESCRIPTION",view.showDescription(i));
            printField("RESTAURATEUR_MANAGEMENUDETAILS_CATEGORY",view.showType(i));
            for(String str : view.showIngredientList(i)) {
                printField("RESTAURATEUR_MANAGEMENUDETAILS_INGREDIENTS", str);
            }
            print("##################################");
        }
    }

    private void changeDish() {
        MenuView menuView = app.displayMenuView();
        List<String> idList = menuView.showDishID();
        for (int i = 1; i <= idList.size(); i++) {
            print(i + ") " + menuView.showName(idList.get(i - 1)));
        }
        int formID = askFieldInteger("FIELD_DISH_ID") - 1;
        ChangeDishView view = app.displayChangeDishView(idList.get(formID));

        view.insertName(askField("FIELD_DISH_NAME", view.showName()));
        view.insertDescription(askField("FIELD_DISH_DESCRIPTION", view.showDescription()));
        view.selectType(askField("FIELD_DISH_TYPE", view.showType()));

        if(askFieldBoolean("RESTAURATEUR_EDITDISH_ADDINGREDIENT")) {
            ChangeViewIngredientListForm form = new ChangeViewIngredientListForm(app, view);
            form.show();
        }
        if (askFieldBoolean("SAVECHANGES")) {
            view.submit();
        }
    }

    private void addDish() {
        AddDishView view = app.displayAddDishView();
        view.insertName(askField("FIELD_DISH_NAME"));
        view.insertDescription(askField("FIELD_DISH_DESCRIPTION"));
        view.selectType(askField("FIELD_DISH_TYPE"));

        if(askFieldBoolean("RESTAURATEUR_EDITDISH_ADDINGREDIENT")) {
            AddViewIngredientListForm form = new AddViewIngredientListForm(app, view);
            form.show();
        }

        if(askFieldBoolean("SAVECHANGES")) {
            view.submit();
        }
    }

    private void removeDish() {
        try {
            MenuView view = app.displayMenuView();
            List<String> idList = view.showDishID();
            for (int i = 1; i <= idList.size(); i++) {
                print(i + ") " + view.showName(idList.get(i - 1)));
            }
            int formID = askFieldInteger("FIELD_DISH_ID") - 1;
            view.submitRemoveDish(idList.get(formID));
        }  catch (GenericViewException e) {
            showError(e);
        }
    }

    private void infoDish() {
        MenuView view = app.displayMenuView();
        List<String> idList = view.showDishID();
        for (int i = 1; i <= idList.size(); i++) {
            print(i + ") " + view.showName(idList.get(i - 1)));
        }
        int formID = askFieldInteger("FIELD_DISH_ID") - 1;
        ShowDishView showDishView = app.displayShowDishView(idList.get(formID));
        printField("RESTAURATEUR_MANAGEMENUDETAILS_NAME", showDishView.showName());
        printField("RESTAURATEUR_MANAGEMENUDETAILS_DESCRIPTION", showDishView.showDescription());
        printField("RESTAURATEUR_MANAGEMENUDETAILS_CATEGORY", showDishView.showType());
        for (String str : showDishView.showIngredientNameList()) {
            printField("RESTAURATEUR_MANAGEMENUDETAILS_INGREDIENTS", str);
        }
        for (String str : showDishView.showAllergenList()) {
            printField("RESTAURATEUR_EDITDISH_ALLERGENS", str);
        }
    }


    private class ChangeViewIngredientListForm extends VirtualListManagerForm {
        private final ChangeDishView view;
        public ChangeViewIngredientListForm(Application app, ChangeDishView view) {
            super(app, "IngredientList");
            this.view = view;
        }


        @Override
        protected void showList() {
            printFieldList("FIELD_DISH_INGREDIENT", view.showIngredientNameList());
        }

        @Override
        protected void addItem() {
            view.addIngredient(askField("ASK_ADD"));
        }

        @Override
        protected void removeItem() {
            view.removeIngredient(askField("ASK_REMOVE"));
        }
    }

    private class AddViewIngredientListForm extends VirtualListManagerForm {
        private final AddDishView view;
        public AddViewIngredientListForm(Application app, AddDishView view) {
            super(app, "IngredientList");
            this.view = view;
        }


        @Override
        protected void showList() {
            printFieldList("FIELD_DISH_INGREDIENT", view.showIngredientNameList());
        }

        @Override
        protected void addItem() {
            view.addIngredient(askField("ASK_ADD"));
        }

        @Override
        protected void removeItem() {
            view.removeIngredient(askField("ASK_REMOVE"));
        }
    }
}
