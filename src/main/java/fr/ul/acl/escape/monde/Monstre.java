package fr.ul.acl.escape.monde;

import org.json.JSONObject;

public abstract class Monstre extends Personnage {
    public Monstre(Type type, double x, double y, double hauteur, double largeur, double vitesse) {
        super(type, x, y, hauteur, largeur, vitesse);
    }

    public Monstre(Type type, double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(type, x, y, hauteur, largeur, vitesse, id);
    }
}
