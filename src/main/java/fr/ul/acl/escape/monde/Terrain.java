package fr.ul.acl.escape.monde;

public abstract class Terrain extends ElementMonde {
    public Terrain(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x, y, hauteur, largeur);
    }

    public boolean estTraversable() {
        return true;
    }
}
