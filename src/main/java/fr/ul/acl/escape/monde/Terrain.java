package fr.ul.acl.escape.monde;

public abstract class Terrain extends ElementMonde{
    public Terrain(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
    }

    public boolean estTraversable(){
        return false;
    }
}
