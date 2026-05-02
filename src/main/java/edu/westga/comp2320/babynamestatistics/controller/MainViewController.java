package edu.westga.comp2320.babynamestatistics.controller;

import edu.westga.comp2320.babynamestatistics.model.NameRecord;
import edu.westga.comp2320.babynamestatistics.model.NameRecordManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller for the main view of the Baby Name Statistics application.
 *
 * @author Raphael Rao
 */
public class MainViewController {

    @FXML private TextField nameField;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;
    @FXML private TextField yearField;
    @FXML private TextField frequencyField;

    @FXML private Button searchButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button deleteAllButton;

    @FXML private ListView<NameRecord> recordsListView;

    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem aboutMenuItem;

    private final NameRecordManager manager = new NameRecordManager();
    private final ObservableList<NameRecord> displayedRecords = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        this.recordsListView.setItems(this.displayedRecords);
        this.setupButtonStates();
        this.setupSelectionPopulatesForm();
    }

    private void setupSelectionPopulatesForm() {
        this.recordsListView.getSelectionModel().selectedItemProperty()
                .addListener((_, _, newVal) -> {
                    if (newVal != null) {
                        this.nameField.setText(newVal.getName());
                        this.yearField.setText(String.valueOf(newVal.getYear()));
                        this.frequencyField.setText(String.valueOf(newVal.getFrequency()));
                        if (newVal.getGender() == 'M') {
                            this.maleRadio.setSelected(true);
                        } else {
                            this.femaleRadio.setSelected(true);
                        }
                    }
                });
    }

    private void setupButtonStates() {
        this.addButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> this.nameField.getText().isBlank()
                        || this.yearField.getText().isBlank()
                        || this.frequencyField.getText().isBlank()
                        || this.genderGroup.getSelectedToggle() == null,
                this.nameField.textProperty(),
                this.yearField.textProperty(),
                this.frequencyField.textProperty(),
                this.genderGroup.selectedToggleProperty()
        ));

        this.searchButton.disableProperty().bind(Bindings.createBooleanBinding(
                this.manager::isEmpty,
                this.displayedRecords
        ));
        this.deleteAllButton.disableProperty().bind(Bindings.createBooleanBinding(
                this.manager::isEmpty,
                this.displayedRecords
        ));

        this.deleteButton.disableProperty().bind(
                this.recordsListView.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private Character getSelectedGender() {
        if (this.maleRadio.isSelected()) {
            return 'M';
        }
        if (this.femaleRadio.isSelected()) {
            return 'F';
        }
        return null;
    }

    private void refreshList() {
        this.displayedRecords.setAll(this.manager.getAll());
    }

    @FXML
    private void onSearch() {
        try {
            String name = this.nameField.getText();
            Character gender = this.getSelectedGender();
            Integer year = parseOptionalInt(this.yearField.getText());
            Integer frequency = parseOptionalInt(this.frequencyField.getText());

            List<NameRecord> results = this.manager.search(name, gender, year, frequency);
            this.displayedRecords.setAll(results);
        } catch (NumberFormatException ex) {
            this.showError("Year and Frequency must be valid integers.");
        }
    }

    private Integer parseOptionalInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value.trim());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText("Invalid Input");
        alert.showAndWait();
    }

    @FXML
    private void onAdd() {
        try {
            String name = this.nameField.getText().trim();
            Character gender = this.getSelectedGender();
            int year = Integer.parseInt(this.yearField.getText().trim());
            int frequency = Integer.parseInt(this.frequencyField.getText().trim());

            NameRecord record = new NameRecord(name, gender, year, frequency);
            boolean added = this.manager.add(record);
            if (!added) {
                this.showError("A record with this name, gender, and year already exists.");
                return;
            }
            this.refreshList();
        } catch (NumberFormatException ex) {
            this.showError("Year and Frequency must be valid integers.");
        } catch (IllegalArgumentException ex) {
            this.showError(ex.getMessage());
        }

    }

    @FXML
    private void onDelete() {
        NameRecord selected = this.recordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            this.manager.remove(selected);
            this.refreshList();
        }
    }

    @FXML
    private void onDeleteAll() {
        this.manager.clear();
        this.refreshList();
    }

    @FXML
    private void onOpen() {
    }

    @FXML
    private void onSave() {
    }

    @FXML
    private void onAbout() {
    }
}