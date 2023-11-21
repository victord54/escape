package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import org.json.JSONObject;

public class Heros extends Personnage {
    public Heros(double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, int id) {
        super(ElementMonde.Type.HERO, x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, id);
        String path = "assets/heros.png";
        Sprite[] tab_sprite_down = new Sprite[3];
        tab_sprite_down[0] = new Sprite(path, 6, 5, 29, 31);
        tab_sprite_down[1] = new Sprite(path, 46, 5, 29, 31);
        tab_sprite_down[2] = new Sprite(path, 86, 5, 29, 31);
        sprites.put(TypeMouvement.DOWN, tab_sprite_down);

        Sprite[] tab_sprite_left = new Sprite[3];
        tab_sprite_left[0] = new Sprite(path, 6, 44, 29, 31);
        tab_sprite_left[1] = new Sprite(path, 46, 44, 29, 31);
        tab_sprite_left[2] = new Sprite(path, 86, 44, 29, 31);
        sprites.put(TypeMouvement.LEFT, tab_sprite_left);

        Sprite[] tab_sprite_right = new Sprite[3];
        tab_sprite_right[0] = new Sprite(path, 6, 84, 29, 31);
        tab_sprite_right[1] = new Sprite(path, 46, 84, 29, 31);
        tab_sprite_right[2] = new Sprite(path, 86, 84, 29, 31);
        sprites.put(TypeMouvement.RIGHT, tab_sprite_right);

        Sprite[] tab_sprite_up = new Sprite[3];
        tab_sprite_up[0] = new Sprite(path, 6, 124, 29, 31);
        tab_sprite_up[1] = new Sprite(path, 46, 124, 29, 31);
        tab_sprite_up[2] = new Sprite(path, 86, 124, 29, 31);
        sprites.put(TypeMouvement.UP, tab_sprite_up);
    }

    public Heros(JSONObject json) {
        super(json);
    }

    @Override
    public char getSymbol() {
        return 'H';
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }

    @Override
    public Heros clone() {
        return new Heros(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, id);
    }
}
