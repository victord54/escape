package fr.ul.acl.escape.monde.objects;

import org.json.JSONObject;

public class Coeur extends Objet {
    protected double valeur;
    public Coeur(double x, double y, double hauteur, double largeur, double valeur){
        super(Type.HEART, x,y,hauteur,largeur);
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
        return 'C';
    }

    public double getValeur(){
        return valeur;
    }

    public boolean estCoeur(){
        return true;
    }

}
