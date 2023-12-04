package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Heros extends Personnage {
    private static Map<TypeMouvement, Image[]> sprites;

    public Heros(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(ElementMonde.Type.HERO, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    public Heros(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[46m[ ]\u001B[0m"; // Cyan background
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }


    @Override
    public Image getSprite(int i) {
        if (sprites == null) return null;
        return sprites.get(orientation)[i];
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }

    @Override
    public Heros clone() {
        return new Heros(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    @Override
    protected void initSprites() {
        if (sprites != null) {
            return;
        }

        Map<TypeMouvement, Image[]> tmpSprites = new HashMap<>();

        String path = "assets/heros.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        Image[] tab_sprite_down = new Image[3];
        tab_sprite_down[0] = spriteSheet.get(6, 5, 29, 31);
        tab_sprite_down[1] = spriteSheet.get(46, 5, 29, 31);
        tab_sprite_down[2] = spriteSheet.get(86, 5, 29, 31);
        tmpSprites.put(TypeMouvement.DOWN, tab_sprite_down);

        Image[] tab_sprite_left = new Image[3];
        tab_sprite_left[0] = spriteSheet.get(6, 44, 29, 31);
        tab_sprite_left[1] = spriteSheet.get(46, 44, 29, 31);
        tab_sprite_left[2] = spriteSheet.get(86, 44, 29, 31);
        tmpSprites.put(TypeMouvement.LEFT, tab_sprite_left);

        Image[] tab_sprite_right = new Image[3];
        tab_sprite_right[0] = spriteSheet.get(6, 84, 29, 31);
        tab_sprite_right[1] = spriteSheet.get(46, 84, 29, 31);
        tab_sprite_right[2] = spriteSheet.get(86, 84, 29, 31);
        tmpSprites.put(TypeMouvement.RIGHT, tab_sprite_right);

        Image[] tab_sprite_up = new Image[3];
        tab_sprite_up[0] = spriteSheet.get(6, 124, 29, 31);
        tab_sprite_up[1] = spriteSheet.get(46, 124, 29, 31);
        tab_sprite_up[2] = spriteSheet.get(86, 124, 29, 31);
        tmpSprites.put(TypeMouvement.UP, tab_sprite_up);

        sprites = Collections.unmodifiableMap(tmpSprites);
    }

    @Override
    public String toString() {
        return "\u001B[46m" + super.toString() + "\u001B[0m";
    }
}
