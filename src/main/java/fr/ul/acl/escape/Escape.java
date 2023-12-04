package fr.ul.acl.escape;

import fr.ul.acl.escape.cli.CLI;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.views.*;
import fr.ul.acl.escape.outils.ErrorBehavior;
import fr.ul.acl.escape.outils.Resources;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Escape extends Application {
    public static boolean javaFXApplication = false;
    public static HostServices Host;

    /**
     * CLI entry point.
     */
    public static void main(String[] args) {
        try {
            // Apply settings
            Settings.load();

            // Start the CLI
            new CLI();
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Unexpected error");
        }
    }

    /**
     * GUI entry point.
     */
    @Override
    public void start(Stage stage) {
        try {
            javaFXApplication = true;
            Host = getHostServices();

            ViewManager.getInstance().setStage(stage);

            // Register the views of the game
            ViewManager.getInstance().registerView(VIEWS.HOME, new HomeView());
            ViewManager.getInstance().registerView(VIEWS.GAME, new GameView());
            ViewManager.getInstance().registerView(VIEWS.SETTINGS, new SettingsView());
            ViewManager.getInstance().registerView(VIEWS.SAVES, new SavesView());
            ViewManager.getInstance().registerView(VIEWS.GAME_MODE, new GameModeView());
            ViewManager.getInstance().registerView(VIEWS.LEVELS, new LevelsView());

            // Set the default view
            ViewManager.getInstance().navigateTo(VIEWS.HOME);

            // Apply settings
            Settings.load();

            // Validate environment
            Dotenv dotenv;
            try {
                dotenv = Dotenv.load();
            } catch (Exception e) {
                ErrorBehavior.crash(e, "Failed to load .env file");
                return;
            }
            if (dotenv.get("ENCRYPTION_KEY") == null || dotenv.get("ENCRYPTION_KEY").isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(Resources.getI18NString("warning.noEncryptionKey"));
                alert.setHeaderText(Resources.getI18NString("warning.noEncryptionKey.message"));
                alert.setContentText(Resources.getI18NString("warning.noEncryptionKey.details"));
                alert.showAndWait();
            }

            // Attach icon
            stage.getIcons().add(Resources.getAsset("assets/icon.png"));

            // Show window
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Unexpected error");
        }
    }
}
