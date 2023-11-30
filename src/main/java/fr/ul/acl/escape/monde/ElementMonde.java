package fr.ul.acl.escape.monde;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
        this.initSprites();
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
        this.initSprites();
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
    public abstract String getSymbol();

    /**
     * @return the color of the ElementMonde if the sprite is not available
     */
    public abstract Color getColor();

    /**
     * @param index the index of the sprite
     * @return the sprite at the given index
     */
    public abstract Image getSprite(int index);

    /**
     * Initialize the sprites of the ElementMonde.
     * This method is automatically called by the constructor.
     * It should be used to initialize the sprites of a concrete type of ElementMonde, but one for all instances of this type.
     */
    protected abstract void initSprites();

    /**
     * Concrete types of ElementMonde
     * Used for JSON serialization ONLY!
     */
    public enum Type {
        HERO, WALKER, NOT_SERIALIZABLE, WALL, HEART, TRAP, GHOST
    }
}
