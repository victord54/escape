package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.Personnage;
import fr.ul.acl.escape.monde.Terrain;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GUIController extends fr.ul.acl.escape.engine.GameController {
    /**
     * The keys currently pressed.
     */
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private boolean t = false;
    private int i;

    public GUIController() {
        super(new Monde());
        i = 5;
        try {
            monde.chargerCarte("carte01");
        } catch (Exception e) {
            System.err.println("Error while loading map");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void update(long timeElapsed) {
        double timeInDouble = timeElapsed * 10e-10;

        try {
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
        } catch (MouvementNullException ignored) {
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

}
