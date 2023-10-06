package fr.ul.acl.escape;

import javafx.application.Application;

/**
 * Just a no {@link Application Application} class to launch the game.
 */
public class Launcher {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--nowindow")) Escape.main(args);
        else Application.launch(Escape.class, args);
    }
}
