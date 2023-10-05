package fr.ul.acl.escape.outils;

/**
 * Static game information common to all classes.
 */
public final class Donnees {

    /**
     *
     * @return height of game screen in box
     */
    public static int hauteurMonde(){return 7;}

    /**
     *
     * @return length of the game screen in boxes
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
}
