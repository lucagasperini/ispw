package com.pickyeaters.logic.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RestaurateurMainForm {
    @FXML
    private Button buttonRestaurantDetails;
    @FXML
    private Button buttonMenuDetails;

    @FXML
    public void initialize() {
        Navigator.getMainForm().setTitle("RESTAURATEUR_HOME");
        Navigator.getMainForm().hideBackButton();

        buttonMenuDetails.setText(AppData.getInstance().i18n("RESTAURATEUR_HOME_MANAGEMENUDETAILS"));
        buttonRestaurantDetails.setText(AppData.getInstance().i18n("RESTAURATEUR_HOME_MANAGERESTAURANTDETAILS"));
    }

    @FXML
    private void clickButtonMenuDetails(ActionEvent event) {
        Navigator.navigateContent("/form/MenuDetails.fxml");
    }

    @FXML
    private void clickButtonRestaurantDetails(ActionEvent event) {
        Navigator.navigateContent("/form/RestaurantDetails.fxml");
    }


}
