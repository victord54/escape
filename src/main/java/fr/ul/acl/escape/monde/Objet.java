package fr.ul.acl.escape.monde;

public abstract class Objet extends ElementMonde{

    public Objet(double x, double y, double hauteur, double largeur) {
        super(x,y,hauteur,largeur);
    }

    public boolean estCoeur(){
        return false;
    }
}
