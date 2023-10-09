package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameView extends View implements GameInterface {
    /**
     * Canvas of the game board. Contains the game elements.
     */
    private Canvas gameBoard;
    /**
     * Canvas of the overlay. Contains some information about the game.
     */
    private Canvas overlay;
    /**
     * Size of a square element on the game board.
     */
    private NumberBinding elementSize;
    /**
     * The game engine.
     */
    private Engine engine;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/game-view.fxml"));
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onViewDisplayed() {
        StackPane centerPane = ((GameViewController) controller).getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        gameBoard = ((GameViewController) controller).getGameBoard();
        overlay = ((GameViewController) controller).getOverlay();

        // binding game board to center pane
        elementSize = Bindings.min(centerPane.widthProperty().divide(Donnees.WORLD_WIDTH), centerPane.heightProperty().divide(Donnees.WORLD_HEIGHT));
        gameBoard.widthProperty().bind(elementSize.multiply(Donnees.WORLD_WIDTH));
        gameBoard.heightProperty().bind(elementSize.multiply(Donnees.WORLD_HEIGHT));

        // binding overlay to center pane
        overlay.widthProperty().bind(centerPane.widthProperty());
        overlay.heightProperty().bind(centerPane.heightProperty());

        // redrawing canvas when resizing
        gameBoard.widthProperty().addListener((observable, oldValue, newValue) -> render());
        gameBoard.heightProperty().addListener((observable, oldValue, newValue) -> render());

        // start engine
        engine = new Engine(this);
        engine.start();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            Settings.showFps = !Settings.showFps;
        }
    }

    /**
     * Draw the game board canvas.
     *
     * @param canvas      The canvas to draw on.
     * @param elementSize The size of a square element on the game board.
     */
    private void drawGameBoard(Canvas canvas, double elementSize) {
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
        gc.drawImage(Resources.getAsset("assets/UL.png"), 0, 0, elementSize, elementSize);
    }

    /**
     * Draw the overlay canvas.
     *
     * @param canvas The canvas to draw on.
     */
    private void drawOverlay(Canvas canvas) {
        if (engine == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // write FPS
        if (Settings.showFps) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillText("FPS: " + engine.getFPS(), 10, canvas.getHeight() - 10);
        }
    }

    @Override
    public void render() {
        this.drawGameBoard(gameBoard, elementSize.doubleValue());
        this.drawOverlay(overlay);
    }
}
