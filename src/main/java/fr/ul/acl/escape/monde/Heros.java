package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

import static fr.ul.acl.escape.outils.Donnees.HERO_SPEED;

public class Heros extends Personnage {

    public Heros(double x, double y, double hauteur, double largeur) {
        super(x, y, hauteur, largeur, HERO_SPEED);
    }

    /**
     * Method who moves a Heros
     *
     * @param typeMouvement : the movement type (RIGHT, LEFT, ...)
     * @param deltaTime     : the time difference since the last iteration
     * @throws MouvementNullException : if movement type is null
     */
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

    @Override
    public boolean estUnHeros() {
        return true;
    }
}
