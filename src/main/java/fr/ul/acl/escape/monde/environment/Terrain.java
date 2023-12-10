package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.entities.Personnage;
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
        } else if (type == Type.WATER) {
            return new Eau(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    /**
     * Method which returns true if the Terrain can be crossed.
     *
     * @return true
     */
    public boolean estTraversable() {
        return true;
    }

    /**
     * Method which returns true if the Terrain is special.
     *
     * @return false
     */
    public boolean estTerrainSpecial() {
        return false;
    }

    /**
     * Method that apply the special action to a Personnage.
     *
     * @param p The Personnage to apply the special action.
     */
    public void appliqueActionSpeciale(Personnage p) {
    }

}
