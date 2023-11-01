package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.monde.*;
import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

import java.util.ArrayList;

public class CLIController extends GameController {
    /**
     * The action number to perform.
     */
    private int action;

    protected CLIController() {
        super(new Monde());

        try {
            monde.chargerCarte("carte01");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la carte");
            System.exit(1);
        }
    }

    @Override
    public void update(long elapsed) {
        switch (action) {
            case 1 -> {
                try {
                    monde.deplacementHeros(TypeMouvement.LEFT, 1);
                } catch (MouvementNullException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                try {
                    monde.deplacementHeros(TypeMouvement.RIGHT, 1);
                } catch (MouvementNullException e) {
                    throw new RuntimeException(e);
                }
            }
            case 3 -> {
                try {
                    monde.deplacementHeros(TypeMouvement.UP, 1);
                } catch (MouvementNullException e) {
                    throw new RuntimeException(e);
                }
            }
            case 4 -> {
                try {
                    monde.deplacementHeros(TypeMouvement.DOWN, 1);
                } catch (MouvementNullException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Heros getHeros() {
        return monde.getHeros();
    }

    public ArrayList<Personnage> getPersonnages() {
        return monde.getPersonnages();
    }

    public ArrayList<Terrain> getTerrains() {
        return monde.getTerrains();
    }

    public void setAction(int action) {
        this.action = action;
    }
}
