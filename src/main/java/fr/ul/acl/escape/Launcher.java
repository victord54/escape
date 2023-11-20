package fr.ul.acl.escape;

import javafx.application.Application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Just a no {@link Application Application} class to launch the game.
 */
public final class Launcher {
    /**
     * Immutable list of arguments passed to the program.
     */
    private static List<String> argsList;

    public static void main(String[] args) {
        argsList = Collections.unmodifiableList(Arrays.asList(args));

        if (argsList.contains("--nowindow")) Escape.main(args);
        else Application.launch(Escape.class, args);
    }

    /**
     * Returns the list of arguments passed to the program.
     *
     * @return Immutable list of arguments.
     */
    public static List<String> getArgs() {
        return argsList;
    }
}
