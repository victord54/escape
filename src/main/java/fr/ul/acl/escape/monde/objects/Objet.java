package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Personnage;
import org.json.JSONObject;

public abstract class Objet extends ElementMonde {
    protected boolean visible;

    public Objet(Type type, double x, double y, double hauteur, double largeur, boolean visible) {
        super(type, x, y, hauteur, largeur);
        this.visible = visible;
    }

    public Objet(JSONObject json) {
        super(json);
        this.visible = json.getBoolean("visible");
    }

    public static Objet fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.HEART) {
            return new Coeur(json);
        } else if (type == Type.TRAP) {
            return new Piege(json);
        } else if (type == Type.TRAPDOOR) {
            return new Trappe(json);
        } else if (type == Type.TRAINING) {
            return new Training(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("visible", visible);
        return json;
    }

    public boolean estRamassable() {
        return true;
    }

    public boolean estConsommable() {
        return false;
    }

    /**
     * Consume the object by the given personnage.
     * Should be called only if {@link #estConsommable()} or {@link #estDeclenchable()} is true.
     *
     * @param p the personnage
     */
    public void consommePar(Personnage p, Monde m) {
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean estDeclenchable() {
        return false;
    }

    public boolean estTrappe() {
        return false;
    }

    public boolean necessiteDureePourActivation() {
        return false;
    }

    /**
     * Called when the personnage is not on the object.
     * Should be called only if {@link #necessiteDureePourActivation()} is true.
     *
     * @param p the personnage
     */
    public void notOnObject(Personnage p) {
    }

    /**
     * Called when the personnage is on the object.
     * Should be called only if {@link #necessiteDureePourActivation()} is true.
     *
     * @param p   the personnage
     * @param now the current time in nanoseconds
     */
    public void onObject(Personnage p, long now) {
    }
}
