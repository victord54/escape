package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

public class Heros extends Personnage {
    public Heros(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(ElementMonde.Type.HERO, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    public Heros(JSONObject json) {
        super(json);
    }

    @Override
    public char getSymbol() {
        return 'H';
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }

    @Override
    public Heros clone() {
        return new Heros(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }
}
