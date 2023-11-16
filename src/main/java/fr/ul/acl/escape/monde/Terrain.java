package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.gui.Sprite;
import javafx.scene.image.Image;

public abstract class Terrain extends ElementMonde {
    protected Sprite sprite;
    public Terrain(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur);
    }

    abstract public Image getSprite();
    public boolean estTraversable() {
        return true;
    }
}
