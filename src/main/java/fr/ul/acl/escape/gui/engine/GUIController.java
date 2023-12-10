package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.GameMode;
import fr.ul.acl.escape.KeyAction;
import fr.ul.acl.escape.KeyBindings;
import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.outils.ErrorBehavior;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class GUIController extends fr.ul.acl.escape.engine.GameController {
    /**
     * The keys currently pressed.
     */
    private final HashSet<KeyCode> keysPressed = new HashSet<>();
    /**
     * If true, it means the game is paused.
     */
    protected boolean onPause = false;
    /**
     * If true, it means the game is over.
     */
    protected boolean onOver = false;
    /**
     * If true, the key R is pressed.
     */
    private boolean takeKeyPressed = false;

    private boolean attackKeyPressed = false;

    /**
     * Create a new controller with a new world from a default map.
     */
    public GUIController() {
        try {
            monde = Monde.fromMap("map01" + JSON.extension, GameMode.CAMPAIGN);
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Failed to load map");
        }
    }

    /**
     * Create a new controller from a JSON object.
     *
     * @param json The JSON object.
     *             See {@link Monde#toJSONSave()} for the format.
     */
    public GUIController(JSONObject json) {
        try {
            monde = Monde.fromJSON(json);
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Failed to load map from JSON");
        }
    }

    /**
     * Create a new controller from a world.
     *
     * @param monde The world.
     */
    public GUIController(Monde monde) {
        this.monde = monde;
    }

    @Override
    public void update(long deltaTime, long now) {
        if (onPause || onOver) return;
        double timeInDouble = deltaTime * 10e-10;

        KeyBindings keyBindings = Settings.keyBindings.get();

        // Déplacements
        if (keysPressed.contains(keyBindings.getKey(KeyAction.UP))) {
            monde.getHeros().setMoving(true);
            MovementManager.instance.addMouvement(TypeMouvement.UP);
        }
        if (keysPressed.contains(keyBindings.getKey(KeyAction.DOWN))) {
            monde.getHeros().setMoving(true);
            MovementManager.instance.addMouvement(TypeMouvement.DOWN);
        }
        if (keysPressed.contains(keyBindings.getKey(KeyAction.RIGHT))) {
            monde.getHeros().setMoving(true);
            MovementManager.instance.addMouvement(TypeMouvement.RIGHT);
        }
        if (keysPressed.contains(keyBindings.getKey(KeyAction.LEFT))) {
            monde.getHeros().setMoving(true);
            MovementManager.instance.addMouvement(TypeMouvement.LEFT);
        }
        MovementManager.instance.executerMouvement(monde, timeInDouble);

        // Ramasser object
        if (keysPressed.contains(keyBindings.getKey(KeyAction.TAKE)) && !takeKeyPressed) {
            monde.heroRamassageObjet();
            takeKeyPressed = true;
        } else if (!keysPressed.contains(keyBindings.getKey(KeyAction.TAKE))) {
            takeKeyPressed = false;
        }

        // Attaquer
        if (keysPressed.contains(keyBindings.getKey(KeyAction.ATTACK)) && !attackKeyPressed) {
            attackKeyPressed = true;
            monde.heroAttaque(now);
        } else if (!keysPressed.contains(keyBindings.getKey(KeyAction.ATTACK))) {
            attackKeyPressed = false;
        }

        // State of the game
        if (!monde.heroStillAlive()) {
            onOver = true;
        }


        monde.deplacementMonstres(timeInDouble);
        monde.monstreAttaque(now);
        monde.activationObjetAvecDuree(now);
    }

    public boolean collisionAvecTerrains(Personnage p) {
        return monde.collisionAvecTerrains(p);
    }

    public void onKeyPressed(KeyEvent event) {
        keysPressed.add(event.getCode());
    }

    public void onKeyReleased(KeyEvent event) {
        monde.getHeros().setMoving(false);
        keysPressed.remove(event.getCode());
    }

    public void setOnPause(boolean b) {
        onPause = b;
    }

    public boolean getOnOver() {
        return onOver;
    }

    /**
     * The MovementManager class handles movements of an object within a world.
     * It allows adding movement types and executing these movements in the world
     * with a specified delta time.
     * <p>
     * It allows to manage diagonal movements
     */
    protected static class MovementManager {

        protected static final double PIBY4 = Math.PI / 4;
        protected static final MovementManager instance = new MovementManager();
        Set<TypeMouvement> mouvements;

        private MovementManager() {
            mouvements = new HashSet<>();
        }

        /**
         * Adds a movement type to the list of movements to be performed.
         *
         * @param mov Type of movement to add.
         */
        public void addMouvement(TypeMouvement mov) {
            this.mouvements.add(mov);
        }

        /**
         * Executes the movements in the world with the specified delta time.
         * If two movements are present, the delta time is adjusted for diagonal movements.
         *
         * @param monde     Instance of the world in which to perform the movements.
         * @param deltaTime Delta time for executing the movements.
         */
        public void executerMouvement(Monde monde, double deltaTime) {
            double deltaTimeTraite = deltaTime;
            if (mouvements.size() == 2) deltaTimeTraite *= PIBY4;

            for (TypeMouvement mouv : mouvements) {
                monde.deplacementHeros(mouv, deltaTimeTraite);
            }
            mouvements = new HashSet<>();
        }

    }
}
