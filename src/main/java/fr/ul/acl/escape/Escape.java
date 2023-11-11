package fr.ul.acl.escape;

import fr.ul.acl.escape.cli.CLI;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.views.GameView;
import fr.ul.acl.escape.gui.views.HomeView;
import fr.ul.acl.escape.gui.views.SettingsView;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Escape extends Application {
    /**
     * CLI entry point.
     */
    public static void main(String[] args) {
        new CLI();
    }

    /**
     * GUI entry point.
     */
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.getInstance().setStage(stage);

        // Register the views of the game
        ViewManager.getInstance().registerView(VIEWS.HOME, new HomeView());
        ViewManager.getInstance().registerView(VIEWS.GAME, new GameView());
        ViewManager.getInstance().registerView(VIEWS.SETTINGS, new SettingsView());

        // Set the default view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);

        // Apply settings
        Settings.load();

        // Show window
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }
}
