package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.entities.Personnage;
import org.json.JSONObject;

public class Piege extends Objet{
    protected double degat;
    public Piege(double x, double y, double hauteur, double largeur, double degat) {
        super(Type.TRAP, x, y, hauteur, largeur, false);
        this.degat = degat;
    }

    public Piege(JSONObject json) {
        super(json);
        this.degat = json.getDouble("degat");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("degat", degat);
        return json;
    }
    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public void consommePar(Personnage p) {
        p.coeursPerdu(degat);
        this.visible = true;
    }

    @Override
    public boolean estPiege(){
        return true;
    }

    @Override
    public boolean estDeclenchable(){
        return !visible;
    }
}
