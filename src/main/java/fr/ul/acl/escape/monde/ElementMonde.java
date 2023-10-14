package fr.ul.acl.escape.monde;

public abstract class ElementMonde {

    protected final double hauteur;
    protected final double largeur;
    protected double x, y;

    public ElementMonde(double x, double y, double hauteur, double largeur) {
        this.x = x;
        this.y = y;
        this.hauteur = hauteur;
        this.largeur = largeur;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    @Override
    public String toString() {
        return "ElementMonde{" + "x=" + x + ", y=" + y + ", hauteur=" + hauteur + ", largeur=" + largeur + '}';
    }
}
