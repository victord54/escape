package fr.ul.acl.escape.monde;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(Type.WALL, x, y, hauteur, largeur);
    }

    public boolean estTraversable() {
        return false;
    }
}
