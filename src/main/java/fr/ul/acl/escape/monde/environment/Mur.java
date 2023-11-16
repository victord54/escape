package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.monde.ElementMonde;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.WALL, x, y, hauteur, largeur);
    }

    public boolean estTraversable() {
        return false;
    }
}
