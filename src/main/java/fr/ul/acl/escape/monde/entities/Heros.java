package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;

import static fr.ul.acl.escape.outils.Donnees.HERO_HEART;
import static fr.ul.acl.escape.outils.Donnees.HERO_SPEED;

public class Heros extends Personnage {
    public Heros(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.HERO, x, y, hauteur, largeur, HERO_SPEED);
        coeurs = HERO_HEART;
    }

    public Heros(JSONObject json) {
        super(json);
    }

    @Override
    public char getSymbol() {
        return 'h';
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }
}
