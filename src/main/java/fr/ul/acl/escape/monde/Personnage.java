package fr.ul.acl.escape.monde;

public abstract class Personnage extends ElementMonde{

    protected float vitesse;

    public abstract void deplacer(String typeMouvement);
}
