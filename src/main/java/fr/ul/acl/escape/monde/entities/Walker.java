package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.TypeMouvement;
import org.json.JSONObject;

import static fr.ul.acl.escape.outils.Donnees.WALKER_HEART;
import static fr.ul.acl.escape.outils.Donnees.WALKER_SPEED;

public class Walker extends Monstre {

    public Walker(double x, double y, double hauteur, double largeur) {
        super(Type.WALKER, x, y, hauteur, largeur, WALKER_SPEED);
        coeurs = WALKER_HEART;
        setSprites();
    }

    public Walker(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(Type.WALKER, x, y, hauteur, largeur, vitesse, id);
        coeurs = WALKER_HEART;
        //setSprites();
    }

    private void setSprites() {
        String path = "assets/squelettes.png";
        Sprite[] tab_sprite_down = new Sprite[3];
        tab_sprite_down[0] = new Sprite(path, 6, 0, 21, 47);
        tab_sprite_down[1] = new Sprite(path, 38, 0, 21, 47);
        tab_sprite_down[2] = new Sprite(path, 70, 0, 21, 47);
        sprites.put(TypeMouvement.DOWN, tab_sprite_down);

        Sprite[] tab_sprite_left = new Sprite[3];
        tab_sprite_left[0] = new Sprite(path, 6, 49, 29, 45);
        tab_sprite_left[1] = new Sprite(path, 38, 49, 29, 45);
        tab_sprite_left[2] = new Sprite(path, 70, 49, 29, 45);
        sprites.put(TypeMouvement.LEFT, tab_sprite_left);

        Sprite[] tab_sprite_right = new Sprite[3];
        tab_sprite_right[0] = new Sprite(path, 2, 97, 29, 46);
        tab_sprite_right[1] = new Sprite(path, 34, 97, 29, 46);
        tab_sprite_right[2] = new Sprite(path, 65, 97, 29, 46);
        sprites.put(TypeMouvement.RIGHT, tab_sprite_right);

        Sprite[] tab_sprite_up = new Sprite[3];
        tab_sprite_up[0] = new Sprite(path, 6, 145, 21, 46);
        tab_sprite_up[1] = new Sprite(path, 39, 145, 21, 46);
        tab_sprite_up[2] = new Sprite(path, 71, 145, 21, 46);
        sprites.put(TypeMouvement.UP, tab_sprite_up);
    }

    public Walker(JSONObject json) {
        super(json);
    }
}
