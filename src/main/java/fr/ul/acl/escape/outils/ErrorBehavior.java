package fr.ul.acl.escape.outils;

import javafx.scene.control.Alert;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;

/**
 * Unifies the error handling of the application.
 */
public class ErrorBehavior {
    /**
     * Crash the application.
     *
     * @param e       The exception that caused the crash.
     * @param message The message to display.
     */
    public static void crash(Exception e, String message) {
        handle(e, message);
        if (!Donnees.CLI_MODE) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(message);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        System.exit(1);
    }

    /**
     * Handle an exception.
     *
     * @param e       The exception to handle.
     * @param message The message to display.
     */
    public static void handle(Exception e, String message) {
        System.err.println(message);
        if (DEBUG) e.printStackTrace();
    }
}
