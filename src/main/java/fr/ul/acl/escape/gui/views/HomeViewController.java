package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import javafx.fxml.FXML;

public class HomeViewController extends ViewController {

    @FXML
    protected void onClickStart() {
        ViewManager.getInstance().navigateTo(VIEWS.GAME);
    }

    @FXML
    protected void onClickSettings() {
        ViewManager.getInstance().navigateTo(VIEWS.SETTINGS);
    }

    @FXML
    protected void onClickQuit() {
        ViewManager.getInstance().quit();
    }
}
