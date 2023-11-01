package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeViewController extends ViewController {
    @FXML
    private Label title;
    @FXML
    private Button startButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button quitButton;

    @FXML
    private void onClickStart() {
        ViewManager.getInstance().navigateTo(VIEWS.GAME);
    }

    @FXML
    private void onClickSettings() {
        ViewManager.getInstance().navigateTo(VIEWS.SETTINGS);
    }

    @FXML
    private void onClickQuit() {
        ViewManager.getInstance().quit();
    }

    @Override
    public void applyLanguage() {
        title.setText(Resources.getI18NString("game.title"));
        startButton.setText(Resources.getI18NString("game.start"));
        settingsButton.setText(Resources.getI18NString("settings"));
        quitButton.setText(Resources.getI18NString("quit"));
    }
}
