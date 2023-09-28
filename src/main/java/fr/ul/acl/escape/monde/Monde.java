package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.outils.GestionFichier;

import java.util.List;

public class Monde {
    private int nbLigne, nbCol;
    private List<ElementMonde> elementMondes;

    public Monde(){

    }
    private final GestionFichier gestionFichier = new GestionFichier();

    public boolean collision(ElementMonde e1, ElementMonde e2){
        return true;
    }

    public void gestionCollisions(){

    }
}
