package fr.ul.acl.escape;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class GameController extends ViewController {
    @FXML
    public Canvas canvas;

    public Canvas getCanvas() {
        return canvas;
    }

    @FXML
    public StackPane pane;

    public StackPane getPane() {
        return pane;
    }
}
