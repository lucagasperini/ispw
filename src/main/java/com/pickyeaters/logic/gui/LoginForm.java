package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.cli.PickieMainForm;
import com.pickyeaters.logic.cli.RestaurateurMainForm;
import com.pickyeaters.logic.exception.LoginViewException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.view.LoginView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginForm {
    private LoginView loginView;

    @FXML
    private TextField inputEmail;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private Button buttonLogin;
    @FXML
    private Text textEmail;
    @FXML
    private Text textPassword;
    @FXML
    protected Text textTitle;
    @FXML
    protected Text textSubtitle;

    @FXML
    protected void initialize() {
        loginView = AppData.getInstance().getApp().displayLoginView();

        textEmail.setText(AppData.getInstance().i18n("LOGIN_EMAIL"));
        textPassword.setText(AppData.getInstance().i18n("LOGIN_PASSWORD"));
        buttonLogin.setText(AppData.getInstance().i18n("LOGIN_LOGIN"));
        textTitle.setText(AppData.getInstance().i18n("LOGIN_TITLE"));
        textSubtitle.setText(AppData.getInstance().i18n("LOGIN_SUBTITLE"));
    }

    @FXML
    private void clickButtonLogin(ActionEvent event) {
        try {
            loginView.insertEmail(inputEmail.getText());
            loginView.insertPassword(inputPassword.getText());
            AppData.getInstance().getApp().login();

            Navigator.navigateMainForm();
            Navigator.navigateHome();

        } catch (LoginViewException e) {
            Navigator.showError(e);
        }
    }

}
