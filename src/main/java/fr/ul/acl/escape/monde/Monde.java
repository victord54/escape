package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.outils.GestionFichier;

import java.util.List;

public class Monde {
    private int nbLigne, nbCol;
    private List<ElementMonde> elementMondes;

    public Monde(){

    }
    private final GestionFichier gestionFichier = new GestionFichier();

    /**
     * Function that return if there is a collision between two element of the world.
     * @param e1 The first element.
     * @param e2 The second element.
     * @return true if there is a collision between e1 and e2, false otherwise.
     */
    public boolean collision(ElementMonde e1, ElementMonde e2){
        // e1 gauche de e2 -> pt droit de e1 < pt gauche e2
        // e1 en dessous de e2 -> pt haut de e1 -> > pt bas e2
        // e1 à droite de e2 -> pt gauche de e1 > pt droit e2
        // e1 au dessus de e2 -> pt bas de e < pt haut e2

        if ((e1.getX() < e2.getX() + e2.getLargeur()) && (e1.getY() < e2.getY() + e2.getHauteur()) &&
                (e1.getX() + e1.getLargeur() > e2.getX()) && ((e1.getY() + e1.getLargeur() > e2.getY()))){
            return true;
        }
        return false;
    }
    public void gestionCollisions(){

    }
}
