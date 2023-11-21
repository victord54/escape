package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import org.json.JSONObject;

public abstract class Objet extends ElementMonde {
    protected boolean visible;

    protected Sprite sprite;

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

    public Image getSprite() {
        return sprite.getSprite();
    }

    /**
     * Consumn the object by the given personnage.
     *
     * @param p the personnage
     */
    public abstract void consommePar(Personnage p);

    public boolean estCoeur() {
        return false;
    }

    public boolean estPiege() {
        return false;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean estDeclenchable() {
        return false;
    }

}
