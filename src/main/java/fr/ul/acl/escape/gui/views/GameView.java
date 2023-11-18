package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.engine.GUIController;
import fr.ul.acl.escape.gui.engine.GUIEngine;
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
    private GUIEngine engine;
    /**
     * The game controller.
     */
    private GUIController gameController;

    /**
     * If true, the grid is drawn on the game board.
     * Dev purpose only.
     */
    private boolean drawGrid = false;

    /**
     * If true, the overlay is drawn on the overlay canvas.
     */
    private boolean drawOverlay = false;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/game-view.fxml"));
        this.root = loader.load();
        this.controller = loader.getController();

        Settings.showFps.subscribe((evt, oldValue, newValue) -> {
            drawOverlay = newValue;
            if (!drawOverlay) clearCanvas(overlay);
        });
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

        // init game controller
        gameController = new GUIController();

        // start engine
        engine = new GUIEngine(this, gameController);
        engine.start();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            engine.stop();
            ViewManager.getInstance().navigateTo(VIEWS.HOME);
        } else if (event.getCode() == KeyCode.SPACE) {
            Settings.showFps.set(!Settings.showFps.get());
        } else if (event.getCode() == KeyCode.G) {
            drawGrid = !drawGrid;
        } else {
            gameController.onKeyPressed(event);
        }
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        gameController.onKeyReleased(event);
    }

    @Override
    public void render() {
        this.drawGameBoard(gameBoard, elementSize.doubleValue());
        this.drawOverlay(overlay);
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
        if (drawGrid) {
            for (int i = 0; i < Donnees.WORLD_WIDTH; i++) {
                for (int j = 0; j < Donnees.WORLD_HEIGHT; j++) {
                    gc.setFill(i % 2 + j % 2 == 1 ? Color.LIGHTGRAY : Color.GRAY);
                    gc.fillRect(i * elementSize, j * elementSize, elementSize, elementSize);
                }
            }
        }

        // draw game environment
        this.gameController.getTerrains().forEach(terrain -> {
            gc.setFill(Color.FIREBRICK);
            gc.fillRect(terrain.getX() * elementSize, terrain.getY() * elementSize, terrain.getLargeur() * elementSize, terrain.getHauteur() * elementSize);
        });

        // draw game entities
        this.gameController.getPersonnages().forEach(personnage -> {
            if (personnage.estUnHeros()) {
                gc.drawImage(Resources.getAsset("assets/UL.png"), personnage.getX() * elementSize, personnage.getY() * elementSize, personnage.getLargeur() * elementSize, personnage.getHauteur() * elementSize);
            } else {
                gc.setFill(Color.BLUEVIOLET);
                gc.fillRect(personnage.getX() * elementSize, personnage.getY() * elementSize, personnage.getLargeur() * elementSize, personnage.getHauteur() * elementSize);
            }
        });
    }

    /**
     * Draw the overlay canvas.
     *
     * @param canvas The canvas to draw on.
     */
    private void drawOverlay(Canvas canvas) {
        if (engine == null || !drawOverlay) return;

        // clear canvas
        clearCanvas(canvas);

        // write FPS
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGREEN);
        gc.fillText("FPS: " + engine.getFPS(), 10, canvas.getHeight() - 10);
    }

    /**
     * Clear the canvas.
     *
     * @param canvas The canvas to clear.
     */
    private void clearCanvas(Canvas canvas) {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
