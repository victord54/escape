package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.*;
import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.engine.GUIController;
import fr.ul.acl.escape.gui.engine.GUIEngine;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.ErrorBehavior;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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

    private int iterationHeros = 0;

    /**
     * Previous save data if the game is loaded from a save.
     */
    private SaveData save;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
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
        save = null;
        if (args.length > 0 && args[0] instanceof SaveData) {
            save = (SaveData) args[0];
            gameController = new GUIController(save.getJSON());
        } else if (args.length > 0 && args[0] instanceof Monde) {
            gameController = new GUIController((Monde) args[0]);
        } else {
            gameController = new GUIController();
        }

        // init game board
        controller.setPauseMenuVisible(false, save != null);
        controller.setEndMenuVisible(false);
        controller.setButtonsListener(this);

        StackPane centerPane = controller.getPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        gameBoard = controller.getGameBoard();
        overlay = controller.getOverlay();

        // binding game board to center pane
        bindingGameBoardToCenterPane(controller);

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
            gameController.setOnPause(newValue);
        });
        engine.gameOver.subscribe((evt, oldValue, newValue) -> controller.setEndMenuVisible(newValue));
        engine.start();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        KeyBindings keyBindings = Settings.keyBindings.get();
        if (event.getCode() == keyBindings.getKey(KeyAction.PAUSE) && !engine.gameOver.get()) {
            if (engine != null) engine.paused.set(!engine.paused.get());
        } else if (event.getCode() == keyBindings.getKey(KeyAction.SHOW_FPS)) {
            Settings.showFps.set(!Settings.showFps.get());
        } else if (event.getCode() == keyBindings.getKey(KeyAction.GRID) && Donnees.DEBUG) {
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
        GameViewController controller = (GameViewController) this.controller;
        bindingGameBoardToCenterPane(controller);

        if (gameController.getOnOver())
            engine.gameOver.set(true);
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
            for (int i = 0; i < gameController.getWidth(); i++) {
                for (int j = 0; j < gameController.getHeight(); j++) {
                    gc.setFill(i % 2 + j % 2 == 1 ? Color.LIGHTGRAY : Color.GRAY);
                    gc.fillRect(i * elementSize, j * elementSize, elementSize, elementSize);
                }
            }
        }

        // draw game environment
        this.gameController.getTerrains().forEach(terrain -> renderElement(gc, terrain, elementSize, 0, false));

        if (engine == null) return;

        // draw game objects
        this.gameController.getObjets().forEach(objet -> {
            if (objet.isVisible()) {
                renderElement(gc, objet, elementSize, (int) (engine.getLastUpdate() / 1e8), false);
            }
        });

        // draw game entities
        this.gameController.getPersonnages().forEach(personnage -> {
            if (personnage.estUnHeros()) {
                if (personnage.isMoving() && !(engine.paused.get() || engine.gameOver.get())) {
                    iterationHeros = (int) (engine.getLastUpdate() / 1e8);
                }
                renderElement(gc, personnage, elementSize, iterationHeros, false);
            } else {
                int iteration = 0;
                if (personnage.isMoving() && !(engine.paused.get() || engine.gameOver.get())) {
                    iteration = (int) (engine.getLastUpdate() / 1e8);
                }
                if (personnage.peutTraverserObstacles() && this.gameController.collisionAvecTerrains(personnage)) {
                    gc.setGlobalAlpha(0.5);
                }
                renderElement(gc, personnage, elementSize, iteration, true);
                gc.setGlobalAlpha(1.0);
            }
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

        // pause menu or end menu
        if (engine.paused.get() || engine.gameOver.get()) {
            gc.setFill(new Color(0, 0, 0, 0.75));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        // draw hero info
        Heros heros = this.gameController.getHeros();
        drawHearts(gc, heros.getCoeurs(), heros.getMaxCoeurs(), 10, 5);
        drawTraining(gc, heros.getTrainingProgress(), -10, 5);

        // draw level info
        if (gameController.getGameMode() == GameMode.CAMPAIGN) {
            Font oldFont = gc.getFont();
            TextAlignment oldAlign = gc.getTextAlign();
            gc.setFont(Font.font(gc.getFont().getFamily(), FontWeight.BOLD, 20));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(Color.WHITE);
            gc.fillText(Resources.getI18NString("game.level") + " " + gameController.getCurrentLevelDifficulty(), canvas.getWidth() / 2, gc.getFont().getSize() + 5);

            gc.setFont(oldFont);
            gc.setTextAlign(oldAlign);
        }

        // write FPS
        if (drawFPS) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillText("FPS: " + engine.getFPS(), 10, canvas.getHeight() - 10);
        }
    }


    private void drawHearts(GraphicsContext gc, double coeurs, double coeursMax, double posX, double posY) {
        if (posX < 0) posX = gc.getCanvas().getWidth() + posX - 30.0 * coeursMax;
        if (posY < 0) posY = gc.getCanvas().getHeight() + posY - 25;


        // number of lost hearts
        double coeursPerdu = coeursMax - coeurs;
        // number of full hearts remaining
        int nbCoeursRestantPleins = (int) (coeurs);
        // quartile of heats remaining (0.75,0.5,0.25)
        double coeursRestantsNonPleins = coeurs - nbCoeursRestantPleins;
        // number of lost hearts full
        int coeursPerduPleins = (int) coeursPerdu;

        int decalage = 0;

        Image img = Resources.getAsset("assets/coeurs.png");
        gc.setFill(Color.LIGHTGREEN);
        // draw full heart
        for (int i = 0; i < nbCoeursRestantPleins; i++) {
            if (img != null) {
                gc.drawImage(img, 1, 1, 23, 22, posX + (decalage * 30), posY, 25, 25);
            } else {
                gc.fillOval(posX + (decalage * 30), posY, 25, 25);
            }
            decalage++;
        }

        // draw if there is a heart not full
        if (coeursRestantsNonPleins >= 0.75) {
            if (img != null) {
                gc.drawImage(img, 26, 1, 23, 22, posX + (decalage * 30), posY, 25, 25);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillOval(posX + (decalage * 30), posY, 25, 25);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillArc(posX + (decalage * 30), posY, 25, 25, 90, 360, ArcType.ROUND);
            }
            decalage++;
        } else if (coeursRestantsNonPleins >= 0.5) {
            if (img != null) {
                gc.drawImage(img, 51, 1, 23, 22, posX + (decalage * 30), posY, 25, 25);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillOval(posX + (decalage * 30), posY, 25, 25);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillArc(posX + (decalage * 30), posY, 25, 25, 90, 270, ArcType.ROUND);
            }
            decalage++;
        } else if (coeursRestantsNonPleins >= 0.25) {
            if (img != null) {
                gc.drawImage(img, 76, 1, 23, 22, posX + (decalage * 30), posY, 25, 25);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillOval(posX + (decalage * 30), posY, 25, 25);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillArc(posX + (decalage * 30), posY, 25, 25, 90, 180, ArcType.ROUND);
            }
            decalage++;
        }

        // draw the lost hearts
        for (int i = 0; i < coeursPerduPleins; i++) {
            if (img != null) {
                gc.drawImage(img, 101, 1, 23, 22, posX + (decalage * 30), posY, 25, 25);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillOval(posX + (decalage * 30), posY, 25, 25);
            }
            decalage++;
        }
    }

    private void drawTraining(GraphicsContext gc, int progress, double posX, double posY) {
        int size = 32;

        if (posX < 0) posX = gc.getCanvas().getWidth() + posX - size;
        if (posY < 0) posY = gc.getCanvas().getHeight() + posY - size;

        Image img = Resources.getAsset("assets/duck.png");
        ColorAdjust desaturate = new ColorAdjust();

        // semi-transparent image and desaturated
        desaturate.setSaturation(-1);
        gc.setEffect(desaturate);
        gc.setGlobalAlpha(0.5);
        if (img != null) {
            gc.drawImage(img, 99, 1, 26, 26, posX, posY, size, size);
            gc.setEffect(null);
        } else {
            gc.setFill(Color.YELLOW);
            gc.fillRect(posX, posY, size, size);
        }

        // full image
        desaturate.setSaturation(progress >= 100 ? 0 : -0.5);
        gc.setEffect(desaturate);
        gc.setGlobalAlpha(1.0);
        double sizeToDraw = size * progress / 100.0;
        if (img != null) {
            double sizeOfImage = 26.0 * progress / 100.0;
            gc.drawImage(img, 99, 1, sizeOfImage, 26, posX, posY, sizeToDraw, size);
            gc.setEffect(null);
        } else {
            gc.setFill(Color.YELLOW);
            gc.fillRect(posX, posY, sizeToDraw, size);
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
        JSONObject json = gameController.getJSONWorld();

        long date = System.currentTimeMillis();
        json.put("date", date);

        if (FileManager.write(json, SaveData.FOLDER + separator + date + ENCRYPTED.extension, true)) {
            if (save != null && overwrite) {
                save.deleteFromFS();
            }
            quit();
            return;
        }

        ErrorBehavior.showWarning(
                Resources.getI18NString("error.cannotSave"),
                Resources.getI18NString("error.cannotSave.message"),
                Resources.getI18NString("error.cannotSave.details"),
                true
        );
    }

    @Override
    public void quit() {
        // stop engine
        if (engine != null) {
            engine.stop();
            engine = null;
        }

        // go back to main menu
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    @Override
    public void resume() {
        if (engine == null) return;
        engine.paused.set(false);
        gameController.setOnPause(false);
    }

    @Override
    public void replay() {
        if (engine == null) return;
        engine.stop();
        engine = null;
        ViewManager.getInstance().navigateTo(VIEWS.GAME);
    }

    /**
     * Draw an element on the canvas.
     *
     * @param gc           The graphics context of the canvas.
     * @param elementMonde The element to draw.
     * @param elementSize  The size of a square element on the game board.
     * @param spriteIndex  The index of the sprite to draw.
     * @param drawPV       If true, the PV of the element are drawn (only for entities).
     */
    private void renderElement(GraphicsContext gc, ElementMonde elementMonde, double elementSize, int spriteIndex, boolean drawPV) {
        Image sprite = elementMonde.getSprite(spriteIndex);
        if (sprite == null) {
            gc.setFill(elementMonde.getColor());
            gc.fillRect(elementMonde.getX() * elementSize, elementMonde.getY() * elementSize, Math.ceil(elementMonde.getLargeur() * elementSize), Math.ceil(elementMonde.getHauteur() * elementSize));
        } else {
            gc.drawImage(sprite, elementMonde.getX() * elementSize, elementMonde.getY() * elementSize, elementMonde.getLargeur() * elementSize, elementMonde.getHauteur() * elementSize);
        }

        if (drawPV) {
            double heightPVBar = elementSize / 16.0;

            Personnage personnage = (Personnage) elementMonde;
            gc.setFill(Color.RED);
            gc.fillRect(personnage.getX() * elementSize, personnage.getY() * elementSize - heightPVBar * 2, personnage.getLargeur() * elementSize, heightPVBar);
            gc.setFill(Color.GREEN);
            gc.fillRect(personnage.getX() * elementSize, personnage.getY() * elementSize - heightPVBar * 2, personnage.getLargeur() * elementSize * personnage.getCoeurs() / personnage.getMaxCoeurs(), heightPVBar);
        }
    }

    /**
     * Updates the dimensions of the game board based on the dimensions of the center pane.
     * <p>
     * This method calculates the appropriate size for elements on the game board
     * based on the dimensions of the center pane and binds the game board dimensions accordingly.
     * </p>
     *
     * @param controller The GameViewController associated with the game board.
     * @see GameViewController
     */
    private void bindingGameBoardToCenterPane(GameViewController controller) {
        StackPane centerPane = controller.getPane();

        elementSize = Bindings.min(centerPane.widthProperty().divide(gameController.getWidth()), centerPane.heightProperty().divide(gameController.getHeight()));
        gameBoard.widthProperty().bind(elementSize.multiply(gameController.getWidth()));
        gameBoard.heightProperty().bind(elementSize.multiply(gameController.getHeight()));
    }
}
