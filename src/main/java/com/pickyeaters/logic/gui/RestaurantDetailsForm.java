package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.UserView;
import com.pickyeaters.logic.view.restaurant.EditRestaurantView;
import com.pickyeaters.logic.view.restaurant.ShowRestaurantView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RestaurantDetailsForm {

    @FXML
    private TextField inputRestaurateurFirstname;
    @FXML
    private TextField inputRestaurateurLastname;
    @FXML
    private TextField inputRestaurateurEmail;
    @FXML
    private TextField inputRestaurantName;
    @FXML
    private TextField inputRestaurantAddress;
    @FXML
    private TextField inputRestaurantPhone;
    @FXML
    private TextField inputRestaurantCity;
    @FXML
    private Button buttonSave;
    @FXML
    private Text textRestaurateurFirstname;
    @FXML
    private Text textRestaurateurLastname;
    @FXML
    private Text textRestaurateurDetails;
    @FXML
    private Text textRestaurateurEmail;
    @FXML
    private Text textRestaurantDetails;
    @FXML
    private Text textRestaurantName;
    @FXML
    private Text textRestaurantAddress;
    @FXML
    private Text textRestaurantPhone;
    @FXML
    private Text textRestaurantCity;


    @FXML
    private Text textMyPersonalDetails;
    @FXML
    private Text textMyRestaurantDetails;


    @FXML
    public void initialize() {
        Navigator.getMainForm().setTitle("RESTAURATEUR_MANAGERESTAURANTDETAILS");
        Navigator.getMainForm().showBackButton();
        buttonSave.setText(AppData.getInstance().i18n("SAVECHANGES"));
        textRestaurateurFirstname.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURATEUR_FIRSTNAME"));
        textRestaurateurLastname.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURATEUR_LASTNAME"));
        textRestaurateurEmail.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURATEUR_EMAIL"));
        textRestaurantName.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_NAME"));
        textRestaurantAddress.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_ADDRESS"));
        textRestaurantPhone.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_PHONE"));
        textRestaurantCity.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_RESTAURANT_CITY"));
        textMyPersonalDetails.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_MYPERSONALDETAILS"));
        textMyRestaurantDetails.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGERESTAURANTDETAILS_MYRESTAURANTDETAILS"));

        UserView userView = AppData.getInstance().getApp().displayUserView();
        inputRestaurateurFirstname.setText(userView.showFirstname());
        inputRestaurateurLastname.setText(userView.showLastname());
        inputRestaurateurEmail.setText(userView.showEmail());

        ShowRestaurantView view = AppData.getInstance().getApp().displayShowRestaurantView();
        inputRestaurantName.setText(view.showName());
        inputRestaurantAddress.setText(view.showAddress());
        inputRestaurantPhone.setText(view.showPhone());
        inputRestaurantCity.setText(view.showCity());
    }

    @FXML
    private void clickButtonSave(ActionEvent event) {
        try {
            EditRestaurantView view = AppData.getInstance().getApp().displayEditRestaurantView();
            view.insertName(inputRestaurantName.getText());
            view.insertAddress(inputRestaurantAddress.getText());
            view.insertPhone(inputRestaurantPhone.getText());
            view.insertCity(inputRestaurantCity.getText());
            view.submit();
            UserView userView = AppData.getInstance().getApp().displayUserView();
            userView.insertEmail(inputRestaurateurEmail.getText());
            userView.insertFirstname(inputRestaurateurFirstname.getText());
            userView.insertLastname(inputRestaurateurLastname.getText());
            userView.submit();
            Navigator.getMainForm().updateName();
            Navigator.navigateContentParent();
        } catch (GenericViewException e) {
            Navigator.showError(e);
        }
    }

}
