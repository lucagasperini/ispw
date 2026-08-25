package com.pickyeaters.logic.gui;


import javafx.application.Application;

public class MainGUI
{
    public static void main(String[] args) {
        AppData.getInstance().init(args);
        Application.launch(Launcher.class, args);
    }
}
