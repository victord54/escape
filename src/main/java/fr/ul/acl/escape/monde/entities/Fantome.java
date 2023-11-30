package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.TypeMouvement;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Fantome extends Monstre {
    private static Map<TypeMouvement, Image[]> sprites;

    public Fantome(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(Type.GHOST, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    public Fantome(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[47m[ ]\u001B[0m";
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Image getSprite(int i) {
        if (sprites == null) return null;
        return sprites.get(dernierMouvement)[i];
    }

    @Override
    protected void initSprites() {
        if (sprites != null) {
            return;
        }
        Map<TypeMouvement, Image[]> tmpSprites = new HashMap<>();

        String path = "assets/fantomes.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        Image[] tab_sprite_down = new Image[3];
        tab_sprite_down[0] = spriteSheet.get(5, 3, 37, 44);
        tab_sprite_down[1] = spriteSheet.get(53, 2, 38, 44);
        tab_sprite_down[2] = spriteSheet.get(103, 1, 29, 44);
        tmpSprites.put(TypeMouvement.DOWN, tab_sprite_down);

        Image[] tab_sprite_left = new Image[3];
        tab_sprite_left[0] = spriteSheet.get(8, 53, 36, 42);
        tab_sprite_left[1] = spriteSheet.get(58, 50, 30, 41);
        tab_sprite_left[2] = spriteSheet.get(105, 49, 29, 40);
        tmpSprites.put(TypeMouvement.LEFT, tab_sprite_left);

        Image[] tab_sprite_right = new Image[3];
        tab_sprite_right[0] = spriteSheet.get(3, 100, 35, 41);
        tab_sprite_right[1] = spriteSheet.get(54, 98, 30, 41);
        tab_sprite_right[2] = spriteSheet.get(104, 97, 29, 41);
        tmpSprites.put(TypeMouvement.RIGHT, tab_sprite_right);

        Image[] tab_sprite_up = new Image[3];
        tab_sprite_up[0] = spriteSheet.get(4, 145, 37, 44);
        tab_sprite_up[1] = spriteSheet.get(52, 144, 38, 44);
        tab_sprite_up[2] = spriteSheet.get(103, 143, 31, 44);
        tmpSprites.put(TypeMouvement.UP, tab_sprite_up);

        sprites = Collections.unmodifiableMap(tmpSprites);

    }

    @Override
    public Personnage clone() {
        return new Fantome(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    @Override
    public boolean peutTraverserObstacles() {
        return true;
    }
}
