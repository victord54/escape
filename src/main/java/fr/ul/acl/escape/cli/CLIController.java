package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.GameMode;
import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.outils.ErrorBehavior;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class CLIController extends GameController {
    /**
     * The action number to perform.
     */
    private int action;

    protected CLIController() {
        try {
            monde = Monde.fromMap("map01" + JSON.extension, GameMode.CAMPAIGN);
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Failed to load map");
        }
    }

    @Override
    public void update(long deltaTime) {
        double dt = 1 / getHeros().getVitesse();

        switch (action) {
            case 1 -> monde.deplacementHeros(TypeMouvement.LEFT, dt);
            case 2 -> monde.deplacementHeros(TypeMouvement.RIGHT, dt);
            case 3 -> monde.deplacementHeros(TypeMouvement.UP, dt);
            case 4 -> monde.deplacementHeros(TypeMouvement.DOWN, dt);
            case 5 -> monde.heroAttaque();
            case 6 -> monde.heroRamassageObjet();
        }

        monde.deplacementMonstres(dt);
        monde.monstreAttaque();
    }

    public void setAction(int action) {
        this.action = action;
    }
}
