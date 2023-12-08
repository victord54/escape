package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Coeur extends Objet {
    private static Image sprite;

    protected final double valeur;

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
    public String getSymbol() {
        if (!visible) {
            return " "; // Invisible
        }
        return "♥";
    }

    @Override
    public Image getSprite(int i) {
        return sprite;
    }

    @Override
    public Color getColor() {
        return Color.LIGHTGREEN;
    }

    @Override
    public void consommePar(Personnage p, Monde monde) {
        p.coeursGagne(valeur);
    }

    @Override
    public boolean estConsommable() {
        return true;
    }

    @Override
    protected void initSprites() {
        if (sprite != null) {
            return;
        }

        String path = "assets/coeurs.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        sprite = spriteSheet.get(1, 1, 23, 22);
    }
}
