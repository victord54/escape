package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

import static fr.ul.acl.escape.outils.Donnees.WALKER_HIT;

public class Walker extends Monstre {

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
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
        return new Walker(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }
}
