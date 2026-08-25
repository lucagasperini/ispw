package com.pickyeaters.logic.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PickieMainForm {
    @FXML
    private Button buttonFindRestaurant;
    @FXML
    private Button buttonEatingPreferences;

    @FXML
    protected void initialize() {
        Navigator.getMainForm().setTitle("PICKY_HOME");
        Navigator.getMainForm().hideBackButton();

        buttonFindRestaurant.setText(AppData.getInstance().i18n("PICKY_HOME_FINDRESTAURANT"));
        buttonEatingPreferences.setText(AppData.getInstance().i18n("PICKY_HOME_PERSONALIZEEATINGPREFERENCES"));
    }

    @FXML
    private void clickFindRestaurant(ActionEvent event) {
        Navigator.navigateContent("/form/FindRestaurant.fxml");
    }

    @FXML
    private void clickButtonEatingPreferences(ActionEvent event) {
        Navigator.navigateContent("/form/EatingPreference.fxml");
    }
}
