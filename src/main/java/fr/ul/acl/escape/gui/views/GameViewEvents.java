package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Escape;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewEvents;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
        StackPane centerPane = ((GameViewController) controller).getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Canvas canvas = ((GameViewController) controller).getCanvas();

        // redrawing canvas when resizing
        NumberBinding elementSize = Bindings.min(
                centerPane.widthProperty().divide(cases.length),
                centerPane.heightProperty().divide(cases[0].length));
        canvas.widthProperty().bind(elementSize.multiply(cases.length));
        canvas.heightProperty().bind(elementSize.multiply(cases[0].length));
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> draw(canvas, elementSize.doubleValue()));
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> draw(canvas, elementSize.doubleValue()));

        // first draw
        this.draw(canvas, elementSize.doubleValue());
    }

    @Override
    public void onKeyPressed(ViewController controller, KeyEvent event) {
    }

    @Override
    public void onKeyReleased(ViewController controller, KeyEvent event) {
    }

    /**
     * Draw the grid on the canvas.
     *
     * @param canvas      The canvas to draw on.
     * @param elementSize The size of a square element of the grid.
     */
    private void draw(Canvas canvas, double elementSize) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw grid
        for (int i = 0; i < cases.length; i++) {
            for (int j = 0; j < cases[i].length; j++) {
                gc.setFill(i % 2 + j % 2 == 1 ? Color.LIGHTGRAY : Color.GRAY);
                gc.fillRect(i * elementSize, j * elementSize, elementSize, elementSize);
            }
        }

        // test draw image
        // TODO: cache images
        Image img = new Image(Escape.getResource("assets/UL.png").toString());
        gc.drawImage(img, 0, 0, elementSize, elementSize);
    }
}
