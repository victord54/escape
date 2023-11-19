package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.WALL, x, y, hauteur, largeur);
    }

    public Mur(JSONObject json) {
        super(json);
    }

    @Override
    public char getSymbol() {
        return 'X';
    }

    public boolean estTraversable() {
        return false;
    }
}
