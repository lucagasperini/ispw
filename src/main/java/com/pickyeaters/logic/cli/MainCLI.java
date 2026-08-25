package com.pickyeaters.logic.cli;

import com.pickyeaters.logic.exception.*;
import com.pickyeaters.logic.view.*;

import java.util.Scanner;

public class MainCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Application will construct base system
        Application app = new Application(args);

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
                    System.out.println("ERROR: " + e.getMessage());
                }
                promptForLocale(app.displayLocaleView());
                // Config error handling
            } catch (ConfigViewException e) {
                if(!e.getMessage().isEmpty()) {
                    displayErrorMessage(e.getMessage());
                }
                promptForConfig(app.displayConfigView());
                // Database error handling, if requested with parameters
            } catch (DatabaseControllerException e) {
                if(!e.getMessage().isEmpty()) {
                    displayErrorMessage(e.getMessage());
                }
                promptForConfig(app.displayConfigView());
            }
        }

        LoginView loginView = app.displayLoginView();

        boolean isAuth = false;
        while(!isAuth) {
            try {
                promptForLogin(loginView);
                app.login();
                isAuth = true;
            } catch (LoginViewException e) {
                System.out.println(e.getMessage());
            }
        }

        if(loginView.isLoggedRestaurateur()) {
            RestaurateurMainForm mainForm = new RestaurateurMainForm(app);
            mainForm.show();
        } else if(loginView.isLoggedPickie()) {
            PickieMainForm mainForm = new PickieMainForm(app);
            mainForm.show();
        } else if(loginView.isLoggedAdmin()) {
            throw new NotImplementedException();
        } else {
            throw new NotImplementedException();
        }

        quit();
    }

    private static void quit() {
        scanner.close();
    }

    private static void displayErrorMessage(String errorMessage) {
        System.out.println(VirtualView.i18n("ERROR") + ": " + errorMessage);
    }

    private static void promptForLocale(LocaleView localeView) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- System Locale Setup ---");
        try {
            System.out.print("Enter Locale Language (localeLang): ");
            localeView.selectLocaleLang(scanner.nextLine());
            // Handle invalid locale selected
        } catch (LocaleViewException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void promptForConfig(ConfigView configView) {

        System.out.println("--- System Config Setup ---");

        try {
            if(configView.checkedProviderDatabase()) {
                System.out.print("Enter Database Name (databaseName): ");
                configView.insertDatabaseName(scanner.nextLine());
                System.out.print("Enter Database Host (databaseHost): ");
                configView.insertDatabaseHost(scanner.nextLine());
                System.out.print("Enter Database Port (databasePort): ");
                configView.insertDatabasePort(scanner.nextLine());
                System.out.print("Enter Database User (databaseUser): ");
                configView.insertDatabaseUser(scanner.nextLine());
                System.out.print("Enter Database Password (databasePassword): ");
                configView.insertDatabasePassword(scanner.nextLine());
            }

        } catch (ConfigViewException e) {
            try {
                System.out.print("Enter Provider (provider): ");
                configView.selectProvider(scanner.nextLine());
            } catch (ConfigViewException ex) {
                displayErrorMessage(ex.getMessage());
            }
        }
    }

    private static void promptForLogin(LoginView loginView) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Login ---");
        System.out.print("Enter user e-mail: ");
        loginView.insertEmail(scanner.nextLine());
        System.out.print("Enter user password: ");
        loginView.insertPassword(scanner.nextLine());
    }


}
