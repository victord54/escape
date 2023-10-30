package fr.ul.acl.escape;

import fr.ul.acl.escape.outils.Donnees;
import javafx.application.Application;

import java.util.Arrays;
import java.util.List;

/**
 * Just a no {@link Application Application} class to launch the game.
 */
public class Launcher {
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        Donnees.DEBUG = argsList.contains("--debug");

        if (argsList.contains("--nowindow")) Escape.main(args);
        else Application.launch(Escape.class, args);
    }
}
