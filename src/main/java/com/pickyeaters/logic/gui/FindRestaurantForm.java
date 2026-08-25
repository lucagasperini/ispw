package com.pickyeaters.logic.gui;

import com.pickyeaters.logic.exception.GenericViewException;
import com.pickyeaters.logic.view.FindRestaurantView;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class FindRestaurantForm {

    @FXML
    private Button buttonSearch;

    @FXML
    private CheckBox checkboxAppetizer;

    @FXML
    private CheckBox checkboxContour;

    @FXML
    private CheckBox checkboxDessert;

    @FXML
    private CheckBox checkboxDrink;

    @FXML
    private CheckBox checkboxFirst;

    @FXML
    private CheckBox checkboxSecond;

    @FXML
    private TableColumn<TableRow, Void> columnActions;

    @FXML
    private TableColumn<TableRow, String> columnAddress;

    @FXML
    private TableColumn<TableRow, String> columnName;

    @FXML
    private TableColumn<TableRow, String> columnPhone;

    @FXML
    private ComboBox<String> comboBoxCity;

    @FXML
    private TableView<TableRow> tableRestaurant;

    @FXML
    private Text textCategory;

    @FXML
    private Text textCity;

    @FXML
    private Text textRestaurant;

    private FindRestaurantView view;

    @FXML
    public void initialize() {
        Navigator.getMainForm().setTitle("PICKY_FINDARESTAURANT");
        Navigator.getMainForm().showBackButton();
        view = AppData.getInstance().getApp().displayFindRestaurantView();

        comboBoxCity.getItems().addAll(view.showAllCity());

        checkboxDessert.setText(AppData.getInstance().i18n(checkboxDessert.getText()));
        checkboxSecond.setText(AppData.getInstance().i18n(checkboxSecond.getText()));
        checkboxContour.setText(AppData.getInstance().i18n(checkboxContour.getText()));
        checkboxFirst.setText(AppData.getInstance().i18n(checkboxFirst.getText()));
        checkboxDrink.setText(AppData.getInstance().i18n(checkboxDrink.getText()));
        checkboxAppetizer.setText(AppData.getInstance().i18n(checkboxAppetizer.getText()));

        textCategory.setText(AppData.getInstance().i18n(textCategory.getText()));
        textRestaurant.setText(AppData.getInstance().i18n(textRestaurant.getText()));
        textCity.setText(AppData.getInstance().i18n(textCity.getText()));

        buttonSearch.setText(AppData.getInstance().i18n(buttonSearch.getText()));

        columnActions.setText(AppData.getInstance().i18n(columnActions.getText()));
        columnAddress.setText(AppData.getInstance().i18n(columnAddress.getText()));
        columnName.setText(AppData.getInstance().i18n(columnName.getText()));
        columnPhone.setText(AppData.getInstance().i18n(columnPhone.getText()));

        checkboxAppetizer.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedAppetizer();
        });
        checkboxDrink.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedDrink();
        });
        checkboxFirst.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedFirst();
        });
        checkboxContour.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedContour();
        });
        checkboxSecond.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedSecond();
        });
        checkboxDessert.selectedProperty().addListener((a,b,c)-> {
            view.toggleNeedDessert();
        });

        columnName.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().name)
        );
        columnAddress.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().address)
        );
        columnPhone.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().phone)
        );
        columnActions.setCellFactory(
                column -> new TableCell<>() {
                    private final Button showButton = new Button("Show");
                    {
                        showButton.getStyleClass().add("bt-stepAction1");
                        showButton.setOnAction( event -> {
                                    String id = getTableRow().getItem().id;
                                    ShowMenuDetailsForm form = Navigator.navigateContent("/form/ShowMenuDetails.fxml");
                                    form.init(id);
                                }
                        );
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        setGraphic(empty ? null : showButton);
                    }
                }
        );

        checkboxAppetizer.setSelected(true);
        checkboxDrink.setSelected(true);
        checkboxFirst.setSelected(true);
        checkboxContour.setSelected(true);
        checkboxSecond.setSelected(true);
        checkboxDessert.setSelected(true);
    }

    class TableRow {
        private final String name;
        private final String address;
        private final String phone;
        private final String id;

        public TableRow(String name, String address, String phone, String id) {
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.id = id;
        }
    }

    @FXML
    private void clickButtonSearch(ActionEvent event) {
        try {
            view.insertCity(comboBoxCity.getValue());

            view.startSearch();

            List<String> idList = view.showRestaurantID();

            tableRestaurant.getItems().clear();
            for (String i : idList) {
                tableRestaurant.getItems().add(new TableRow(
                        view.showRestaurantName(i),
                        view.showRestaurantAddress(i),
                        view.showRestaurantPhone(i),
                        i
                ));
            }
        } catch (GenericViewException e) {
            Navigator.showError(e);
        }
    }
}
