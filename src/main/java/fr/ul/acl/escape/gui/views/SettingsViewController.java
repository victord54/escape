package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class SettingsViewController extends ViewController {
    @FXML
    private CheckBox fullScreenCheckBox;

    @FXML
    private void onFullScreenToggle() {
        Settings.FULL_SCREEN.set(fullScreenCheckBox.isSelected());
    }

    @FXML
    private void onClickBack() {
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    public void setFullScreenCheckBox(boolean fullScreen) {
        this.fullScreenCheckBox.setSelected(fullScreen);
    }
}
