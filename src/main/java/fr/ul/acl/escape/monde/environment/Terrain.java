package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import javafx.scene.image.Image;

public abstract class Terrain extends ElementMonde {
    protected Sprite sprite;

    public Terrain(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x, y, hauteur, largeur);
    }

    abstract public Image getSprite();

    public boolean estTraversable() {
        return true;
    }
}
