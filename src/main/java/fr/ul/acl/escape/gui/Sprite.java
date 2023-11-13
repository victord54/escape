package fr.ul.acl.escape.gui;


import fr.ul.acl.escape.outils.Resources;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Sprite {
    Image spriteSheet;

    public Sprite(String path, double x, double y, double width, double height) {
        spriteSheet = Resources.getAsset(path);
        WritableImage imageRewrite = new WritableImage(spriteSheet.getPixelReader(), (int) x, (int) y, (int) width, (int) height);
        spriteSheet = imageRewrite;
    }

    public Image getSprite() {
        return spriteSheet;
    }
}
