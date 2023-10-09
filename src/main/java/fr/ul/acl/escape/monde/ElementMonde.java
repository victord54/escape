package fr.ul.acl.escape.monde;

public abstract class ElementMonde {

    protected final int hauteur;
    protected final int largeur;
    protected float x, y;

    public ElementMonde(float x, float y, int hauteur, int largeur) {
        this.x = x;
        this.y = y;
        this.hauteur = hauteur;
        this.largeur = largeur;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    @Override
    public String toString() {
        return "ElementMonde{" + "x=" + x + ", y=" + y + ", hauteur=" + hauteur + ", largeur=" + largeur + '}';
    }
}
