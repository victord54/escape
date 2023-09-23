package fr.ul.acl.escape;

import javafx.beans.binding.Bindings;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

public class GameView implements ViewEvents {
    private final int[][] cases;

    public GameView() {
        cases = new int[10][14];
    }

    @Override
    public void onDisplayed(ViewController controller) {
        StackPane centerPane = ((GameController) controller).getCenterPane();
        GridPane grid = ((GameController) controller).getGrid();
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        grid.getChildren().clear();
        for (int i = 0; i < cases.length; i++) {
            for (int j = 0; j < cases[i].length; j++) {
                Pane pane = new Pane();
                pane.setBackground(new Background(new BackgroundFill(j % 2 + i % 2 == 1 ? javafx.scene.paint.Color.BLACK : javafx.scene.paint.Color.WHITE, null, null)));
                grid.add(pane, i, j);
                pane.prefWidthProperty().bind(Bindings.min(centerPane.widthProperty().divide(cases.length), centerPane.heightProperty().divide(cases[0].length)));
                pane.prefHeightProperty().bind(Bindings.min(centerPane.widthProperty().divide(cases.length), centerPane.heightProperty().divide(cases[0].length)));
            }
        }
    }

    @Override
    public void onKeyPressed(ViewController controller, KeyEvent event) {

    }
}
