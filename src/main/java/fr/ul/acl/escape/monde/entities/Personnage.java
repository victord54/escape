package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.outils.FabriqueId;
import javafx.geometry.Rectangle2D;
import org.json.JSONObject;

import java.util.List;

public abstract class Personnage extends ElementMonde {
    protected final int id;
    protected double vitesse;
    protected double maxCoeurs;
    protected double degats;
    protected double coeurs;
    protected boolean isMoving = false;
    protected TypeMouvement dernierMouvement = TypeMouvement.DOWN;
    protected TypeMouvement orientation;

    public Personnage(Type type, double x, double y, double hauteur, double largeur, double vitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(type, x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.coeurs = coeurs;
        this.maxCoeurs = maxCoeurs;
        this.id = id > 0 ? id : FabriqueId.getInstance().getId();
        this.orientation = TypeMouvement.DOWN;
        this.degats = degats;
    }

    public Personnage(JSONObject json) {
        super(json);
        this.id = json.optInt("id", FabriqueId.getInstance().getId());
        this.vitesse = json.getDouble("speed");
        this.coeurs = json.getDouble("life");
        this.maxCoeurs = json.getDouble("maxLife");
        this.degats = json.getDouble("damages");
        this.orientation = TypeMouvement.DOWN;
    }

    public static Personnage fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.HERO) {
            return new Heros(json);
        } else if (type == Type.WALKER) {
            return new Walker(json);
        } else if (type == Type.GHOST) {
            return new Fantome(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("id", id);
        json.put("life", coeurs);
        json.put("maxLife", maxCoeurs);
        json.put("speed", vitesse);
        json.put("damages", degats);
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
            case RIGHT -> this.x += vitesseTransformee;
            case LEFT -> this.x -= vitesseTransformee;
            case UP -> this.y -= vitesseTransformee;
            case DOWN -> this.y += vitesseTransformee;
        }
        this.dernierMouvement = typeMouvement;
        this.orientation = typeMouvement;
    }

    /**
     * The method inflicts damage on the characters provided in a list.
     *
     * @param touches: List of characters to be damaged
     */
    public void attaquer(List<Personnage> touches) {
        for (Personnage p : touches) {
            p.coeursPerdu(this.degats);
        }
    }

    /**
     * Returns the attack hitbox based on the current orientation of the object.
     * The hitbox is a Rectangle2D representing a point in the direction of the attack.
     *
     * @return Rectangle2D representing the attack hitbox.
     */
    public Rectangle2D getHitBoxAttaque() {
        double hitbox = 0.4;
        switch (this.orientation) {
            case RIGHT -> {
                return new Rectangle2D(x + largeur, y, hitbox, hauteur);
            }
            case LEFT -> {
                return new Rectangle2D(x - largeur, y, hitbox, hauteur);
            }
            case UP -> {
                return new Rectangle2D(x, y - hauteur, largeur, hitbox);
            }
            case DOWN -> {
                return new Rectangle2D(x, y + hauteur, largeur, hitbox);
            }
        }
        return new Rectangle2D(this.x, this.y, hitbox, hitbox);
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

    public double getMaxCoeurs() {
        return maxCoeurs;
    }

    public void setHauteur(double h) {
        this.hauteur = h;
    }

    public void setLargeur(double l) {
        this.largeur = l;
    }

    public TypeMouvement getDernierMouvement() {
        return dernierMouvement;
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
        if (coeurs < 0) {
            coeurs = 0;
        }
    }

    public boolean estVivant() {
        return this.coeurs > 0;
    }

    /**
     * Method that increases the number of hearts.
     *
     * @param c Number of hearts won.
     */
    public void coeursGagne(double c) {
        coeurs += c;
        if (coeurs > maxCoeurs) {
            coeurs = maxCoeurs;
        }
    }

    public TypeMouvement getOrientation() {
        return orientation;
    }

    public void setOrientation(TypeMouvement o) {
        this.orientation = o;
    }

    /**
     * @return a copy of the Personnage
     */
    public abstract Personnage clone();

    /**
     * @return True if the Personnage can cross an obstacle, false otherwise.
     */
    public boolean peutTraverserObstacles() {
        return false;
    }

    /**
     * Copies the statistics from another character to this character.
     * <p>
     * This method copies the damage, maximum hearts, current hearts, and speed
     * from the specified character to the current character.
     * </p>
     *
     * @param personnage The character whose statistics are to be copied.
     * @see Personnage
     */
    public void copierStatistique(Personnage personnage) {
        degats = personnage.degats;
        maxCoeurs = personnage.maxCoeurs;
        coeurs = personnage.coeurs;
        vitesse = personnage.vitesse;
    }
}
