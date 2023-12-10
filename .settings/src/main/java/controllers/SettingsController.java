package controllers;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import enums.Theme;
import managers.SettingsManager;
import javafx.scene.control.TextField;

public class SettingsController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ChoiceBox<String> themeChoiceBox;

    @FXML
    private TextField chatGPTApiKey;

    @FXML
    private Button applyButton;

    @FXML
    private void initialize() {
        ObservableList<String> themeNames = FXCollections.observableArrayList();
        for (Theme theme : Theme.values()) {
            themeNames.add(theme.name());
        }
        themeChoiceBox.setItems(themeNames);
        themeChoiceBox.setValue(SceneController.getInstance().getTheme().name());

        chatGPTApiKey.setText(SettingsManager.getInstance().getProperty("chatgpt_api_key"));
    }

    @FXML
    private void applySettings() {
        try {
            final String theme = themeChoiceBox.getValue();

            SceneController.getInstance().setTheme(Theme.getTheme(theme));

            SettingsManager.getInstance().setProperty("theme", theme);
            SettingsManager.getInstance().setProperty("chatgpt_api_key", chatGPTApiKey.getText().trim());
            SettingsManager.getInstance().saveSettings();

            Notifications.create()
                    .owner(rootPane)
                    .title("Dictionary")
                    .text("Settings have been updated")
                    .showInformation();
        } catch (Exception e) {
        }
    }
}
