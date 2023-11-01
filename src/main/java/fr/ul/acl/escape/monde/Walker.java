package fr.ul.acl.escape.monde;

import static fr.ul.acl.escape.outils.Donnees.WALKER_SPEED;

public class Walker extends Monstre {

    public Walker(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur, WALKER_SPEED);
    }

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, int id){
        super(x, y, hauteur, largeur, WALKER_SPEED, id);
    }
}
