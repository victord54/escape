package fr.ul.acl.escape.monde;

public class Mur extends Terrain {
    public Mur(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
    }

    public boolean estTraversable() {
        return false;
    }
}
