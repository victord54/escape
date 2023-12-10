package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.SpriteSheet;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Eau extends Terrain{
    private static Image sprite;

    public Eau(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x, y, hauteur, largeur);
    }

    public Eau(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[44m[~]\u001B[0m";
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }

    @Override
    public Image getSprite(int i) {
        return sprite;
    }

    @Override
    protected void initSprites() {
        if (sprite != null) {
            return;
        }

        String path = "assets/decors.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        sprite = spriteSheet.get(0, 50, 50, 50);
    }

    @Override
    public boolean terrainSpecial(){
        return true;
    }
}
