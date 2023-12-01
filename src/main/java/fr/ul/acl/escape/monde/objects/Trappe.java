package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Trappe extends Objet{

    private static Image[] sprites;

    private boolean ouverte;

    String carteOuTeleporter;

    public Trappe(double x, double y, double hauteur, double largeur, boolean visible, String carteOuTeleporter) {
        super(Type.TRAPDOOR, x, y, hauteur, largeur, visible);
        this.carteOuTeleporter = carteOuTeleporter;
        this.ouverte = false;
    }

    public Trappe(JSONObject json) {
        super(json);
        this.carteOuTeleporter = json.getString("map");
    }

    @Override
    public String getSymbol() {
        return "T";
    }

    @Override
    public Color getColor() {
        return Color.LIGHTCYAN;
    }

    @Override
    public Image getSprite(int i) {
        if(ouverte) return sprites[1];
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

        sprites[0] = spriteSheet.get(23,0,24,24);
        sprites[1] = spriteSheet.get(0,0,24,24);
    }

    @Override
    public boolean estDeclenchable() {
        return ouverte;
    }

    @Override
    public void consommePar(Personnage p, Monde monde) {
        //TODO : indiquer au monde de changer de niveau
    }

    public void ouvrir(){
        ouverte = true;
    }

    @Override
    public boolean estTrappe() {
        return true;
    }
}
