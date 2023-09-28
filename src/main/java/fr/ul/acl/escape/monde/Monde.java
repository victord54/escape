package fr.ul.acl.escape.monde;

import java.util.ArrayList;

public class Monde {
    private int nbLigne, nbCol;
    private ArrayList<Personnage> personnages;
    private ArrayList<Terrain> terrains;


    public Monde() {
        personnages = new ArrayList<>();
        personnages.add(new Heros(1, 1, 1, 2, 1));
        terrains = new ArrayList<>();
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

        if ((e1.getX() < e2.getX() + e2.getLargeur()) && (e1.getY() < e2.getY() + e2.getHauteur()) && (e1.getX() + e1.getLargeur() > e2.getX()) && ((e1.getY() + e1.getLargeur() > e2.getY()))) {
            return true;
        }

        return false;
    }


    /**
     * Function that check between each element of the world if there is a collision and
     * deal with the consequences if there is one.
     */
    public void gestionCollisions() {
        // On vérifie les collisions pour chaque personnages
        for (int i = 0; i < personnages.size(); i++) {
            // Vérifications avec les terrains
            for (Terrain t : terrains) {
                if (!t.estTraversable()) {
                    if (this.collision(personnages.get(i), t)) {
                        this.redeplacementSiCollision(personnages.get(i), t);
                    }
                }
            }
            // Vérification avec les autres personnages : j = i + 1 pour commencer après le personnage dont on est
            // entrain de vérifier les collisions
            for (int j = i + 1; j < personnages.size(); j++) {
                if (this.collision(personnages.get(i), personnages.get(j))) {
                    this.redeplacementSiCollision(personnages.get(i), personnages.get(j));
                }
            }
        }

    }

    /**
     * Function that places the Personnage correctly (in front of) an Element of the world.
     *
     * @param e1 The Personnage to be placed correctly.
     * @param e2 The world Element in front of which the character must be correctly placed.
     */
    protected void redeplacementSiCollision(Personnage e1, ElementMonde e2) {
        // Calcul des distances afin de savoir d'où vient e1
        float e1LeftAndE2Right = Math.abs(e1.getX() - (e2.getX() + e2.getLargeur()));
        float e1RightAndE2Left = Math.abs((e1.getX() + e1.getLargeur()) - e2.getX());
        float e1BottomAndE2Top = Math.abs((e1.getY() + e1.getHauteur()) - e2.getY());
        float e1TopAndE2Bottom = Math.abs(e1.getY() - (e2.getY() + e2.getHauteur()));


        // Collision à gauche de e1
        if (e1.getX() < e2.getX() + e2.getLargeur() && e1.getX() + e1.getLargeur() > e2.getX() + e2.getLargeur() && ((e1LeftAndE2Right < e1TopAndE2Bottom) && (e1LeftAndE2Right < e1BottomAndE2Top))) {
            e1.setX(e2.getX() + e2.getLargeur());
        }
        // Collision à droite de e1
        if (e1.getX() + e1.getLargeur() > e2.getX() && e1.getX() < e2.getX() && ((e1RightAndE2Left < e1BottomAndE2Top) && (e1RightAndE2Left < e1TopAndE2Bottom))) {
            e1.setX(e2.getX() - e1.getLargeur());
        }
        // Collision en haut de e1
        if (e1.getY() < e2.getY() + e2.getHauteur() && e1.getY() + e1.getHauteur() > e2.getY() + e2.getHauteur() && ((e1TopAndE2Bottom < e1LeftAndE2Right) && (e1TopAndE2Bottom < e1RightAndE2Left))) {
            e1.setY(e2.getY() + e2.getHauteur());
        }
        // Collision en bas de e1
        if (e1.getY() + e1.getHauteur() > e2.getY() && e1.getY() < e2.getY() && ((e1BottomAndE2Top < e1LeftAndE2Right) && (e1BottomAndE2Top < e1RightAndE2Left))) {
            e1.setY(e2.getY() - e1.getHauteur());
        }
    }

    public void updateData() {
        gestionCollisions();
    }

    public Heros getHeros() {
        for (Personnage p : personnages) {
            if (p.estUnHeros()) return (Heros) p;
        }
        return null;
    }
}
