package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.SpriteSheet;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BordureMonde extends Terrain {
    private static Image sprite;

    public BordureMonde(double x, double y) {
        super(Type.NOT_SERIALIZABLE, x, y, 1, 1);
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
