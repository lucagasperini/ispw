package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.MenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuDetailsForm {
    @FXML
    private VBox vboxMenu;
    @FXML
    private Button buttonAddDish;

    @FXML
    public void initialize() {
        Navigator.getMainForm().showBackButton();
        Navigator.getMainForm().setTitle("RESTAURATEUR_MANAGEMENUDETAILS");
        buttonAddDish.setText(AppData.getInstance().i18n("RESTAURATEUR_MANAGEMENUDETAILS_ADDDISH"));

        MenuView view = AppData.getInstance().getApp().displayMenuView();
        List<String> idList = view.showDishID();
        vboxMenu.getChildren().clear();

        for(String i : idList) {
            Navigator.LoadedForm<DishItemWidget> node = Navigator.loadNode("/form/DishItemWidget.fxml");
            node.getController().init(i);
            vboxMenu.getChildren().add(node.getNode());
        }

    }

    @FXML
    private void clickButtonAddDish(ActionEvent event) {
        Navigator.navigateContent(
                "/form/Dish.fxml",
                new AddDishForm(AppData.getInstance().getApp().displayAddDishView())
        );
    }
}
