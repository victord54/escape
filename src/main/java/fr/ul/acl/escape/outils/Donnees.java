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
     * The width of the game screen in boxes.
     */
    public static int longeurMonde(){return 13;}

    /**
     *
     * @return symbol of Hero/player
     */
    public static char symboleHero(){return 'H';}

    /**
     *
     * @return symbol of monster : walker
     */
    public static char symboleWalker(){return 'W';}

    /**
     *
     * @return symbol of wall
     */
    public static char symboleMur(){return 'M';}

    /**
     *
     * @return symbol of hole
     */
    public static char symboleTrou(){return 'T';}

    /**
     *
     * @return height of wall
     */
    public static int hauteurMur(){return 1;}

    /**
     *
     * @return width of wall
     */
    public static int largeurMur(){return 1;}

    /**
     *
     * @return height of hero
     */
    public static int hauteurHero(){return 1;}

    /**
     *
     * @return width of hero
     */
    public static int largeurHero(){return 1;}

    /**
     *
     * @return height of monster
     */
    public static int hauteurWalker(){return 1;}

    /**
     *
     * @return width of hero
     */
    public static int largeurWalker(){return 1;}

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

    private Donnees() {
    }
}
