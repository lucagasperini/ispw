package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.exception.NotImplementedException;
import com.pickyeaters.logic.utils.LiteralKey;
import com.pickyeaters.logic.utils.LiteralMessage;
import com.pickyeaters.logic.view.LoginView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Navigator {
    private static MainForm mainForm;
    private static Stage stage;

    private static final Deque<String> fxmlContentHistory = new LinkedList<>();
    private static final Deque<String> fxmlHistory = new LinkedList<>();

    private Navigator() {}

    public static void init (Stage privaryStage) {
        stage = privaryStage;
    }

    public static class LoadedForm<T> {
        private final T controller;
        private final Node node;
        LoadedForm(Node node, T controller) {
            this.controller = controller;
            this.node = node;
        }

        public Node getNode() {
            return node;
        }

        public T getController() {
            return controller;
        }
    }

    public static <T> LoadedForm<T> loadNode(String fxml) {
        try {
        FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
        Node node = loader.load();
        T controller = loader.getController();
        return new LoadedForm<>(node, controller);
        } catch (IOException ex) {
            showError(LiteralMessage.FXML_ERROR, LiteralMessage.FXML_ERROR, ex.getMessage());
            return null;
        }
    }

    public static <T> Node loadNode(String fxml, T controller) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
            loader.setController(controller);
            return loader.load();
        } catch (IOException ex) {
            showError(LiteralMessage.FXML_ERROR, LiteralMessage.FXML_ERROR, ex.getMessage());
            return null;
        }
    }

    public static <T> T navigate (String fxml) {
        fxmlHistory.push(fxml);

        LoadedForm<T> loaderForm = loadNode(fxml);
        stage.setScene(new Scene((Parent) loaderForm.getNode()));
        return loaderForm.getController();
    }

    public static <T> T navigateParent() {
        try {
            fxmlHistory.pop();
        } catch (NoSuchElementException e) {
            showErrorMessage("Cannot load a parent fxml for root");
            return null;
        }
        LoadedForm<T> loaderForm = loadNode(fxmlHistory.getFirst());
        stage.setScene(new Scene((Parent) loaderForm.getNode()));
        return loaderForm.getController();
    }

    public static <T> T refresh() {
        LoadedForm<T> loaderForm = loadNode(fxmlHistory.getFirst());
        stage.setScene(new Scene((Parent) loaderForm.getNode()));
        return loaderForm.getController();
    }

    public static <T> T refreshContent() {
        return mainForm.loadContent(fxmlContentHistory.getFirst());
    }

    public static void navigateMainForm() {
        mainForm = navigate("/form/Main.fxml");
    }

    public static <T> T navigateContent(String fxml) {
        fxmlContentHistory.push(fxml);
        return mainForm.loadContent(fxml);
    }

    public static <T> void navigateContent(String fxml, T controller) {
        fxmlContentHistory.push(fxml);
        mainForm.loadContent(fxml, controller);
    }

    public static <T> T navigateContentParent() {
        try {
            fxmlContentHistory.pop();
        } catch (NoSuchElementException e) {
            showErrorMessage("Cannot load a content fxml for root");
            return null;
        }
        return mainForm.loadContent(fxmlContentHistory.getFirst());
    }

    public static <T> void navigateContentParent(T controller) {
        try {
            fxmlContentHistory.pop();
        } catch (NoSuchElementException e) {
            showErrorMessage("Cannot load a parent content fxml for root");
        }
        mainForm.loadContent(fxmlContentHistory.getFirst(), controller);
    }

    public static void navigateHome() {
        fxmlContentHistory.clear();
        LoginView loginView = AppData.getInstance().getApp().displayLoginView();
        if(loginView.isLoggedRestaurateur()) {
            Navigator.navigateContent("/form/RestaurateurMain.fxml");
        } else if(loginView.isLoggedPickie()) {
            Navigator.navigateContent("/form/PickieMain.fxml");
        } else {
            throw new NotImplementedException();
        }
    }

    public static MainForm getMainForm() {
        return mainForm;
    }

    private static void showMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void showInformation(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.INFORMATION);
    }

    public static void showInformation(String key) {
        String title = AppData.getInstance().i18n(key + "_ALERT_CONFERMATION_TITLE");
        String header = AppData.getInstance().i18n(key + "_ALERT_CONFERMATION_HEADER");
        String content = AppData.getInstance().i18n(key + "_ALERT_CONFERMATION_CONTENT");
        showInformation(title, header, content);
    }

    public static void showError(String title, String header, String content) {
        showMessage(title, header, content, Alert.AlertType.ERROR);
    }

    public static void showError(String message, String key) {
        String content;
        String header;
        String title;

        if(message.isEmpty()) {
            content = AppData.getInstance().i18n(LiteralKey.DEFAULT_ALERT_ERROR_CONTENT);
        } else {
            content = message;
        }

        if(key.isEmpty()) {
            title = AppData.getInstance().i18n(LiteralKey.DEFAULT_ALERT_ERROR_TITLE);
            header = AppData.getInstance().i18n(LiteralKey.DEFAULT_ALERT_ERROR_HEADER);
        } else {
            title = AppData.getInstance().i18n(key + "_ALERT_ERROR_TITLE");
            header = AppData.getInstance().i18n(key + "_ALERT_ERROR_HEADER");
            String tryKeyContent = AppData.getInstance().i18n(key + "_ALERT_ERROR_CONTENT");
            if(!tryKeyContent.equals("["+ key + "_ALERT_ERROR_CONTENT]")) {
                content = tryKeyContent;
            }
        }
        showError(title,header,content);
    }

    public static void showError(GenericViewException ex) {
        showError(ex.getMessage(), ex.getKey());
    }

    public static void showErrorMessage(String message) {
        showError(message, "");
    }
}
