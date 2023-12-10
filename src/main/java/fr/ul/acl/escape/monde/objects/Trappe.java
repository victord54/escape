package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Trappe extends Objet {

    private static Image[] sprites;
    private final String carteOuTeleporter;
    private boolean ouverte;

    public Trappe(double x, double y, double hauteur, double largeur, String carteOuTeleporter, boolean ouverte) {
        super(Type.TRAPDOOR, x, y, hauteur, largeur, true);
        this.carteOuTeleporter = carteOuTeleporter;
        this.ouverte = ouverte;
    }

    public Trappe(double x, double y, double hauteur, double largeur, boolean visible) {
        super(Type.TRAPDOOR, x, y, hauteur, largeur, visible);
        this.carteOuTeleporter = "";
        this.ouverte = false;
    }


    public Trappe(JSONObject json) {
        super(json);
        this.ouverte = json.getBoolean("open");
        this.carteOuTeleporter = json.getString("map");
        this.ouverte = json.getBoolean("isOpen");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("open", this.ouverte);
        json.put("map", this.carteOuTeleporter);
        json.put("isOpen", this.ouverte);

        return json;
    }

    @Override
    public String getSymbol() {
        return ouverte ? "○" : "◍";
    }

    @Override
    public Color getColor() {
        return Color.LIGHTGREEN;
    }

    @Override
    public Image getSprite(int i) {
        if (ouverte) return sprites[1];
        return sprites[0];
    }

    @Override
    protected void initSprites() {
        if (sprites != null) {
            return;
        }

        sprites = new Image[2];

        String path = "assets/trap-door.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);

        if (spriteSheet.get() == null) return;

        sprites[0] = spriteSheet.get(95,0,96,96);
        sprites[1] = spriteSheet.get(0,0,96,96);
    }

    @Override
    public boolean estDeclenchable() {
        return ouverte;
    }

    @Override
    public void consommePar(Personnage p, Monde monde) {
        monde.changerMap(this.carteOuTeleporter);
    }

    /**
     * Sets the status of an object to "open".
     * <p>
     * This method changes the status of an object to "open" by setting the "ouverte" attribute to true.
     * </p>
     */
    public void ouvrir() {
        ouverte = true;
    }

    @Override
    public boolean estTrappe() {
        return true;
    }
}
