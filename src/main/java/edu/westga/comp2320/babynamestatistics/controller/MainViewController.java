package edu.westga.comp2320.babynamestatistics.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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

    @FXML private ListView<String> recordsListView;

    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem aboutMenuItem;

    @FXML
    private void initialize() {
    }

    @FXML
    private void onSearch() {
    }

    @FXML
    private void onAdd() {
    }

    @FXML
    private void onDelete() {
    }

    @FXML
    private void onDeleteAll() {
    }

    @FXML
    priva

    @FXML
    private void onSave() {
    }

    @FXML
    private void onAbout() {
    }te void onOpen() {
    }
}