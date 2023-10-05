package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.TypeMouvement;

public abstract class Personnage extends ElementMonde {
    protected float vitesse;

    public Personnage(float x, float y, int hauteur, int largeur, float vitesse) {
        super(x, y, hauteur, largeur);
        vitesse = 1;
        this.vitesse = vitesse;
    }

    public abstract void deplacer(TypeMouvement typeMouvement) throws MouvementNullException;

    public boolean estUnHeros() {
        return false;
    }

}
