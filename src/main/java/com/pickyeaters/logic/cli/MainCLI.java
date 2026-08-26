package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.exception.*;
import com.pickyeaters.logic.view.*;

import java.util.Scanner;

public class MainCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static Application app;

    public static void main(String[] args) {
        // Application will construct base system
        app = new Application(args);
        doSystemConfiguration();
        doLogin();
        doMainForm();
        quit();
    }

    private static void doSystemConfiguration() {

        // Recursive error handling
        boolean passedCheck = false;
        while(!passedCheck) {
            try {
                // try to startup the system with current parameters,
                // if fails will display error and ask for new parameters
                app.systemStart();
                passedCheck = true;
                // Locale error handling
            } catch (LocaleViewException e) {
                if(!e.getMessage().isEmpty()) {
                    app.getPrinter().println("ERROR: " + e.getMessage());
                }
                promptForLocale(app.displayLocaleView());
                // Config error handling
            } catch (ConfigViewException | DatabaseControllerException e) {
                if(!e.getMessage().isEmpty()) {
                    displayErrorMessage(e.getMessage());
                }
                promptForConfig(app.displayConfigView());
                // Database error handling, if requested with parameters
            }
        }
    }

    private static void doLogin() {
        LoginView loginView = app.displayLoginView();

        boolean isAuth = false;
        while(!isAuth) {
            try {
                promptForLogin(loginView);
                app.login();
                isAuth = true;
            } catch (LoginViewException e) {
                app.getPrinter().println(e.getMessage());
            }
        }
    }

    private static void doMainForm() {
        LoginView loginView = app.displayLoginView();

        if(loginView.isLoggedRestaurateur()) {
            RestaurateurMainForm mainForm = new RestaurateurMainForm(app);
            mainForm.show();
        } else if(loginView.isLoggedPickie()) {
            PickieMainForm mainForm = new PickieMainForm(app);
            mainForm.show();
        } else {
            throw new NotImplementedException();
        }
    }

    private static void quit() {
        scanner.close();
    }

    private static void displayErrorMessage(String errorMessage) {
        app.getPrinter().println(VirtualView.i18n("ERROR") + ": " + errorMessage);
    }

    private static void promptForLocale(LocaleView localeView) {
        app.getPrinter().println("--- System Locale Setup ---");
        try {
            app.getPrinter().print("Enter Locale Language (localeLang): ");
            localeView.selectLocaleLang(scanner.nextLine());
            // Handle invalid locale selected
        } catch (LocaleViewException e) {
            app.getPrinter().println("ERROR: " + e.getMessage());
        }
    }

    private static void promptForConfig(ConfigView configView) {
        app.getPrinter().println("--- System Config Setup ---");

        try {
            if(configView.checkedProviderDatabase()) {
                app.getPrinter().print("Enter Database Name (databaseName): ");
                configView.insertDatabaseName(scanner.nextLine());
                app.getPrinter().print("Enter Database Host (databaseHost): ");
                configView.insertDatabaseHost(scanner.nextLine());
                app.getPrinter().print("Enter Database Port (databasePort): ");
                configView.insertDatabasePort(scanner.nextLine());
                app.getPrinter().print("Enter Database User (databaseUser): ");
                configView.insertDatabaseUser(scanner.nextLine());
                app.getPrinter().print("Enter Database Password (databasePassword): ");
                configView.insertDatabasePassword(scanner.nextLine());
            }

        } catch (ConfigViewException e) {
            try {
                app.getPrinter().print("Enter Provider (provider): ");
                configView.selectProvider(scanner.nextLine());
            } catch (ConfigViewException ex) {
                displayErrorMessage(ex.getMessage());
            }
        }
    }

    private static void promptForLogin(LoginView loginView) {
        app.getPrinter().println("--- Login ---");
        app.getPrinter().print("Enter user e-mail: ");
        loginView.insertEmail(scanner.nextLine());
        app.getPrinter().print("Enter user password: ");
        loginView.insertPassword(scanner.nextLine());
    }


}
