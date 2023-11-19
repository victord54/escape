package fr.ul.acl.escape.monde.entities;

import org.json.JSONObject;

import static fr.ul.acl.escape.outils.Donnees.WALKER_HEART;
import static fr.ul.acl.escape.outils.Donnees.WALKER_SPEED;

public class Walker extends Monstre {
    public Walker(double x, double y, double hauteur, double largeur) {
        super(Type.WALKER, x, y, hauteur, largeur, WALKER_SPEED);
        coeurs = WALKER_HEART;
    }

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, id);
        coeurs = WALKER_HEART;
    }

    public Walker(JSONObject json) {
        super(json);
    }
}
