package fr.ul.acl.escape.monde;

import java.util.ArrayList;

public abstract class Monstre extends Personnage {

    protected ArrayList<TypeMouvement> derniersMouvementsEssayes;
    protected TypeMouvement dernierMouvementReussi = TypeMouvement.DOWN;

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur, vitesse);
        derniersMouvementsEssayes = new ArrayList<>();
    }

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur, vitesse, id);
    }

    /**
     * Method that add a movement to the list of movements tried.
     *
     * @param mvt the movement to add
     */
    protected void addMouvementEssayes(TypeMouvement mvt) {
        if (!derniersMouvementsEssayes.contains(mvt)) derniersMouvementsEssayes.add(mvt);
    }

    /**
     * Method that checks if a movement has already been tried.
     *
     * @param mvt the movement to check
     * @return true if the movement has already been tried, false otherwise
     */
    protected boolean mouvementDansList(TypeMouvement mvt) {
        return derniersMouvementsEssayes.contains(mvt);
    }

    /**
     * Method that reinitialises the list of movements tried.
     */
    protected void reinitialiseListMouvementsEssayes() {
        derniersMouvementsEssayes = new ArrayList<>();
    }

    protected TypeMouvement getDernierMouvementReussi() {
        return dernierMouvementReussi;
    }

    protected void setLastMouvement(TypeMouvement mvt) {
        dernierMouvementReussi = mvt;
    }
}
