package fr.ul.acl.escape.ui;

import javafx.scene.input.KeyEvent;

/**
 * The events of a view.
 */
public interface ViewEvents {
    /**
     * Fired when the view is initialized, before it is displayed.
     *
     * @param controller The controller of the view.
     */
    void onViewInit(ViewController controller);

    /**
     * Fired when the view is displayed.
     *
     * @param controller The controller of the view.
     */
    void onViewDisplayed(ViewController controller);

    /**
     * Fired when a key is pressed.
     *
     * @param controller The controller of the view.
     * @param event      The event of the key pressed.
     */
    void onKeyPressed(ViewController controller, KeyEvent event);
}
