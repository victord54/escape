package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.monde.*;
import fr.ul.acl.escape.outils.ErrorBehavior;

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
            ErrorBehavior.crash(e, "Failed to load map");
        }
    }

    @Override
    public void update(long deltaTime) {
        switch (action) {
            case 1 -> monde.deplacementHeros(TypeMouvement.LEFT, 1);
            case 2 -> monde.deplacementHeros(TypeMouvement.RIGHT, 1);
            case 3 -> monde.deplacementHeros(TypeMouvement.UP, 1);
            case 4 -> monde.deplacementHeros(TypeMouvement.DOWN, 1);
        }

        monde.deplacementMonstres(1);
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
