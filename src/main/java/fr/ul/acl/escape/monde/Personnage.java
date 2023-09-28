package fr.ul.acl.escape.monde;

public abstract class Personnage extends ElementMonde{

    protected float vitesse;

    public Personnage(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
    }

    public abstract void deplacer(String typeMouvement);
}
