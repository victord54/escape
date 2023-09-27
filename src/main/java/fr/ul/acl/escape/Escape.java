package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.views.GameViewEvents;
import fr.ul.acl.escape.gui.views.HomeViewEvents;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Escape extends Application {
    public static final String GAME_TITLE = "Escape";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.getInstance().setStage(stage);

        // Register the views of the game
        ViewManager.getInstance().views.put(VIEWS.HOME, new View(
                new FXMLLoader(getResource("gui/home-view.fxml")),
                new HomeViewEvents(GAME_TITLE)
        ));
        ViewManager.getInstance().views.put(VIEWS.GAME, new View(
                new FXMLLoader(getResource("gui/game-view.fxml")),
                new GameViewEvents()
        ));

        // Set the default view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);

        // Show window
        stage.setTitle(GAME_TITLE);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    /**
     * Get a resource from the 'resources/fr/ul/acl/escape' directory.
     *
     * @param path The path of the resource.
     * @return The URL of the resource.
     */
    public static URL getResource(String path) {
        return Escape.class.getResource(path);
    }
}