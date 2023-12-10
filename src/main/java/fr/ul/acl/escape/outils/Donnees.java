package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Launcher;
import net.harawata.appdirs.AppDirsFactory;

import java.util.*;

import static fr.ul.acl.escape.Escape.javaFXApplication;

/**
 * Static game information common to all classes.
 */
public final class Donnees {
    /**
     * Folder where the game data is stored.
     */
    public static final String APPDATA_FOLDER = AppDirsFactory.getInstance().getUserDataDir("Escape", null, "UL");
    /**
     * If the game is in debug mode.
     */
    public static final boolean DEBUG = Launcher.getArgs() != null && Launcher.getArgs().contains("--debug");
    /**
     * If the game is in CLI mode.
     */
    public static final boolean CLI_MODE = !javaFXApplication;
    /**
     * The supported locales.
     */
    public static final Set<Locale> SUPPORTED_LOCALES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Locale.ENGLISH,
            Locale.FRANCE,
            Locale.CANADA_FRENCH
    )));

    /**
     * Name of the file in the resources containing the example level.
     */
    public static final String EXAMPLE_LEVEL_FILE = "maps/exampleCustomLevel.json";

    /**
     * hero hit damage
     */
    public static final double HERO_HIT = 1;
    /**
     * countdown between two hero hit in ms
     */
    public static final double HERO_HIT_COUNTDOWN = 500;

    /**
     * total hero training duration in ms
     */
    public static final int HERO_TRAINING_DURATION = 4_000;

    /**
     * Walker hit damage
     */
    public static final double WALKER_HIT = 1;

    /**
     * Walker hit damage
     */
    public static final double MONSTER_HIT_COUNTDOWN = 1;

    /**
     * Default width of the window when the game starts.
     */
    public static final int WINDOW_DEFAULT_WIDTH = 800;
    /**
     * Default height of the window when the game starts.
     */
    public static final int WINDOW_DEFAULT_HEIGHT = 600;

    /**
     * Conversion factor to transform float to int.
     */
    public static final int CONVERSION_FACTOR = 10_000;
    /**
     * height of heart
     */
    public static final double HEART_HEIGHT = 0.3;

    /**
     * width of heart
     */
    public static final double HEART_WIDTH = 0.3;
    /**
     * The value of a heart.
     */
    public static final double HEART_VALUE = 1.0;

    /**
     * The chance of a heart drop by a Monstre.
     */
    public static final double CHANCE_OF_HEART_DROP = 0.4;

    private Donnees() {
    }
}
