package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.monde.objects.Objet;
import fr.ul.acl.escape.outils.ErrorBehavior;

import java.util.ArrayList;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class CLIController extends GameController {
    /**
     * The action number to perform.
     */
    private int action;

    protected CLIController() {
        try {
            monde = Monde.fromMap("map01" + JSON.extension);
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

    public ArrayList<Objet> getObjets() {
        return monde.getObjets();
    }

    public void setAction(int action) {
        this.action = action;
    }
}
