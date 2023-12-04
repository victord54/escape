package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class SettingsViewController extends ViewController {
    @FXML
    private Label settingsTitle;
    @FXML
    private CheckBox fullScreenCheckBox;
    @FXML
    private Label settingsLanguage;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Button resetButton;
    @FXML
    private Button openFolderButton;
    @FXML
    private Button backButton;

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
        settingsTitle.setText(Resources.getI18NString("settings"));
        fullScreenCheckBox.setText(Resources.getI18NString("settings.fullscreen"));
        settingsLanguage.setText(Resources.getI18NString("settings.language"));
        resetButton.setText(Resources.getI18NString("settings.reset"));
        openFolderButton.setText(Resources.getI18NString("settings.openFolder"));
        backButton.setText(Resources.getI18NString("back"));
    }

    public void resetFocus() {
        backButton.requestFocus();
    }
}
