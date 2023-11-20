package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.entities.Personnage;
import org.json.JSONObject;

public class Piege extends Objet {
    protected double degats;

    public Piege(double x, double y, double hauteur, double largeur, double degats) {
        super(Type.TRAP, x, y, hauteur, largeur, false);
        this.degats = degats;
    }

    public Piege(JSONObject json) {
        super(json);
        this.degats = json.getDouble("degat");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("degat", degats);
        return json;
    }

    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public void consommePar(Personnage p) {
        this.visible = true;
        p.coeursPerdu(degats);
    }

    @Override
    public boolean estPiege() {
        return true;
    }

    @Override
    public boolean estDeclenchable() {
        return !visible;
    }
}
