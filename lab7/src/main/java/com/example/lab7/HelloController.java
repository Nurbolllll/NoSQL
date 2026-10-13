package com.example.lab7;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloController {

    @FXML private BorderPane rootPane;
    @FXML private TextArea txtEditor;
    @FXML private Label lblStatus;
    @FXML private MenuItem miNew;
    @FXML private MenuItem miOpen;
    @FXML private MenuItem miSave;

    private Path currentFile = null;

    @FXML
    public void initialize() {
        miNew.setAccelerator(javafx.scene.input.KeyCombination.keyCombination("Shortcut+N"));
        miOpen.setAccelerator(javafx.scene.input.KeyCombination.keyCombination("Shortcut+O"));
        miSave.setAccelerator(javafx.scene.input.KeyCombination.keyCombination("Shortcut+S"));

        txtEditor.textProperty().addListener((obs, oldText, newText) -> {
            lblStatus.setText(" Символов: " + newText.length());
        });
    }

    @FXML
    private void onLightTheme() {
        rootPane.getStyleClass().remove("dark-theme");
        lblStatus.setText(" Установлена светлая тема");
    }

    @FXML
    private void onDarkTheme() {
        if (!rootPane.getStyleClass().contains("dark-theme")) {
            rootPane.getStyleClass().add("dark-theme");
        }
        lblStatus.setText(" Установлена тёмная тема");
    }

    @FXML
    private void onNew() {
        txtEditor.clear();
        currentFile = null;
        lblStatus.setText(" Новый документ");
    }

    @FXML
    private void onOpen() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt")
        );
        File file = chooser.showOpenDialog(txtEditor.getScene().getWindow());
        if (file == null) return;

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            txtEditor.setText(content);
            currentFile = file.toPath();
            lblStatus.setText(" Открыт: " + file.getName());
        } catch (IOException ex) {
            showError("Ошибка открытия файла: " + ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        if (currentFile == null) {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt")
            );
            File file = chooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file == null) return;
            currentFile = file.toPath();
        }

        try {
            Files.writeString(currentFile, txtEditor.getText(), StandardCharsets.UTF_8);
            lblStatus.setText(" Сохранено: " + currentFile.getFileName());
        } catch (IOException ex) {
            showError("Ошибка сохранения файла: " + ex.getMessage());
        }
    }

    @FXML private void onCopy() { txtEditor.copy(); }
    @FXML private void onCut() { txtEditor.cut(); }
    @FXML private void onPaste() { txtEditor.paste(); }
    @FXML private void onExit() { Platform.exit(); }

    @FXML
    private void onAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Редактор заметок (Вариант 6: Темы). Лабораторная работа №7.", ButtonType.OK);
        a.setHeaderText("О программе");
        a.showAndWait();
    }

    private void showError(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.setHeaderText("Ошибка");
        a.showAndWait();
    }
}