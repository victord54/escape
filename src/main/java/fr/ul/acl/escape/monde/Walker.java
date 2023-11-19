package fr.ul.acl.escape.monde;

import static fr.ul.acl.escape.outils.Donnees.*;

public class Walker extends Monstre {

    public Walker(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur, WALKER_SPEED);
        coeurs = WALKER_HEART;
        degats = WALKER_HIT;
        delayAttaque = WALKER_HIT_COUNTDOWN;
    }

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur, vitesse, id);
        coeurs = WALKER_HEART;
    }
}
