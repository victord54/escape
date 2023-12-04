package fr.ul.acl.escape.gui;


import fr.ul.acl.escape.outils.Resources;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpriteSheet {
    private final Image spriteSheet;

    public SpriteSheet(String path) {
        spriteSheet = Resources.getAsset(path);
    }

    public Image get() {
        return spriteSheet;
    }

    public Image get(int x, int y, int width, int height) {
        if (spriteSheet == null) return null;
        return new WritableImage(spriteSheet.getPixelReader(), x, y, width, height);
    }
}
