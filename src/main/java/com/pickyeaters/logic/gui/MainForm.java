package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.UserView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.Random;

public class MainForm {
    @FXML
    private ScrollPane mainPane;
    @FXML
    private BorderPane mainLayout;
    @FXML
    private ImageView imageLogo;
    @FXML
    private Text textNavbarUsername;
    @FXML
    private Text textNavbarWelcome;
    @FXML
    private MenuItem menuItemNavbarLogout;
    @FXML
    private Button buttonBack;
    @FXML
    protected Text textTitle;
    @FXML
    protected Text textSubtitle;

    private Runnable actionBackButton = null;
    Random random = new Random();

    @FXML
    public void initialize() {
        showNavbar();
    }


    public <T> T loadContent(String fxml) {
        Navigator.LoadedForm<T> loadedForm = Navigator.loadNode(fxml);
        mainPane.setContent(loadedForm.getNode());
        return loadedForm.getController();
    }


    public <T> void loadContent(String fxml, T controller) {
        Node node = Navigator.loadNode(fxml, controller);
        mainPane.setContent(node);
    }


    public void setActionBackButton(Runnable actionBackButton) {
        this.actionBackButton = actionBackButton;
    }

    public void setDefaultActionBackButton() {
        this.actionBackButton = null;
    }

    public void hideHeader() {
        mainLayout.setTop(null);
    }
    public void showHeader() {
        buttonBack.setVisible(true);
        textTitle.setVisible(true);
        textSubtitle.setVisible(true);

        buttonBack.setText(AppData.getInstance().i18n("BACK"));
    }

    public void updateName() {
        UserView view = AppData.getInstance().getApp().displayUserView();
        textNavbarUsername.setText(view.showFirstname());
    }

    public void showNavbar() {
        updateName();
        textNavbarWelcome.setText(AppData.getInstance().i18n("NAVBAR_HELLO_" + random.nextInt(1,4)));
        menuItemNavbarLogout.setText(AppData.getInstance().i18n("NAVBAR_LOGOFF"));
    }

    public void setTitle(String key) {
        textTitle.setText(AppData.getInstance().i18n(key + "_TITLE"));
        textSubtitle.setText(AppData.getInstance().i18n(key + "_SUBTITLE"));
    }
    public void showBackButton() {
        showHeader();
    }

    public void hideTitle() {
        textTitle.setVisible(false);
        textSubtitle.setVisible(false);
    }

    public void hideBackButton() {
        buttonBack.setVisible(false);
    }

    @FXML
    protected void clickImageLogo() {
        Navigator.navigateHome();
    }

    @FXML
    private void clickMenuItemNavbarLogout(ActionEvent event) {
        AppData.getInstance().getApp().logout();
        Navigator.navigateParent();
    }

    @FXML
    private void clickButtonBack(ActionEvent event) {
        if(actionBackButton != null) {
            actionBackButton.run();
        } else {
            Navigator.navigateContentParent();
        }
    }
}
