package fr.ul.acl.escape.outils;

/**
 * Static game information common to all classes.
 */
public final class Donnees {
    /**
     * The title of the game.
     */
    public static final String GAME_TITLE = "Escape";

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
    public static final int HERO_HEIGHT = 1;

    /**
     * width of hero
     */

    public static final int HERO_WIDTH = 1;

    /**
     * speed of hero
     */

    public static final int HERO_SPEED = 4;

    /**
     * height of monster
     */
    public static final int WALKER_HEIGHT = 1;

    /**
     * width of monster
     */
    public static final int WALKER_WIDTH = 1;

    /**
     * speed of monster
     */

    public static final double WALKER_SPEED = 0.1;

    /**
     * The height of the game screen in boxes.
     */
    public static final int WORLD_HEIGHT = 7;

    /**
     * The height of the game screen in boxes.
     */
    public static final int WORLD_WIDTH = 13;

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
