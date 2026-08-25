package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.eatingpreference.EditEatingPreferenceView;

import java.util.Map;

public class EatingPreferenceForm extends VirtualForm {
    private final EditEatingPreferenceView view;
    public EatingPreferenceForm(Application app) {
        super(app, "EatingPreference");
        view = app.displayEditEatingPreferenceView();
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    public boolean request(String request) {
        switch (request) {
            case "show", "s":
                showEatingPreference();
                return true;
            case "edit ingredient", "ei":
                DislikeIngredientListForm ingredientForm = new DislikeIngredientListForm(app, view);
                ingredientForm.show();
                return true;
            case "edit allergen", "ea":
                AllergenListForm allergenForm = new AllergenListForm(app, view);
                allergenForm.show();
                return true;
            case "edit group", "eg":
                ExcludedGroupListForm excludedGroupForm = new ExcludedGroupListForm(app, view);
                excludedGroupForm.show();
                return true;
            case "submit", "k":
                view.submit();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
                [show, s]
                [edit ingredient, ei]
                [edit allergen, ea]
                [edit group, eg]
                [submit, k]""";
    }

    private void showEatingPreference() {
        printFieldList("FIELD_EATINGPREFERENCE_DISLIKEINGREDIENT", view.showExcludedIngredientList());
        printFieldList("FIELD_EATINGPREFERENCE_ALLERGEN", view.showAllergenList());
        printFieldList("FIELD_EATINGPREFERENCE_EXCLUDEDGROUP", view.showExcludedGroupList());
    }

    private class DislikeIngredientListForm extends VirtualListManagerForm {
        private final EditEatingPreferenceView view;
        public DislikeIngredientListForm(Application app, EditEatingPreferenceView view) {
            super(app, "DislikeIngredientList");
            this.view = view;
        }


        @Override
        protected void showList() {
            printFieldList("FIELD_EATINGPREFERENCE_DISLIKEINGREDIENT", view.showExcludedIngredientList());
        }

        @Override
        protected void addItem() {
            view.addDislikeIngredient(askField("ASK_ADD"));
        }

        @Override
        protected void removeItem() {
            view.removeDislikeIngredient(askField("ASK_REMOVE"));
        }
    }

    private class AllergenListForm extends VirtualListManagerForm {
        private final EditEatingPreferenceView view;
        public AllergenListForm(Application app, EditEatingPreferenceView view) {
            super(app, "AllergenList");
            this.view = view;
        }


        @Override
        protected void showList() {
            printFieldList("FIELD_EATINGPREFERENCE_ALLERGEN", view.showAllergenList());
        }

        @Override
        protected void addItem() {
            view.addAllergen(askField("ASK_ADD"));
        }

        @Override
        protected void removeItem() {
            view.removeAllergen(askField("ASK_REMOVE"));
        }
    }

    private class ExcludedGroupListForm extends VirtualListManagerForm {
        private final EditEatingPreferenceView view;
        public ExcludedGroupListForm(Application app, EditEatingPreferenceView view) {
            super(app, "ExcludedGroupList");
            this.view = view;
        }


        @Override
        protected void showList() {
            printFieldList("FIELD_EATINGPREFERENCE_EXCLUDEDGROUP", view.showExcludedGroupList());
        }

        @Override
        protected void addItem() {
            view.addExcludedGroup(askField("ASK_ADD"));
        }

        @Override
        protected void removeItem() {
            view.removeExcludedGroup(askField("ASK_REMOVE"));
        }
    }


}
