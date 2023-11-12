package fr.ul.acl.escape.monde;

import org.json.JSONObject;

public abstract class ElementMonde {
    /**
     * ElementMonde type, private use only, for JSON serialization
     */
    private final Type type;

    protected final double hauteur;
    protected final double largeur;
    protected double x, y;

    public ElementMonde(Type type, double x, double y, double hauteur, double largeur) {
        this.type = type;
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

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("x", x);
        json.put("y", y);
        json.put("height", hauteur);
        json.put("width", largeur);
        json.put("type", type);
        return json;
    }

    public enum Type {
        HERO, WALKER, WALL
    }
}
