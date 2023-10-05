package fr.ul.acl.escape.gui.engine;

import javafx.animation.AnimationTimer;

public class Engine extends AnimationTimer {
    /**
     * The user interface.
     */
    private final GameInterface ui;
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

    // TODO: add game

    public Engine(GameInterface ui) {
        this.ui = ui;
    }

    @Override
    public void handle(long now) {
        if (now - previousSecond >= 1e9f) {
            previousSecond = now;
            fps = nbFrames;
            nbFrames = 0;
        }
        nbFrames++;
        //this.game.update();
        this.ui.render();
    }

    public int getFPS() {
        return fps;
    }
}
