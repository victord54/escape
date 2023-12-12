package fr.ul.acl.escape.engine;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.monde.objects.Objet;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GameController {
    /**
     * The game world.
     */
    protected Monde monde;

    /**
     * Updates the game state.
     *
     * @param deltaTime The elapsed time since the last update in nanoseconds.
     * @param now       The current time in nanoseconds.
     */
    public abstract void update(long deltaTime, long now);

    /**
     * @return A JSON object representing the game state.
     */
    public JSONObject getJSONWorld() {
        return monde.toJSONSave();
    }

    /**
     * @return the height of the world
     */
    public int getHeight() {
        return monde.getHeight();
    }

    /**
     * @return the width of the world
     */
    public int getWidth() {
        return monde.getWidth();
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

    public Monde getMonde(){
        return monde;
    }
}
