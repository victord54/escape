package fr.ul.acl.escape.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * A view of the application, as a Node and its controller.
 */
public class View {
    /**
     * The root node of the view.
     */
    private final Parent root;

    /**
     * The controller of the view.
     */
    private final ViewController controller;

    /**
     * The events of the view.
     */
    private final ViewEvents viewEvents;

    /**
     * Creates a view from a {@link FXMLLoader}.
     *
     * @param loader The loader of the view.
     * @throws IOException If the view cannot be loaded.
     */
    public View(FXMLLoader loader) throws IOException {
        this(loader, null);
    }

    /**
     * Creates a view from a {@link FXMLLoader}.
     *
     * @param loader     The loader of the view.
     * @param viewEvents The events of the view.
     * @throws IOException If the view cannot be loaded.
     */
    public View(FXMLLoader loader, ViewEvents viewEvents) throws IOException {
        this(loader.load(), loader.getController(), viewEvents);
    }

    /**
     * Creates a view from a root node and its controller.
     *
     * @param root       The root node of the view.
     * @param controller The controller of the view.
     */
    public View(Parent root, ViewController controller, ViewEvents viewEvents) {
        this.root = root;
        this.controller = controller;
        this.viewEvents = viewEvents;

        if (viewEvents != null) {
            // Register the key pressed event
            root.setOnKeyPressed(event -> viewEvents.onKeyPressed(controller, event));
        }
    }

    /**
     * Get the root node of the view.
     *
     * @return The root node of the view.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Should be called when the view is initialized, before it is displayed.
     */
    public void onViewInit() {
        if (viewEvents != null) {
            viewEvents.onViewInit(controller);
        }
    }

    /**
     * Should be called when the view is displayed.
     */
    public void onViewDisplayed() {
        if (viewEvents != null) {
            viewEvents.onViewDisplayed(controller);
        }
    }
}

