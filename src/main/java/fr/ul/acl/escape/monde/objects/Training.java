package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.gui.SpriteSheet;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.json.JSONObject;

public class Training extends Objet {
    private static Image[] sprites;

    private boolean heroWasOn = false;
    private long lastTime = 0;

    public Training(double x, double y, double hauteur, double largeur) {
        super(Type.TRAINING, x, y, hauteur, largeur, true);
    }

    public Training(JSONObject json) {
        super(json);
    }

    @Override
    public String getSymbol() {
        return "⛵";
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    public Image getSprite(int index) {
        return sprites[(index / 2) % sprites.length];
    }

    @Override
    protected void initSprites() {
        if (sprites != null) {
            return;
        }

        String path = "assets/duck.png";
        SpriteSheet spriteSheet = new SpriteSheet(path);
        if (spriteSheet.get() == null) return;

        Image[] tab_sprite = new Image[8];
        tab_sprite[0] = spriteSheet.get(32, 0, 30, 30);
        tab_sprite[1] = spriteSheet.get(32, 30, 30, 30);
        tab_sprite[2] = spriteSheet.get(32, 0, 30, 30);
        tab_sprite[3] = spriteSheet.get(32, 30, 30, 30);
        tab_sprite[4] = spriteSheet.get(64, 0, 30, 30);
        tab_sprite[5] = spriteSheet.get(64, 30, 30, 30);
        tab_sprite[6] = spriteSheet.get(64, 0, 30, 30);
        tab_sprite[7] = spriteSheet.get(64, 30, 30, 30);

        sprites = tab_sprite;
    }

    @Override
    public boolean necessiteDureePourActivation() {
        return true;
    }

    @Override
    public void notOnObject(Personnage p) {
        if (!p.estUnHeros()) return;
        heroWasOn = false;
    }

    @Override
    public void onObject(Personnage p, long now) {
        if (!p.estUnHeros()) return;

        if (!heroWasOn) {
            lastTime = now;
            heroWasOn = true;
            return;
        }

        long timeElapsed = now - lastTime;
        lastTime = now;

        Heros h = (Heros) p;
        h.addTrainingTime(timeElapsed);
    }
}
