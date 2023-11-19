package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

public abstract class Monstre extends Personnage {
    public Monstre(ElementMonde.Type type, double x, double y, double hauteur, double largeur, double vitesse) {
        super(type, x, y, hauteur, largeur, vitesse);
    }

    public Monstre(ElementMonde.Type type, double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(type, x, y, hauteur, largeur, vitesse, id);
    }

    public Monstre(JSONObject json) {
        super(json);
    }
}
