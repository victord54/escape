package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.monde.*;
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

    /**
     * If true, the key R is pressed.
     */
    boolean rKeyPressed = false;

    public GUIController() {
        super(new Monde());

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
            monde.heroCollisionAvecObjet();
            rKeyPressed = true;
        } else if (!keysPressed.contains(KeyCode.R)) {
            rKeyPressed = false;
        }

        //Attaquer
        if (keysPressed.contains(KeyCode.ENTER)) {
            monde.heroAttaque();
        }


        monde.deplacementMonstres(timeInDouble);
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
