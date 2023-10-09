package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.GestionFichier;

import java.util.ArrayList;

public class Monde {
    private final ArrayList<Personnage> personnages;
    private final ArrayList<Terrain> terrains;

    public Monde() {
        personnages = new ArrayList<>();
        terrains = new ArrayList<>();
    }

    /**
     * Load map's informations in the world
     * Charge les information de la carte dans le monde
     * @param carte nom de la carte à charger
     */
    public void chargerCarte(String carte) throws Exception {
        // Variable de vérification
        boolean heroExiste = false;

        this.personnages = new ArrayList<>();
        this.terrains = new ArrayList<>();
        // On récupère les informations de la carte
        char[][] donnees = GestionFichier.lireFichierCarte(carte);

        // On parcours les données
        for (int j= 0; j < Donnees.hauteurMonde(); j++) {
            for (int i = 0; i < Donnees.longeurMonde(); i++) {
                if(donnees[j][i] != '0'){
                    // Elements du terrain comme les murs et les trous
                    if(donnees[j][i] == 'M'){
                        this.terrains.add(new Mur(i*Donnees.largeurMur(),j*Donnees.hauteurMur(),Donnees.hauteurMur(),Donnees.largeurMur()));
                    }

                    // Personnages pose sur la carte hero et monstre
                    if(donnees[j][i] == 'H' && !heroExiste){
                        this.personnages.add(new Heros(i*Donnees.largeurMur(),j*Donnees.hauteurMur(),Donnees.hauteurHero(),Donnees.largeurHero()) );
                        heroExiste = true;
                    }
                    if(donnees[j][i] == 'W'){
                        this.personnages.add(new Walker(i*Donnees.largeurMur(),j*Donnees.hauteurMur(),Donnees.hauteurWalker(),Donnees.largeurWalker()));
                    }
                }
            }
        }
        if(!heroExiste) throw new Exception("Where hero ?");
    }



    /**
     * Function that return if there is a collision between two element of the world.
     *
     * @param e1 The first element.
     * @param e2 The second element.
     * @return true if there is a collision between e1 and e2, false otherwise.
     */
    protected boolean collision(ElementMonde e1, ElementMonde e2) {
        // e1 gauche de e2 -> pt droit de e1 < pt gauche e2
        // e1 en dessous de e2 -> pt haut de e1 -> > pt bas e2
        // e1 à droite de e2 -> pt gauche de e1 > pt droit e2
        // e1 au dessus de e2 -> pt bas de e < pt haut e2

        return ((e1.getX() < e2.getX() + e2.getLargeur()) && (e1.getY() < e2.getY() + e2.getHauteur()) && (e1.getX() + e1.getLargeur() > e2.getX()) && ((e1.getY() + e1.getLargeur() > e2.getY())));
    }

    /**
     * Function that add a Terrain to the ArrayList of Terrain.
     *
     * @param t The Terrain to be added.
     */
    public void addTerrains(Terrain t) {
        this.terrains.add(t);
    }

    /**
     * Function that add a Personnage to the ArrayList of Personnage.
     *
     * @param p The Personnage to be added.
     */
    public void addPersonnage(Personnage p) {
        this.personnages.add(p);
    }


    /**
     * Function that check if the Heros can be deplaced and deplaced it in the right direction if there is no collision.
     *
     * @param typeMouvement The Type of mouvement the Heros wants to make.
     * @throws MouvementNullException Exception throw if typeMouvement is null.
     */
    public void deplacementHeros(TypeMouvement typeMouvement) throws MouvementNullException {
        if (typeMouvement == null) throw new MouvementNullException();

        Heros h = getHeros();
        Heros tmp = new Heros(h.getX(), h.getY(), h.getHauteur(), h.getLargeur(), 1);
        tmp.deplacer(typeMouvement);

        boolean collision = false;
        for (Terrain t : terrains) {
            if (collision(tmp, t)) {
                collision = true;
                break;
            }
        }
        if (!collision) {
            for (Personnage p : personnages) {
                if (!p.estUnHeros()) {
                    if (collision(tmp, p)) {
                        collision = true;
                        break;
                    }
                }
            }
        }

        if (!collision) {
            this.getHeros().deplacer(typeMouvement);
        }
    }

    /**
     * Function that get the Heros.
     *
     * @return the Heros.
     */
    public Heros getHeros() {
        for (Personnage p : personnages) {
            if (p.estUnHeros()) return (Heros) p;
        }
        return null;
    }

    public void updateData() {
    }
}
