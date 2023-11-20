package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

public abstract class Objet extends ElementMonde {

    public Objet(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x,y,hauteur,largeur);
    }

    public Objet(JSONObject json) {
        super(json);
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON();
    }
    public static Objet fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.HEART) {
            return new Coeur(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public boolean estCoeur(){
        return false;
    }
}
