package fr.ul.acl.escape.monde.entities;

import org.json.JSONObject;

public class Walker extends Monstre {
    public Walker(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, id);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    public Walker(JSONObject json) {
        super(json);
    }

    @Override
    public Walker clone() {
        return new Walker(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, id);
    }
}
