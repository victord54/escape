package fr.ul.acl.escape.monde;

import org.json.JSONObject;

public abstract class ElementMonde {
    /**
     * ElementMonde type, private use only, for JSON serialization
     */
    private final Type type;
    protected double hauteur;
    protected double largeur;
    protected double x, y;

    public ElementMonde(Type type, double x, double y, double hauteur, double largeur) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.hauteur = hauteur;
        this.largeur = largeur;
    }

    /**
     * Create an ElementMonde from a JSON object
     * No checks are made on the JSON object
     *
     * @param json the JSON object
     */
    public ElementMonde(JSONObject json) {
        this.type = Type.valueOf(json.getString("type"));
        this.x = json.getDouble("x");
        this.y = json.getDouble("y");
        this.hauteur = json.getDouble("height");
        this.largeur = json.getDouble("width");
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("x", x);
        json.put("y", y);
        json.put("height", hauteur);
        json.put("width", largeur);
        return json;
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

    /**
     * @return a char representing the ElementMonde (for the console)
     */
    public abstract char getSymbol();

    /**
     * Concrete types of ElementMonde
     * Used for JSON serialization ONLY!
     */
    public enum Type {
        HERO, WALKER, NOT_SERIALIZABLE, WALL, HEART, TRAP
    }
}
