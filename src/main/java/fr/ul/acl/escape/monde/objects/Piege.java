package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Piege extends Objet {
    private static Image sprite;

    protected double degats;

    public Piege(double x, double y, double hauteur, double largeur, double degats) {
        super(Type.TRAP, x, y, hauteur, largeur, false);
        this.degats = degats;
    }

    public Piege(JSONObject json) {
        super(json);
        this.degats = json.getDouble("damage");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("damage", degats);
        return json;
    }


    @Override
    public String getSymbol() {
        if (!visible) {
            return " "; // Invisible
        }
        return "✘"; // Red foreground
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public Image getSprite(int i) {
        return sprite;
    }

    @Override
    public void consommePar(Personnage p) {
        this.visible = true;
        p.coeursPerdu(degats);
    }

    @Override
    public boolean estDeclenchable() {
        return !visible;
    }

    @Override
    protected void initSprites() {
        if (sprite != null) {
            return;
        }

        String path = "assets/spike.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        sprite = spriteSheet.get(0, 0, 191, 107);
    }
}
