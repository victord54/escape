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
    public static final boolean DEBUG = Launcher.getArgs() != null && Launcher.getArgs().contains("--debug");
    /**
     * The supported locales.
     */
    public static final Set<Locale> SUPPORTED_LOCALES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Locale.ENGLISH,
            Locale.FRANCE,
            Locale.CANADA_FRENCH
    )));

    public static final boolean CLI_MODE = (Launcher.getArgs() != null && Launcher.getArgs().contains("--nowindow")) || isJUnitTest();

    /**
     * symbol of Hero/player
     */
    public static final char SYMBOL_HERO = 'H';
    /**
     * symbol of monster : walker
     */
    public static final char SYMBOL_WALKER = 'W';
    /**
     * symbol of wall
     */
    public static final char SYMBOL_WALL = 'M';
    /**
     * symbol of hole
     */
    public static final char SYMBOL_HOLE = 'T';
    /**
     * height of wall
     */
    public static final int WALL_HEIGHT = 1;

    /**
     * width of wall
     */
    public static final int WALL_WIDTH = 1;

    /**
     * height of hero
     */
    public static final double HERO_HEIGHT = 0.8;

    /**
     * width of hero
     */
    public static final double HERO_WIDTH = 0.8;
    /**
     * speed of hero
     */
    public static final int HERO_SPEED = 4;

    /**
     * number of hero's hearts
     */
    public static final double HERO_HEART = 3;

    /**
     * hero hit damage
     */
    public static final double HERO_HIT = 1;
    /**
     * countdown between two hero hit in ms
     */
    public static final double HERO_HIT_COUNTDOWN = 500;
    /**
     * height of monster
     */
    public static final double WALKER_HEIGHT = 0.9;
    /**
     * width of monster
     */
    public static final double WALKER_WIDTH = 0.6;
    /**
     * speed of monster
     */
    public static final int WALKER_SPEED = 2;
    /**
     * Number of walker's hearts
     */
    public static final double WALKER_HEART = 3;
    /**
     * The height of the game screen in boxes.
     */
    public static final int WORLD_HEIGHT = 12;
    /**
     * The height of the game screen in boxes.
     */
    public static final int WORLD_WIDTH = 18;
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

    private static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    private Donnees() {
    }
}
