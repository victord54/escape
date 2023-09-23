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
        this.root = loader.load();
        this.controller = loader.getController();
        this.viewEvents = viewEvents;

        root.setOnKeyPressed(event -> viewEvents.onKeyPressed(controller, event));
    }

    public Parent getRoot() {
        return root;
    }

    public ViewController getController() {
        return controller;
    }

    public void onDisplayed() {
        if (viewEvents != null) {
            viewEvents.onDisplayed(controller);
        }
    }
}

interface ViewEvents {
    void onDisplayed(ViewController controller);
    void onKeyPressed(ViewController controller, KeyEvent event);
}
