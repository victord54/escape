package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.outils.FabriqueId;
import javafx.geometry.Rectangle2D;

import java.util.List;

import static fr.ul.acl.escape.monde.TypeMouvement.UP;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;
    private final int id;
    protected double coeurs;

    public TypeMouvement orientation;

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();
        orientation = UP;
    }

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.id = id;
        orientation = UP;
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

}
