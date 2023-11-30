package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class GameModeView extends View {
    public GameModeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game-mode-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }
}
