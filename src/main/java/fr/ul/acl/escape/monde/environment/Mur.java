package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Mur extends Terrain {
    public Mur(double x, double y, double hauteur, double largeur) {
        super(ElementMonde.Type.WALL, x, y, hauteur, largeur);
        sprite = new Sprite("assets/decors.png", 0, 0, 50, 50);
    }

    public Mur(JSONObject json) {
        super(json);
        sprite = new Sprite("assets/decors.png", 0, 0, 50, 50);
    }

    @Override
    public char getSymbol() {
        return '█';
    }

    @Override
    public Color getColor() {
        return Color.BROWN;
    }

    public boolean estTraversable() {
        return false;
    }
}
