package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.ViewController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class GameViewController extends ViewController {
    @FXML
    private StackPane pane;
    @FXML
    private Canvas gameBoard;
    @FXML
    private Canvas overlay;

    public StackPane getPane() {
        return pane;
    }

    public Canvas getGameBoard() {
        return gameBoard;
    }

    public Canvas getOverlay() {
        return overlay;
    }
}
