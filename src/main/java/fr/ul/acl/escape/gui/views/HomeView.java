package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class HomeView extends View {
    public HomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/home-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            ViewManager.getInstance().setFullScreen(false);
        }
    }
}
