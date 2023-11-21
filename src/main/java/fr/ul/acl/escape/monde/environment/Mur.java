package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import org.json.JSONObject;
import javafx.scene.image.Image;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.WALL, x, y, hauteur, largeur);
        sprite = new Sprite("assets/decors.png", 0, 0, 50, 50);
    }

    public Mur(JSONObject json) {
        super(json);
    }

    @Override
    public char getSymbol() {
        return '█';
    }

    public boolean estTraversable() {
        return false;
    }
}
