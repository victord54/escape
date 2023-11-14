package fr.ul.acl.escape;

import fr.ul.acl.escape.cli.CLI;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.views.GameView;
import fr.ul.acl.escape.gui.views.HomeView;
import fr.ul.acl.escape.gui.views.SavesView;
import fr.ul.acl.escape.gui.views.SettingsView;
import fr.ul.acl.escape.outils.Resources;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCombination;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.beans.EventHandler;
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
        ViewManager.getInstance().registerView(VIEWS.SAVES, new SavesView());

        // Set the default view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);

        // Apply settings
        Settings.load();

        // Validate environment
        Dotenv dotenv = Dotenv.load();
        if (dotenv.get("ENCRYPTION_KEY") == null || dotenv.get("ENCRYPTION_KEY").isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Resources.getI18NString("warning.noEncryptionKey"));
            alert.setHeaderText(Resources.getI18NString("warning.noEncryptionKey.message"));
            alert.showAndWait();
        }

        // Show window
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }
}
