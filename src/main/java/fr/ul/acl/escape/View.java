package fr.ul.acl.escape;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class View {
    private final Parent root;

    private final ViewController controller;

    private final ViewEvents viewEvents;

    public View(FXMLLoader loader) throws IOException {
        this(loader, null);
    }

    public View(FXMLLoader loader, ViewEvents viewEvents) throws IOException {
        this(loader.load(), loader.getController(), viewEvents);
    }

    public View(Parent root, ViewController controller, ViewEvents viewEvents) {
        this.root = root;
        this.controller = controller;
        this.viewEvents = viewEvents;

        if (viewEvents != null) {
            root.setOnKeyPressed(event -> viewEvents.onKeyPressed(controller, event));
        }
    }

    public Parent getRoot() {
        return root;
    }

    public ViewController getController() {
        return controller;
    }

    public void onViewInit() {
        if (viewEvents != null) {
            viewEvents.onViewInit(controller);
        }
    }

    public void onViewDisplayed() {
        if (viewEvents != null) {
            viewEvents.onViewDisplayed(controller);
        }
    }
}

interface ViewEvents {
    void onViewInit(ViewController controller);
    void onViewDisplayed(ViewController controller);
    void onKeyPressed(ViewController controller, KeyEvent event);
}
