package fr.ul.acl.escape.outils;

import java.util.*;

/**
 * Static game information common to all classes.
 */
public final class Donnees {
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
    public static final double HERO_HEIGHT = 0.9;
    /**
     * width of hero
     */

    public static final double HERO_WIDTH = 0.9;
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
    public static final double WALKER_WIDTH = 0.9;
    /**
     * speed of monster
     */
    public static final int WALKER_SPEED = 2;

    /**
     * Number of walker's hearts
     */
    public static final double WALKER_HEART = 3;

    /**
     * Walker hit damage
     */
    public static final double WALKER_HIT = 1;

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
     * The supported locales.
     */
    public static final Set<Locale> SUPPORTED_LOCALES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Locale.ENGLISH,
            Locale.FRENCH
    )));
    /**
     * If the game is in debug mode.
     */
    public static boolean DEBUG = false;

    /**
     * Conversion factor to transform float to int.
     */
    public static final int CONVERSION_FACTOR = 10000;

    private Donnees() {
    }
}
