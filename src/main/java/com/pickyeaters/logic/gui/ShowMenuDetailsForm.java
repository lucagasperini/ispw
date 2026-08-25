package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.MenuView;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

public class ShowMenuDetailsForm {
    @FXML
    private VBox vboxMenu;

    @FXML
    public void initialize() {
        Navigator.getMainForm().showBackButton();
        Navigator.getMainForm().setTitle("RESTAURATEUR_MANAGEMENUDETAILS");
        Navigator.getMainForm().setDefaultActionBackButton();
    }

    public void init(String restaurantID) {
        MenuView view = AppData.getInstance().getApp().displayMenuView(restaurantID);
        List<String> idList = view.showDishID();
        vboxMenu.getChildren().clear();

        for(String i : idList) {
            Navigator.LoadedForm<ShowDishItemWidget> node = Navigator.loadNode("/form/ShowDishItemWidget.fxml");
            node.getController().init(i, restaurantID);
            vboxMenu.getChildren().add(node.getNode());
        }
    }
}
