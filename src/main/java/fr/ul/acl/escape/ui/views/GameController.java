package fr.ul.acl.escape.ui.views;

import fr.ul.acl.escape.ui.ViewController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class GameController extends ViewController {
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
