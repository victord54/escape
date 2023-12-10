package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Eau extends Terrain {
    private static Image sprite;

    public Eau(double x, double y, double hauteur, double largeur) {
        super(Type.WATER, x, y, hauteur, largeur);
    }

    public Eau(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "\u001B[44m[~]\u001B[0m";
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }

    @Override
    public Image getSprite(int i) {
        return sprite;
    }

    @Override
    protected void initSprites() {
        if (sprite != null) {
            return;
        }

        String path = "assets/decors.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        sprite = spriteSheet.get(0, 50, 50, 50);
    }

    /**
     * Method which returns true if the Terrain is special.
     *
     * @return true
     */
    @Override
    public boolean estTerrainSpecial() {
        return true;
    }

    /**
     * Method that apply the special action to a Personnage.
     *
     * @param p The Personnage to apply the special action.
     */
    @Override
    public void appliqueActionSpeciale(Personnage p) {
        if (p.estUnHeros() && !p.canSwim()) {
            p.coeursPerdu(p.getCoeurs());
        }
        p.diminutionVitesse();
    }
}
