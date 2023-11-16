package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

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
            Settings.fullScreen.set(false);
        }
    }

    @Override
    public void onViewDisplayed() {
        super.onViewDisplayed();
        Map<String, JSONObject> saves = FileManager.readDirectory("saves", true);
        ((HomeViewController) controller).isThereSaves(!saves.isEmpty());
    }
}
