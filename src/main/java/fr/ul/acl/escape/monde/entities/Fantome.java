package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.TypeMouvement;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fantome extends Monstre{
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

    }

    @Override
    public Personnage clone() {
        return new Fantome(x, y, hauteur, largeur, vitesse, coeurs, maxCoeurs, degats, id);
    }

    @Override
    public boolean peutTraverserObstacle(){
        return true;
    }
}
