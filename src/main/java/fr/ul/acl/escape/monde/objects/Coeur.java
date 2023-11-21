package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.entities.Personnage;
import org.json.JSONObject;

public class Coeur extends Objet {
    protected double valeur;

    public Coeur(double x, double y, double hauteur, double largeur, double valeur) {
        super(Type.HEART, x, y, hauteur, largeur, true);
        this.valeur = valeur;
    }

    public Coeur(JSONObject json) {
        super(json);
        this.valeur = json.getDouble("value");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("value", valeur);
        return json;
    }


    @Override
    public char getSymbol() {
        return '♥';
    }

    public double getValeur() {
        return valeur;
    }

    public void consommePar(Personnage p) {
        p.coeursGagne(valeur);
    }

    @Override
    public boolean estConsommable() {
        return true;
    }

    @Override
    public boolean estCoeur() {
        return true;
    }
}
