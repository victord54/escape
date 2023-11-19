package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.SaveData;
import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.engine.GUIController;
import fr.ul.acl.escape.gui.engine.GUIEngine;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.IOException;

import static fr.ul.acl.escape.outils.FileManager.FileType.ENCRYPTED;
import static java.io.File.separator;

public class GameView extends View implements GameInterface, GameViewController.ButtonsListener {
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
     * If true, the value of the FPS is drawn on the overlay canvas.
     */
    private boolean drawFPS = false;

    private int iteration_heros = 0;

    /**
     * Previous save data if the game is loaded from a save.
     */
    private SaveData save;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/game-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();

        Settings.showFps.subscribe((evt, oldValue, newValue) -> drawFPS = newValue);
    }

    @Override
    public void onViewDisplayed(Object... args) {
        super.onViewDisplayed();
        GameViewController controller = (GameViewController) this.controller;

        // init game controller
        if (args.length > 0 && args[0] instanceof SaveData) {
            save = (SaveData) args[0];
            gameController = new GUIController(save.getJSON());
        } else {
            save = null;
            gameController = new GUIController();
        }

        // init game board
        controller.setPauseMenuVisible(false, save != null);
        controller.setButtonsListener(this);

        StackPane centerPane = controller.getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        gameBoard = controller.getGameBoard();
        overlay = controller.getOverlay();

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
        this.engine = new GUIEngine(this, gameController);
        engine.paused.subscribe((evt, oldValue, newValue) -> {
            controller.setPauseMenuVisible(newValue, save != null);
        });
        engine.start();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            engine.paused.set(!engine.paused.get());
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
            gc.drawImage(terrain.getSprite(), terrain.getX() * elementSize, terrain.getY() * elementSize, terrain.getLargeur() * elementSize, terrain.getHauteur() * elementSize);
        });

        // draw game entities
        this.gameController.getPersonnages().forEach(personnage -> {
            if (personnage.estUnHeros()) {
                if (personnage.isMoving()) iteration_heros = (int) (engine.getLastUpdate() / 100000000) % 3;
                gc.drawImage(personnage.getSprite(iteration_heros), personnage.getX() * elementSize, personnage.getY() * elementSize, personnage.getLargeur() * elementSize, personnage.getHauteur() * elementSize);
            }
            int iteration = 0;
            if (personnage.isMoving()) {
                iteration = (int) (engine.getLastUpdate() / 100000000) % 3;
            }
            gc.drawImage(personnage.getSprite(iteration), personnage.getX() * elementSize, personnage.getY() * elementSize, personnage.getLargeur() * elementSize, personnage.getHauteur() * elementSize);

        });
    }

    /**
     * Draw the overlay canvas.
     *
     * @param canvas The canvas to draw on.
     */
    private void drawOverlay(Canvas canvas) {
        if (engine == null) return;

        // clear canvas
        clearCanvas(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // pause menu
        if (engine.paused.get()) {
            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        // write FPS
        if (drawFPS) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillText("FPS: " + engine.getFPS(), 10, canvas.getHeight() - 10);
        }

        // number of hearts the hero currently has
        double coeurs = this.gameController.getHeros().getCoeurs();
        // number max of hearts the hero can have
        double coeursMax = Donnees.HERO_HEART;
        // number of lost hearts
        double coeursPerdu = coeursMax - coeurs;
        // number of full hearts remaining
        int nbCoeursRestantPleins = (int) (coeurs);
        // quartile of heats remaining (0.75,0.5,0.25)
        double coeursRestantsNonPleins = coeurs - nbCoeursRestantPleins;
        // number of lost hearts full
        int coeursPerduPleins = (int) coeursPerdu;

        int decalage = 0;

        // draw full heart
        for (int i = 0; i < nbCoeursRestantPleins; i++) {
            // gc.setFill(Color.RED);
            // gc.fillRect(10 + (decalage * 30), 5, 25, 25);
            gc.drawImage(Resources.getAsset("assets/coeurs.png"), 1, 1, 23, 22, 10 + (decalage * 30), 5, 25, 25);
            decalage++;
        }

        // draw if there is a heart not full
        if (coeursRestantsNonPleins == 0.75) {
            // gc.setFill(Color.BLUEVIOLET);
            // gc.fillRect(10 + (decalage * 30), 5, 25, 25);
            gc.drawImage(Resources.getAsset("assets/coeurs.png"), 26, 1, 23, 22, 10 + (decalage * 30), 5, 25, 25);
            decalage++;
        } else if (coeursRestantsNonPleins == 0.5) {
            // gc.setFill(Color.BLUE);
            // gc.fillRect(10 + (decalage * 30), 5, 25, 25);
            gc.drawImage(Resources.getAsset("assets/coeurs.png"), 51, 1, 23, 22, 10 + (decalage * 30), 5, 25, 25);

            decalage++;
        } else if (coeursRestantsNonPleins == 0.25) {
            // gc.setFill(Color.ORANGE);
            // gc.fillRect(10 + (decalage * 30), 5, 25, 25);
            gc.drawImage(Resources.getAsset("assets/coeurs.png"), 76, 1, 23, 22, 10 + (decalage * 30), 5, 25, 25);
            decalage++;
        }

        // draw the lost hearts
        for (int i = 0; i < coeursPerduPleins; i++) {
            // gc.setFill(Color.PINK);
            // gc.fillRect(10 + (decalage * 30), 5, 25, 25);
            gc.drawImage(Resources.getAsset("assets/coeurs.png"), 101, 1, 23, 22, 10 + (decalage * 30), 5, 25, 25);
            decalage++;
        }
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

    @Override
    public void save(boolean overwrite) {
        if (gameController == null) return;
        JSONObject json = gameController.getJSON();

        long date = System.currentTimeMillis();
        json.put("date", date);

        if (FileManager.write(json, SaveData.FOLDER + separator + date + ENCRYPTED.extension, true)) {
            if (save != null && overwrite) {
                save.deleteFromFS();
            }
            quit();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Resources.getI18NString("error.cannotSave"));
        alert.setHeaderText(Resources.getI18NString("error.cannotSave.message"));
        alert.setContentText(Resources.getI18NString("error.cannotSave.details"));
        alert.showAndWait();
    }

    @Override
    public void quit() {
        if (engine == null) return;
        engine.stop();
        engine = null;

        // go back to main menu
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    @Override
    public void resume() {
        if (engine == null) return;
        engine.paused.set(false);
    }
}
