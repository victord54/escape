package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.outils.ErrorBehavior;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GUIController extends fr.ul.acl.escape.engine.GameController {
    /**
     * The keys currently pressed.
     */
    private final Set<KeyCode> keysPressed = new HashSet<>();

    /**
     * Create a new controller with a new world from a default map.
     */
    public GUIController() {
        try {
            monde = Monde.fromMap("carte01");
        } catch (Exception e) {
            ErrorBehavior.crash(e, "Failed to load map");
        }
    }

    /**
     * Create a new controller from a JSON object.
     *
     * @param json The JSON object.
     *             See {@link Monde#toJSON()} for the format.
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


        monde.deplacementMonstres(timeInDouble);
    }


    public ArrayList<Terrain> getTerrains() {
        return monde.getTerrains();
    }

    public ArrayList<Personnage> getPersonnages() {
        return monde.getPersonnages();
    }

    public void onKeyPressed(KeyEvent event) {
        keysPressed.add(event.getCode());
    }

    public void onKeyReleased(KeyEvent event) {
        keysPressed.remove(event.getCode());
    }

    public JSONObject getJSON() {
        return monde.toJSON();
    }
}
