package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.Property;
import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.engine.GameInterface;
import javafx.animation.AnimationTimer;

public class GUIEngine extends fr.ul.acl.escape.engine.Engine {
    /**
     * If true, the game is paused.
     */
    public final Property<Boolean> paused = new Property<>(false);
    /**
     * Timer used to create the game loop.
     */
    private final AnimationTimer timer;

    /**
     * The time of the last update in nanoseconds.
     */
    private long lastUpdate = 0;
    /**
     * The elapsed time since the last update in nanoseconds.
     */
    private long deltaTime = 0;

    /**
     * The time of the previous second in nanoseconds.
     */
    private long previousSecond = 0;
    /**
     * The number of frames in the current second.
     */
    private int nbFrames = 0;
    /**
     * The number of frames in the previous second.
     */
    private int fps = 0;

    public GUIEngine(GameInterface ui, GameController controller) {
        super(ui, controller);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tick(now);
            }
        };
    }

    @Override
    protected void tick(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        // count fps every second
        if (now - previousSecond >= 1e9f) {
            previousSecond = now;
            fps = nbFrames;
            nbFrames = 0;
        }
        nbFrames++;

        // update and render
        this.controller.update(deltaTime * (paused.get() ? 0 : 1));
        this.ui.render();

        // update elapsed time
        deltaTime = now - lastUpdate;
        lastUpdate = now;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        timer.start();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Returns the number of frames that should be rendered in the last second.
     */
    public int getComputedFPS() {
        return (int) (1e9f / deltaTime);
    }

    /**
     * Returns the number of frames rendered in the last second.
     */
    public int getFPS() {
        return fps;
    }
}
