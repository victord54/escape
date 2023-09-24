package fr.ul.acl.escape.ui.views;

import fr.ul.acl.escape.ui.ViewController;
import fr.ul.acl.escape.ui.ViewEvents;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameViewEvents implements ViewEvents {
    private final int[][] cases;

    public GameViewEvents() {
        cases = new int[12][7];
    }

    @Override
    public void onViewInit(ViewController controller) {
    }

    @Override
    public void onViewDisplayed(ViewController controller) {
        StackPane centerPane = ((GameController) controller).getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Canvas canvas = ((GameController) controller).getCanvas();

        // redrawing canvas when resizing
        NumberBinding elementSize = Bindings.min(
                centerPane.widthProperty().divide(cases.length),
                centerPane.heightProperty().divide(cases[0].length));
        canvas.widthProperty().bind(elementSize.multiply(cases.length));
        canvas.heightProperty().bind(elementSize.multiply(cases[0].length));
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> draw(canvas, newValue.doubleValue() / cases.length));
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> draw(canvas, newValue.doubleValue() / cases[0].length));

        // first draw
        draw(canvas, elementSize.getValue());
    }

    @Override
    public void onKeyPressed(ViewController controller, KeyEvent event) {
    }

    /**
     * Draw the grid on the canvas.
     *
     * @param canvas      The canvas to draw on.
     * @param elementSize The size of a square element of the grid.
     */
    private void draw(Canvas canvas, Number elementSize) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw grid
        double size = elementSize.doubleValue();
        for (int i = 0; i < cases.length; i++) {
            for (int j = 0; j < cases[i].length; j++) {
                gc.setFill(i % 2 + j % 2 == 1 ? Color.LIGHTGRAY : Color.GRAY);
                gc.fillRect(i * size, j * size, size, size);
            }
        }
    }
}
