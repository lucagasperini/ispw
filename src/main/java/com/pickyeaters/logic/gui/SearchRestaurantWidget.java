package com.pickyeaters.logic.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class SearchRestaurantWidget {

    @FXML
    private TitledPane paneRestaurant;

    @FXML
    private VBox vboxDish;

    private String restaurantName;

    @FXML
    public void initialize() {
        paneRestaurant.setText(restaurantName);
    }

    public void init(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
