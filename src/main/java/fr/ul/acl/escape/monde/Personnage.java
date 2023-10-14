package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        vitesse = 1;
        this.vitesse = vitesse;
    }

    public abstract void deplacer(TypeMouvement typeMouvement) throws MouvementNullException;

    public boolean estUnHeros() {
        return false;
    }

}
