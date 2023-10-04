package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public abstract class Personnage extends ElementMonde{

    enum TypeMouvement{
        RIGHT,
        LEFT,
        FORWARD,
        BACK
    }

    protected float vitesse;

    public Personnage(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
        vitesse = 1;
    }

    public abstract void deplacer(TypeMouvement typeMouvement) throws MouvementNullException;

    public boolean estUnHeros(){
        return false;
    }


}
