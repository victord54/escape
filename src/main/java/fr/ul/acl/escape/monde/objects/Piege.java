package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Piege extends Objet {
    protected double degats;

    public Piege(double x, double y, double hauteur, double largeur, double degats) {
        super(Type.TRAP, x, y, hauteur, largeur, false);
        this.degats = degats;
        sprite = new Sprite("assets/spike.png", 0, 0, 191, 107);
    }

    public Piege(JSONObject json) {
        super(json);
        this.degats = json.getDouble("degat");
        sprite = new Sprite("assets/spike.png", 0, 0, 191, 107);
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
    public Color getColor() {
        return Color.RED;
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

    @Override
    public boolean estRamassable(){
        return false;
    }
}
