package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.ViewController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class GameViewController extends ViewController {
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane pane;

    public Canvas getCanvas() {
        return canvas;
    }

    public StackPane getPane() {
        return pane;
    }
}
