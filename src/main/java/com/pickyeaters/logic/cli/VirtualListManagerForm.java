package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.view.Application;

import java.util.Map;

public abstract class VirtualListManagerForm extends VirtualForm {
    public VirtualListManagerForm(Application app, String name) {
        super(app, name);
    }

    @Override
    public void show(Map<String, String> arg) {
        requestLoop();
    }

    public boolean request(String request) {
        switch (request) {
            case "show", "s":
                showList();
                return true;
            case "add", "a":
                addItem();
                return true;
            case "remove", "r":
                removeItem();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String requestHelp() {
        return """
                [show, s]
                [add, a]
                [remove, r]""";
    }

    protected abstract void showList();
    protected abstract void addItem();
    protected abstract void removeItem();

}
