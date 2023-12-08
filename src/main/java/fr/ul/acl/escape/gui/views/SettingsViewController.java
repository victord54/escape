package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.KeyAction;
import fr.ul.acl.escape.KeyBindings;
import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.Map;

public class SettingsViewController extends ViewController {
    // General
    @FXML
    private Label settingsTitle;
    @FXML
    private Button resetButton, openFolderButton, backButton;

    // Display
    @FXML
    private Label settingsDisplayTitle;
    @FXML
    private CheckBox fullScreenCheckBox;
    @FXML
    private Label labelLanguage;
    @FXML
    private ComboBox<String> languageComboBox;

    // Keys
    @FXML
    private Label settingsKeysTitle;
    @FXML
    private Label labelKeyUp, labelKeyLeft, labelKeyDown, labelKeyRight, labelKeyTake, labelKeyAttack, labelKeyPause, labelKeyShowFps, labelKeyShowGrid;
    @FXML
    private TextField keyUp, keyLeft, keyDown, keyRight, keyTake, keyAttack, keyPause, keyShowFps, keyShowGrid;

    @FXML
    private void onFullScreenToggle() {
        Settings.fullScreen.set(fullScreenCheckBox.isSelected());
    }

    @FXML
    private void onClickReset() {
        Settings.reset();
    }

    @FXML
    private void openFolder() {
        FileManager.open(null, true);
    }

    @FXML
    private void onClickBack() {
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    public void setFullScreenCheckBox(boolean fullScreen) {
        this.fullScreenCheckBox.setSelected(fullScreen);
    }

    public ComboBox<String> getLanguageComboBox() {
        return languageComboBox;
    }

    @Override
    public void applyLanguage() {
        // General
        settingsTitle.setText(Resources.getI18NString("settings"));
        resetButton.setText(Resources.getI18NString("settings.reset"));
        openFolderButton.setText(Resources.getI18NString("settings.openFolder"));
        backButton.setText(Resources.getI18NString("back"));

        // Display
        settingsDisplayTitle.setText(Resources.getI18NString("settings.display"));
        fullScreenCheckBox.setText(Resources.getI18NString("settings.display.fullscreen"));
        labelLanguage.setText(Resources.getI18NString("settings.display.language"));

        // Keys
        settingsKeysTitle.setText(Resources.getI18NString("settings.keys"));
        labelKeyUp.setText(Resources.getI18NString("settings.keys.up"));
        labelKeyLeft.setText(Resources.getI18NString("settings.keys.left"));
        labelKeyDown.setText(Resources.getI18NString("settings.keys.down"));
        labelKeyRight.setText(Resources.getI18NString("settings.keys.right"));
        labelKeyTake.setText(Resources.getI18NString("settings.keys.take"));
        labelKeyAttack.setText(Resources.getI18NString("settings.keys.attack"));
        labelKeyPause.setText(Resources.getI18NString("settings.keys.pause"));
        labelKeyShowFps.setText(Resources.getI18NString("settings.keys.showFps"));
        labelKeyShowGrid.setText(Resources.getI18NString("settings.keys.showGrid"));
    }

    public void resetFocus() {
        backButton.requestFocus();
    }

    @FXML
    private void setKey(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        KeyCode code = event.getCode();
        textField.setText(code.getName());
        Settings.keyBindings.get().setKey(textField.getId(), code);
        colorConflictingKeys();
    }

    public void setKeyBindings(KeyBindings keyBindings) {
        getKeys().forEach((input, label) -> {
            KeyAction keyAction = KeyAction.fromInputId(input.getId());
            if (keyAction != null) {
                input.setText(keyBindings.getKey(keyAction).getName());
                if (keyAction.isDebugOnly()) {
                    input.setVisible(Donnees.DEBUG);
                    input.setManaged(Donnees.DEBUG);
                    label.setVisible(Donnees.DEBUG);
                    label.setManaged(Donnees.DEBUG);
                }
            }
        });
        colorConflictingKeys();
    }

    private void colorConflictingKeys() {
        List<String> conflictingKeysIds = Settings.keyBindings.get().getConflictingKeys().stream().map(KeyAction::getInputId).toList();
        for (TextField input : getKeys().keySet()) {
            if (conflictingKeysIds.contains(input.getId())) {
                if (!input.getStyleClass().contains("conflicting-key")) {
                    input.getStyleClass().add("conflicting-key");
                }
            } else {
                input.getStyleClass().remove("conflicting-key");
            }
        }

        backButton.setDisable(!conflictingKeysIds.isEmpty());
    }

    private Map<TextField, Label> getKeys() {
        return Map.of(
                keyUp, labelKeyUp,
                keyLeft, labelKeyLeft,
                keyDown, labelKeyDown,
                keyRight, labelKeyRight,
                keyTake, labelKeyTake,
                keyAttack, labelKeyAttack,
                keyPause, labelKeyPause,
                keyShowFps, labelKeyShowFps,
                keyShowGrid, labelKeyShowGrid
        );
    }
}
