package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.VirtualView;

public class AppData {
    private static final AppData instance = new AppData();

    private Application app;

    private AppData() {
    }

    public void init(String[] args) {
        app = new Application(args);
        app.systemStart();
    }

    public String i18n(String key) {
        return VirtualView.i18n(key);
    }


    public Application getApp() {
        return app;
    }

    public static AppData getInstance() {
        return instance;
    }
}
