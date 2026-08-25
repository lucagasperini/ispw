package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.utils.LiteralKey;
import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.VirtualView;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public abstract class VirtualForm  {

    protected final Application app;
    private final String formName;

    protected VirtualForm(Application app, String formName) {
        this.formName = formName;
        this.app = app;
    }

    private void showError(String text, String key) {
        String title;
        String header;
        String content;
        if(key.isEmpty()) {
            title = i18n(LiteralKey.DEFAULT_ALERT_ERROR_TITLE);
            header = i18n(LiteralKey.DEFAULT_ALERT_ERROR_HEADER);
            content = i18n(LiteralKey.DEFAULT_ALERT_ERROR_CONTENT);
         } else {
            title = i18n(key + "_ALERT_ERROR_TITLE");
            header = i18n(key + "_ALERT_ERROR_HEADER");
            content = i18n(key + "_ALERT_ERROR_CONTENT");
        }

        print(title);
        print(header);

        if(key.isEmpty() && text.isEmpty()) {
            print(content);
        } else if(key.isEmpty()) {
            print(text);
        } else {
            print(content);
        }
    }

    public void showError(GenericViewException ex) {
            showError(ex.getMessage(), ex.getKey());
    }

    public abstract void show(Map<String, String> arg);
    public void show() {
        show(null);
    }

    public void print(String text) {
        System.out.println(text);
    }

    public String i18n(String key) {
        return VirtualView.i18n(key);
    }

    public void printField(String key, String value) {
        System.out.println(i18n(key) + ": " + value);
    }

    public void printFieldList(List<String> value) {
        if(value.isEmpty()) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for(String i : value) {
            builder.append(i);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        System.out.println(builder);
    }

    public void printFieldList(String key, List<String> value) {
        StringBuilder builder = new StringBuilder();
        for(String i : value) {
            builder.append(i);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        System.out.println(i18n(key) + ": " + builder);
    }

    public void printField(String key, boolean value) {
        String yes = i18n("YES");
        String no = i18n("NO");
        System.out.println(i18n(key) + ": " + (value ? yes : no));
    }

    public String askField(String key) {
        return askField(key, "");
    }

    public String askField(String key, String defaultValue) {
        Scanner userInput = new Scanner(System.in);
        if(defaultValue.isEmpty()) {
            System.out.print(i18n(key) + ": ");
        } else {
            System.out.print(i18n(key) + " [" + defaultValue + "]: ");
        }
        String out = userInput.nextLine();
        if(out.isEmpty()) {
            return defaultValue;
        } else {
            return out;
        }
    }

    public boolean askFieldBoolean(String key) {
        Scanner userInput = new Scanner(System.in);

        System.out.print(i18n(key) + "? [Y/N]: ");
        String out = userInput.nextLine().toUpperCase();

        return out.equals("Y");
    }

    public int askFieldInteger(String key) throws GenericViewException {
        try {
            return Integer.parseInt(askField(key));
        } catch (NumberFormatException e) {
            throw new GenericViewException("Input is not an integer", "");
        }
    }

    protected abstract boolean request(String request);

    protected abstract String requestHelp();


    protected void requestLoop() {
        Scanner userInput = new Scanner(System.in);
        while(true) {
            System.out.print(formName + "> ");
            String input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "b", "back" -> {
                    return;
                }
                case "q", "quit" -> System.exit(0);
                case "h", "help" -> System.out.println("""
                                [back, b]
                                [quit, q]
                                """ +
                        requestHelp()
                );
                default -> {
                    try {
                        if (!request(input)) {
                            showError("", "UNSUPPORTED_OPERATION");
                        }
                    }  catch (GenericViewException e) {
                        showError(e);
                    }
                }
            }
        }
    }
}
