package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Monstre extends Personnage {

    /**
     * List of movements tried.
     */
    protected ArrayList<TypeMouvement> derniersMouvementsEssayes = new ArrayList<>();

    public Monstre(ElementMonde.Type type, double x, double y, double hauteur, double largeur, double vitesse, double maxVitesse, double coeurs, double maxCoeurs, double degats, int id) {
        super(type, x, y, hauteur, largeur, vitesse, maxVitesse, coeurs, maxCoeurs, degats, id);
    }

    public Monstre(JSONObject json) {
        super(json);
    }

    /**
     * Method that add a movement to the list of movements tried.
     *
     * @param mvt the movement to add
     */
    public void addMouvementEssayes(TypeMouvement mvt) {
        if (!derniersMouvementsEssayes.contains(mvt)) derniersMouvementsEssayes.add(mvt);
    }

    /**
     * Method that checks if a movement has already been tried.
     *
     * @param mvt the movement to check
     * @return true if the movement has already been tried, false otherwise
     */
    public boolean mouvementDansList(TypeMouvement mvt) {
        return derniersMouvementsEssayes.contains(mvt);
    }

    /**
     * Method that reset the list of movements tried.
     */
    public void reinitialiseListMouvementsEssayes() {
        derniersMouvementsEssayes = new ArrayList<>();
    }
}
