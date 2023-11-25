package fr.ul.acl.escape.engine;

import fr.ul.acl.escape.monde.Monde;
import org.json.JSONObject;

public abstract class GameController {
    /**
     * The game world.
     */
    protected Monde monde;

    /**
     * Updates the game state.
     *
     * @param deltaTime The elapsed time since the last update in nanoseconds.
     */
    public abstract void update(long deltaTime);

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
}
