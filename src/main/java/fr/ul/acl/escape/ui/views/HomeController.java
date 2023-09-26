package fr.ul.acl.escape.ui.views;

import fr.ul.acl.escape.ui.VIEWS;
import fr.ul.acl.escape.ui.ViewController;
import fr.ul.acl.escape.ui.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class HomeController extends ViewController {

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