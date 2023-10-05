package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.engine.Engine;
import fr.ul.acl.escape.gui.engine.GameInterface;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.Resources;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameView extends View implements GameInterface {
    private Canvas canvas;
    private NumberBinding elementSize;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/game-view.fxml"));
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onViewDisplayed() {
        StackPane centerPane = ((GameViewController) controller).getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        canvas = ((GameViewController) controller).getCanvas();

        // redrawing canvas when resizing
        elementSize = Bindings.min(centerPane.widthProperty().divide(Donnees.WORLD_WIDTH), centerPane.heightProperty().divide(Donnees.WORLD_HEIGHT));
        canvas.widthProperty().bind(elementSize.multiply(Donnees.WORLD_WIDTH));
        canvas.heightProperty().bind(elementSize.multiply(Donnees.WORLD_HEIGHT));
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> draw(canvas, elementSize.doubleValue()));
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> draw(canvas, elementSize.doubleValue()));

        // first draw
        this.draw(canvas, elementSize.doubleValue());

        // start engine
        Engine engine = new Engine(this);
        engine.start();
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
        for (int i = 0; i < Donnees.WORLD_WIDTH; i++) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT; j++) {
                gc.setFill(i % 2 + j % 2 == 1 ? Color.LIGHTGRAY : Color.GRAY);
                gc.fillRect(i * elementSize, j * elementSize, elementSize, elementSize);
            }
        }

        // test draw image
        // TODO: cache images
        Image img = new Image(Resources.get("assets/UL.png").toString());
        gc.drawImage(img, 0, 0, elementSize, elementSize);
    }

    @Override
    public void render() {
        this.draw(canvas, elementSize.doubleValue());
    }
}
