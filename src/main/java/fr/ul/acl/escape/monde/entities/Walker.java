package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.TypeMouvement;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Walker extends Monstre {
    private static Map<TypeMouvement, Image[]> sprites;

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, double maxVitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, maxVitesse, coeurs, maxCoeurs, degats, id);
    }

    public Walker(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[45m[ ]\u001B[0m"; // Magenta background
    }

    @Override
    public Color getColor() {
        return Color.MEDIUMPURPLE;
    }

    @Override
    public Image getSprite(int i) {
        if (sprites == null) return null;
        return sprites.get(dernierMouvement)[i];
    }

    @Override
    public Walker clone() {
        return new Walker(x, y, hauteur, largeur, vitesse, maxVitesse, coeurs, maxCoeurs, degats, id);
    }

    @Override
    protected void initSprites() {
        if (sprites != null) {
            return;
        }

        Map<TypeMouvement, Image[]> tmpSprites = new HashMap<>();

        String path = "assets/squelettes.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        Image[] tab_sprite_down = new Image[3];
        tab_sprite_down[0] = spriteSheet.get(6, 0, 21, 47);
        tab_sprite_down[1] = spriteSheet.get(38, 0, 21, 47);
        tab_sprite_down[2] = spriteSheet.get(70, 0, 21, 47);
        tmpSprites.put(TypeMouvement.DOWN, tab_sprite_down);

        Image[] tab_sprite_left = new Image[3];
        tab_sprite_left[0] = spriteSheet.get(6, 49, 29, 45);
        tab_sprite_left[1] = spriteSheet.get(38, 49, 29, 45);
        tab_sprite_left[2] = spriteSheet.get(70, 49, 29, 45);
        tmpSprites.put(TypeMouvement.LEFT, tab_sprite_left);

        Image[] tab_sprite_right = new Image[3];
        tab_sprite_right[0] = spriteSheet.get(2, 97, 29, 46);
        tab_sprite_right[1] = spriteSheet.get(34, 97, 29, 46);
        tab_sprite_right[2] = spriteSheet.get(65, 97, 29, 46);
        tmpSprites.put(TypeMouvement.RIGHT, tab_sprite_right);

        Image[] tab_sprite_up = new Image[3];
        tab_sprite_up[0] = spriteSheet.get(6, 145, 21, 46);
        tab_sprite_up[1] = spriteSheet.get(39, 145, 21, 46);
        tab_sprite_up[2] = spriteSheet.get(71, 145, 21, 46);
        tmpSprites.put(TypeMouvement.UP, tab_sprite_up);

        sprites = Collections.unmodifiableMap(tmpSprites);
    }

    @Override
    public String toString() {
        return "\u001B[45m" + super.toString() + "\u001B[0m";
    }
}
