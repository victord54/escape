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
        showWarning("Error", message, e.toString(), true);
        if (DEBUG) e.printStackTrace();
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

    /**
     * Show a warning to the user.
     * If the application is in CLI mode, the warning is printed to the standard output.
     * Otherwise, a dialog is displayed.
     *
     * @param title   The title of the warning.
     * @param message The message to display.
     * @param details The details of the warning.
     * @param fatal   Whether the warning is fatal or not.
     */
    public static void showWarning(String title, String message, String details, boolean fatal) {
        if (!Donnees.CLI_MODE) {
            Alert alert = new Alert(fatal ? Alert.AlertType.ERROR : Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.setContentText(details);
            alert.showAndWait();
        } else {
            System.out.println(message);
        }
    }
}
