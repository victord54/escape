package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.monde.objects.Objet;
import fr.ul.acl.escape.outils.ErrorBehavior;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class GUIController extends fr.ul.acl.escape.engine.GameController {
    /**
     * The keys currently pressed.
     */
    private final HashSet<KeyCode> keysPressed = new HashSet<>();

    /**
     * If true, the key R is pressed.
     */
    boolean rKeyPressed = false;

    /**
     * Create a new controller with a new world from a default map.
     */
    public GUIController() {
        try {
            monde = Monde.fromMap("map01" + JSON.extension);
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

    @Override
    public void update(long deltaTime) {
        double timeInDouble = deltaTime * 10e-10;

        //Déplacements
        if (keysPressed.contains(KeyCode.Z)) {
            MovementManager.instance.addMouvement(TypeMouvement.UP);
        }
        if (keysPressed.contains(KeyCode.S)) {
            MovementManager.instance.addMouvement(TypeMouvement.DOWN);
        }
        if (keysPressed.contains(KeyCode.D)) {
            MovementManager.instance.addMouvement(TypeMouvement.RIGHT);
        }
        if (keysPressed.contains(KeyCode.Q)) {
            MovementManager.instance.addMouvement(TypeMouvement.LEFT);
        }
        MovementManager.instance.executerMouvement(monde, timeInDouble);

        // Ramasser object
        if (keysPressed.contains(KeyCode.R) && !rKeyPressed) {
            monde.heroRamassageObjet();
            rKeyPressed = true;
        } else if (!keysPressed.contains(KeyCode.R)) {
            rKeyPressed = false;
        }

        //Attaquer
        if (keysPressed.contains(KeyCode.ENTER)) {
            monde.heroAttaque();
        }

        monde.deplacementMonstres(timeInDouble);
        monde.monstreAttaque();
    }


    public ArrayList<Terrain> getTerrains() {
        return monde.getTerrains();
    }

    public ArrayList<Personnage> getPersonnages() {
        return monde.getPersonnages();
    }

    public ArrayList<Objet> getObjets() {
        return monde.getObjets();
    }

    public Heros getHeros() {
        return monde.getHeros();
    }

    public void onKeyPressed(KeyEvent event) {
        keysPressed.add(event.getCode());
    }

    public void onKeyReleased(KeyEvent event) {
        monde.getHeros().setMoving(false);
        keysPressed.remove(event.getCode());
    }


    /**
     * The MovementManager class handles movements of an object within a world.
     * It allows adding movement types and executing these movements in the world
     * with a specified delta time.
     *
     * It allows to manage diagonal movements
     */
    protected static class MovementManager {

        protected static final double PIBY4 = 0.70710678118; //PI/4 pour les mouvements diagonaux
        Set<TypeMouvement> mouvements;

        protected static final MovementManager instance = new MovementManager();

        private MovementManager(){
            mouvements = new HashSet<>();
        }

        /**
         * Adds a movement type to the list of movements to be performed.
         *
         * @param mov Type of movement to add.
         */
        public void addMouvement(TypeMouvement mov){
            this.mouvements.add(mov);
        }

        /**
         * Executes the movements in the world with the specified delta time.
         * If two movements are present, the delta time is adjusted for diagonal movements.
         *
         * @param monde     Instance of the world in which to perform the movements.
         * @param deltaTime Delta time for executing the movements.
         */
        public void executerMouvement(Monde monde, double deltaTime){
            double deltaTimeTraite = deltaTime;
            if(mouvements.size() == 2) deltaTimeTraite *= PIBY4;

            for(TypeMouvement mouv : mouvements){
                monde.deplacementHeros(mouv, deltaTimeTraite);
            }
            mouvements = new HashSet<>();
        }

    }
}
