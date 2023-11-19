package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.gui.Sprite;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.outils.FabriqueId;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public abstract class Personnage extends ElementMonde {
    private final int id;
    protected double vitesse;
    protected double coeurs;

    protected boolean isMoving = false;

    protected HashMap<TypeMouvement, Sprite[]> sprites;

    protected TypeMouvement dernierMouvement = TypeMouvement.DOWN;
    protected TypeMouvement orientation;

    public Personnage(Type type, double x, double y, double hauteur, double largeur, double vitesse) {
        super(type, x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();
        orientation = TypeMouvement.DOWN;
        sprites = new HashMap<>();
    }

    public Personnage(Type type, double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(type, x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.id = id;
        orientation = TypeMouvement.DOWN;
        sprites = new HashMap<>();
    }

    public Personnage(JSONObject json) {
        super(json);
        this.id = json.optInt("id", FabriqueId.getInstance().getId());
        this.vitesse = json.optDouble("speed");
    }

    public static Personnage fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.HERO) {
            return new Heros(json);
        } else if (type == Type.WALKER) {
            return new Walker(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("id", id);
        json.put("life", coeurs);
        json.put("speed", vitesse);
        return json;
    }

    /**
     * Method who moves a Personnage
     *
     * @param typeMouvement : the movement type (RIGHT, LEFT, ...)
     * @param deltaTime     : the time difference since the last iteration
     */
    public void deplacer(TypeMouvement typeMouvement, double deltaTime) {
        double vitesseTransformee = vitesse * (deltaTime >= 0 ? deltaTime : 0);
        switch (typeMouvement) {
            case RIGHT -> {
                this.x += vitesseTransformee;
                dernierMouvement = TypeMouvement.RIGHT;
            }
            case LEFT -> {
                this.x -= vitesseTransformee;
                dernierMouvement = TypeMouvement.LEFT;
            }
            case UP -> {
                this.y -= vitesseTransformee;
                dernierMouvement = TypeMouvement.UP;
            }
            case DOWN -> {
                this.y += vitesseTransformee;
                dernierMouvement = TypeMouvement.DOWN;
            }
        }
        this.orientation = typeMouvement;
    }

    /**
     * The method inflicts damage on the characters provided in a list.
     *
     * @param touches: List of characters to be damaged
     */
    public void attaquer(List<Personnage> touches) {
        for (Personnage p : touches) {
            p.coeursPerdu(1);
        }
    }

    /**
     * Returns the attack hitbox based on the current orientation of the object.
     * The hitbox is a Rectangle2D representing a point in the direction of the attack.
     *
     * @return Rectangle2D representing the attack hitbox.
     */
    public Rectangle2D getHitBoxAttaque() {
        switch (this.orientation) {
            case RIGHT -> {
                return new Rectangle2D(x + largeur, y, 1, 1);
            }
            case LEFT -> {
                return new Rectangle2D(x - largeur, y, 1, 1);
            }
            case UP -> {
                return new Rectangle2D(x, y - hauteur, 1, 1);
            }
            case DOWN -> {
                return new Rectangle2D(x, y + hauteur, 1, 1);
            }
        }
        return new Rectangle2D(this.x, this.y, 1, 1);
    }

    /**
     * Returns the collision hitbox for the object.
     *
     * @return Rectangle2D representing the collision hitbox.
     */
    public Rectangle2D getHitBoxCollision() {
        return new Rectangle2D(x, y, largeur, hauteur);
    }

    public boolean estUnHeros() {
        return false;
    }

    public int getId() {
        return id;
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getCoeurs() {
        return coeurs;
    }

    public void setCoeurs(double c) {
        coeurs = c;
    }

    public void setOrientation(TypeMouvement o) {
        this.orientation = o;
    }

    public TypeMouvement getDernierMouvement() {
        return dernierMouvement;
    }

    public Image getSprite(int i) {
        return sprites.get(dernierMouvement)[i].getSprite();
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    /**
     * Method that reduce the number of hearts.
     *
     * @param c Number of lost hearts.
     */
    public void coeursPerdu(double c) {
        coeurs -= c;
    }

    public boolean estVivant() {
        return this.coeurs > 0;
    }

    @Override
    public String toString() {
        return super.toString() + "id :" + this.id;
    }

    public TypeMouvement getOrientation() {
        return orientation;
    }

}
