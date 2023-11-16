package fr.ul.acl.escape.monde.entities;

import org.json.JSONObject;

import static fr.ul.acl.escape.outils.Donnees.WALKER_SPEED;

public class Walker extends Monstre {

    public Walker(double x, double y, double hauteur, double largeur) {
        super(Type.WALKER, x, y, hauteur, largeur, WALKER_SPEED);
    }

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, id);
    }

    public static Walker fromJSON(JSONObject json) {
        return json.has("id") && json.has("speed") ? new Walker(
                json.getDouble("x"),
                json.getDouble("y"),
                json.getDouble("height"),
                json.getDouble("width"),
                json.getDouble("speed"),
                json.getInt("id")
        ) : new Walker(
                json.getDouble("x"),
                json.getDouble("y"),
                json.getDouble("height"),
                json.getDouble("width")
        );
    }
}
