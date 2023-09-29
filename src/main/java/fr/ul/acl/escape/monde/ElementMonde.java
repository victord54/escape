package fr.ul.acl.escape.monde;

public abstract class ElementMonde {

    protected float x, y;
    protected int hauteur, largeur;

    public ElementMonde(float x, float y, int hauteur, int largeur) {
        this.x = x;
        this.y = y;
        this.hauteur = hauteur;
        this.largeur = largeur;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ElementMonde{" +
                "x=" + x +
                ", y=" + y +
                ", hauteur=" + hauteur +
                ", largeur=" + largeur +
                '}';
    }
}
