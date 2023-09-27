package fr.ul.acl.escape.gui;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

/**
 * A view of the application, as a Node and its controller.
 */
public abstract class View {
    /**
     * The root node of the view.
     */
    protected Parent root;

    /**
     * The controller of the view.
     */
    protected ViewController controller;

    /**
     * Get the root node of the view.
     *
     * @return The root node of the view.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Enable the root node events to be handled by the view.
     */
    public void enableRootEvents() {
        root.setOnKeyPressed(this::onKeyPressed);
        root.setOnKeyReleased(this::onKeyReleased);
    }

    /**
     * Fired when the view is initialized, before it is displayed.
     */
    public void onViewInit() {
    }

    /**
     * Fired when the view is displayed.
     */
    public void onViewDisplayed() {
    }

    /**
     * Fired when a key is pressed.
     *
     * @param event      The event of the key pressed.
     */
    public void onKeyPressed(KeyEvent event) {
    }

    /**
     * Fired when a key is released.
     *
     * @param event      The event of the key released.
     */
    public void onKeyReleased(KeyEvent event) {
    }
}
