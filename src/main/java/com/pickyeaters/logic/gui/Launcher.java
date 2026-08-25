package com.pickyeaters.logic.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application  {
    @Override
    public void start(Stage stage) {
        stage.setTitle("PickyEater");


        Navigator.init(stage);
        Navigator.navigate("/form/Login.fxml");
        stage.show();
    }
}
