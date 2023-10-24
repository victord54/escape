package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
    }

    /**
     * Method who moves a Character
     * @param typeMouvement : the movement type (RIGHT, LEFT, ...)
     * @param deltaTime : the time difference since the last iteration
     * @throws MouvementNullException : if movement type is null
     */
    public abstract void deplacer(TypeMouvement typeMouvement, double deltaTime) throws MouvementNullException;

    public boolean estUnHeros() {
        return false;
    }

}
