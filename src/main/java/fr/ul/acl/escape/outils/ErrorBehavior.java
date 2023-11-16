package fr.ul.acl.escape.outils;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;

/**
 * Unifies the error handling of the application.
 */
// TODO: show a dialog in GUI mode
public class ErrorBehavior {
    /**
     * Crash the application.
     *
     * @param e       The exception that caused the crash.
     * @param message The message to display.
     */
    public static void crash(Exception e, String message) {
        handle(e, message);
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
