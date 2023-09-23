package fr.ul.acl.escape;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GameController extends ViewController {
    @FXML
    private GridPane grid;

    @FXML
    private StackPane centerPane;

    public GridPane getGrid() {
        return grid;
    }

    public StackPane getCenterPane() {
        return centerPane;
    }
}
