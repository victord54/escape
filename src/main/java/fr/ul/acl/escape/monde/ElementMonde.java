package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.gui.Sprite;

import java.util.HashMap;

public abstract class ElementMonde {

    protected final double hauteur;
    protected final double largeur;
    protected double x, y;

    protected HashMap<TypeMouvement, Sprite[]> sprites;
    public ElementMonde(double x, double y, double hauteur, double largeur) {
        this.x = x;
        this.y = y;
        this.hauteur = hauteur;
        this.largeur = largeur;
        sprites = new HashMap<>();
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
