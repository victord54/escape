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
    private final Set<KeyCode> keysPressed = new HashSet<>();

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
            monde.deplacementHeros(TypeMouvement.UP, timeInDouble);
        }
        if (keysPressed.contains(KeyCode.S)) {
            monde.deplacementHeros(TypeMouvement.DOWN, timeInDouble);
        }
        if (keysPressed.contains(KeyCode.D)) {
            monde.deplacementHeros(TypeMouvement.RIGHT, timeInDouble);
        }
        if (keysPressed.contains(KeyCode.Q)) {
            monde.deplacementHeros(TypeMouvement.LEFT, timeInDouble);
        }

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
        keysPressed.remove(event.getCode());
    }
}
