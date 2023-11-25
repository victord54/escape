package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.ElementMonde;
import javafx.scene.image.Image;
import org.json.JSONObject;

public abstract class Terrain extends ElementMonde {
    public Terrain(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x, y, hauteur, largeur);
    }

    public Terrain(JSONObject json) {
        super(json);
    }

    public static Terrain fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.WALL) {
            return new Mur(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public boolean estTraversable() {
        return true;
    }
}
