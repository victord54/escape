package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.gui.Sprite;
import javafx.scene.image.Image;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur);
        sprite = new Sprite("assets/decors.png", 0, 0, 50, 50);
    }

    @Override
    public Image getSprite() {
        return sprite.getSprite();
    }

    public boolean estTraversable() {
        return false;
    }
}
