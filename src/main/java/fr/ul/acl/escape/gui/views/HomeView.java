package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class HomeView extends View {
    /**
     * The title of the game that will be displayed in the home view.
     */
    private final String gameTitle;

    public HomeView(String gameTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/home-view.fxml"));
        this.root = loader.load();
        this.controller = loader.getController();

        this.gameTitle = gameTitle;
    }

    @Override
    public void onViewInit() {
        ((HomeViewController) controller).setGameTitle(gameTitle);
        ((HomeViewController) controller).setFullScreenCheckBox(Settings.getInstance().isFullScreen());
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ESCAPE")) {
            ((HomeViewController) controller).setFullScreenCheckBox(false);
        }
    }
}
