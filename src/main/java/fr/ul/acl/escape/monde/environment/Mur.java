package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.ElementMonde;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Mur extends Terrain {
    private static Image sprite;

    public Mur(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.WALL, x, y, hauteur, largeur);
    }

    public Mur(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[43m[█]\u001B[0m"; // Yellow background
    }

    @Override
    public Color getColor() {
        return Color.DARKGOLDENROD;
    }

    @Override
    public Image getSprite(int i) {
        return sprite;
    }

    public boolean estTraversable() {
        return false;
    }

    @Override
    protected void initSprites() {
        if (sprite != null) {
            return;
        }

        String path = "assets/decors.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        sprite = spriteSheet.get(0, 0, 50, 50);
    }
}
