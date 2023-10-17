package fr.ul.acl.escape.engine;

public abstract class Engine {
    /**
     * The user interface.
     */
    protected final GameInterface ui;
    /**
     * The game controller.
     */
    protected final GameController controller;

    protected Engine(GameInterface ui, GameController controller) {
        this.ui = ui;
        this.controller = controller;
    }

    /**
     * Starts the game.
     */
    public abstract void start();

    /**
     * Stops the game.
     */
    public abstract void stop();

    /**
     * Updates the game state.
     *
     * @param now The current time in nanoseconds.
     */
    protected abstract void tick(long now);
}
