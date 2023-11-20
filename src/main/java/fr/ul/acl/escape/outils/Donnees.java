package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Launcher;
import net.harawata.appdirs.AppDirsFactory;

import java.util.*;

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
    public static final boolean DEBUG = Launcher.getArgs().contains("--debug");
    /**
     * The supported locales.
     */
    public static final Set<Locale> SUPPORTED_LOCALES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Locale.ENGLISH,
            Locale.FRANCE,
            Locale.CANADA_FRENCH
    )));

    /**
     * hero hit damage
     */
    public static final double HERO_HIT = 1;
    /**
     * countdown between two hero hit in ms
     */
    public static final double HERO_HIT_COUNTDOWN = 500;
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
    public static final int CONVERSION_FACTOR = 10000;

    private Donnees() {
    }
}
