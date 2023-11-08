package fr.ul.acl.escape.monde;

public class Coeur extends Objet{
    protected double valeur;
    public Coeur(double x, double y, double hauteur, double largeur, double valeur){
        super(x,y,hauteur,largeur);
        this.valeur = valeur;
    }

    public double getValeur(){
        return valeur;
    }

    public boolean estCoeur(){
        return true;
    }
}
