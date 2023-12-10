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

    private final String carteOuTeleporter;
    private final int difficulteProchaineNiveau;

    public Trappe(double x, double y, double hauteur, double largeur, boolean visible, String carteOuTeleporter) {
        super(Type.TRAPDOOR, x, y, hauteur, largeur, visible);
        this.carteOuTeleporter = carteOuTeleporter;
        this.ouverte = false;
        this.difficulteProchaineNiveau = 0;
    }

    public Trappe(double x, double y, double hauteur, double largeur, boolean visible, int difficulteProchaineNiveau) {
        super(Type.TRAPDOOR, x, y, hauteur, largeur, visible);
        this.carteOuTeleporter = "";
        this.ouverte = false;
        this.difficulteProchaineNiveau = difficulteProchaineNiveau;
    }


    public Trappe(JSONObject json) {
        super(json);
        this.carteOuTeleporter = json.getString("map");
        this.difficulteProchaineNiveau = json.getInt("nextLevelDifficulty");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("map", this.carteOuTeleporter);
        json.put("nextLevelDifficulty", difficulteProchaineNiveau);

        return json;
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

        sprites[0] = spriteSheet.get(95,0,96,96);
        sprites[1] = spriteSheet.get(0,0,96,96);
    }

    @Override
    public boolean estDeclenchable() {
        return ouverte;
    }

    @Override
    public void consommePar(Personnage p, Monde monde) {
        monde.changerMap(this.carteOuTeleporter, this.difficulteProchaineNiveau);
    }

    /**
     * Sets the status of an object to "open".
     * <p>
     * This method changes the status of an object to "open" by setting the "ouverte" attribute to true.
     * </p>
     */
    public void ouvrir(){
        ouverte = true;
    }

    @Override
    public boolean estTrappe() {
        return true;
    }
}
