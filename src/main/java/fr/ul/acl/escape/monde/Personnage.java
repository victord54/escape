package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.FabriqueId;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;
    private int id;


    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();

    }

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.id = id;
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

    @Override
    public String toString() {
        return super.toString() + "id :" + this.id;
    }

}
