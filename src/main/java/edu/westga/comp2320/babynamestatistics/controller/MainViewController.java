package edu.westga.comp2320.babynamestatistics.controller;

import edu.westga.comp2320.babynamestatistics.model.CsvFileHandler;
import edu.westga.comp2320.babynamestatistics.model.NameRecord;
import edu.westga.comp2320.babynamestatistics.model.NameRecordManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the main view of the Baby Name Statistics application.
 *
 * @author Raphael Rao
 */
public class MainViewController {

    public MenuItem openMenuItem;
    public MenuItem saveMenuItem;
    public MenuItem aboutMenuItem;
    @FXML private TextField nameField;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;
    @FXML private TextField yearField;
    @FXML private TextField frequencyField;
    @FXML private TextField popularYearField;
    @FXML private Label femalePopularLabel;
    @FXML private Label malePopularLabel;

    @FXML private Button searchButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button deleteAllButton;

    @FXML private ListView<NameRecord> recordsListView;


    private final NameRecordManager manager = new NameRecordManager();
    private final ObservableList<NameRecord> displayedRecords = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        this.recordsListView.setItems(this.displayedRecords);
        this.setupButtonStates();
        this.setupSelectionPopulatesForm();
        this.setupPopularNamesPanel();
    }

    private void setupPopularNamesPanel() {
        this.femalePopularLabel.setText("");
        this.malePopularLabel.setText("");
        this.popularYearField.focusedProperty().addListener((_, _, isFocused) -> {
            if (!isFocused) {
                this.updatePopularNames();
            }
        });
    }

    private void updatePopularNames() {
        String text = this.popularYearField.getText();
        if (text == null || text.isBlank()) {
            this.femalePopularLabel.setText("");
            this.malePopularLabel.setText("");
            return;
        }
        try {
            int year = Integer.parseInt(text.trim());
            if (year < 0) {
                this.femalePopularLabel.setText("");
                this.malePopularLabel.setText("");
                return;
            }
            List<NameRecord> topFemale = this.manager.topNFor('F', year, 3);
            List<NameRecord> topMale = this.manager.topNFor('M', year, 3);
            this.femalePopularLabel.setText(formatTopList(topFemale));
            this.malePopularLabel.setText(formatTopList(topMale));
        } catch (NumberFormatException ex) {
            this.femalePopularLabel.setText("");
            this.malePopularLabel.setText("");
        }
    }

    private String formatTopList(List<NameRecord> records) {
        if (records.isEmpty()) {
            return "(no data)";
        }
        StringBuilder builder = new StringBuilder();
        for (NameRecord record : records) {
            builder.append(record.getName())
                    .append(" (").append(record.getFrequency()).append(")\n");
        }
        return builder.toString().stripTrailing();
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
        this.displayedRecords.setAll(this.manager.getAllSorted());
    }

    @FXML
    private void onSearch() {
        try {
            String name = this.nameField.getText();
            Character gender = this.getSelectedGender();
            Integer year = parseOptionalInt(this.yearField.getText());
            Integer frequency = parseOptionalInt(this.frequencyField.getText());

            List<NameRecord> results = this.manager.search(name, gender, year, frequency);
            NameRecordManager.sortRecords(results);
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
            if (gender == null) {
                this.showError("Please select a gender.");
                return;
            }
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
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open CSV File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(this.recordsListView.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            CsvFileHandler.load(file, this.manager);
            this.refreshList();
        } catch (IOException ex) {
            this.showError("Could not load file: " + ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showSaveDialog(this.recordsListView.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            CsvFileHandler.save(file, this.manager);
        } catch (IOException ex) {
            this.showError("Could not save file: " + ex.getMessage());
        }
    }

    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Baby Name Frequency App");
        alert.setHeaderText("Message");
        alert.setContentText("""
                Manages the popularity data of baby names over the years.
                
                Author: Raphael Rao""");
        alert.showAndWait();
    }
}