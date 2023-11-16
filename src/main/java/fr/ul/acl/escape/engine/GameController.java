package fr.ul.acl.escape.engine;

import fr.ul.acl.escape.monde.Monde;

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
}
