package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class HomeViewController extends ViewController {

    @FXML
    private Label gameTitle;

    @FXML
    private CheckBox fullScreenCheckBox;

    @FXML
    protected void onButtonClick() {
        ViewManager.getInstance().navigateTo(VIEWS.GAME);
    }

    @FXML
    protected void onFullScreenToggle() {
        ViewManager.getInstance().setFullScreen(fullScreenCheckBox.isSelected());
    }

    public void setGameTitle(String title) {
        this.gameTitle.setText(title);
    }

    public void setFullScreenCheckBox(boolean fullScreen) {
        this.fullScreenCheckBox.setSelected(fullScreen);
        ViewManager.getInstance().setFullScreen(fullScreen);
    }
}
