package fr.ul.acl.escape.monde;

import java.util.ArrayList;

public class Monde {
    private int nbLigne, nbCol;
    private ArrayList<Personnage> personnages;
    private ArrayList<Terrain> terrains;

    public Monde(){

    }


    public boolean collision(ElementMonde e1, ElementMonde e2){
        return true;
    }

    public void gestionCollisions(){



    }
}
