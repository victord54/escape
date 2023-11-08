package fr.ul.acl.escape.monde;

import static fr.ul.acl.escape.outils.Donnees.HERO_SPEED;
import static fr.ul.acl.escape.outils.Donnees.HERO_HEART;
public class Heros extends Personnage {

    public Heros(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur, HERO_SPEED);
        coeurs = HERO_HEART;
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }
}
