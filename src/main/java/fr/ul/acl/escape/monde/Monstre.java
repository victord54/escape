package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.Donnees;

public abstract class Monstre extends Personnage {
    public Monstre(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur, vitesse);
    }

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur, vitesse, id);
    }

    /**
     * Method who moves a Monstre
     *
     * @param typeMouvement the movement type (RIGHT, LEFT, ...)
     */
    public void deplacer(TypeMouvement typeMouvement) {
        int tmpX = (int) (this.x * Donnees.CONVERSION_FACTOR);
        int tmpY = (int) (this.y * Donnees.CONVERSION_FACTOR);
        int v = (int) (this.vitesse * Donnees.CONVERSION_FACTOR);

        switch (typeMouvement) {
            case RIGHT -> tmpX += v;
            case LEFT -> tmpX -= v;
            case UP -> tmpY -= v;
            case DOWN -> tmpY += v;
        }

        this.x = (double) tmpX / Donnees.CONVERSION_FACTOR;
        this.y = (double) tmpY / Donnees.CONVERSION_FACTOR;

    }

    public void deplacer(TypeMouvement typeMouvement, double deltaTime) throws MouvementNullException {
        if (typeMouvement == null) throw new MouvementNullException();

        double vitesseTransformee = vitesse * (deltaTime >= 0 ? deltaTime : 0);
        switch (typeMouvement) {
            case RIGHT -> this.x += vitesseTransformee;
            case LEFT -> this.x -= vitesseTransformee;
            case UP -> this.y -= vitesseTransformee;
            case DOWN -> this.y += vitesseTransformee;
        }
    }
}
