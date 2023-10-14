package fr.ul.acl.escape.engine;

import fr.ul.acl.escape.monde.Monde;

public abstract class GameController {
    /**
     * The game world.
     */
    protected final Monde monde;

    protected GameController(Monde monde) {
        this.monde = monde;
    }

    /**
     * Updates the game state.
     *
     * @param elapsed The elapsed time since the last update in nanoseconds.
     */
    public abstract void update(long elapsed);
}
