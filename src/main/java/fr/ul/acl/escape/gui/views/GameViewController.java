package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameViewController extends ViewController {
    @FXML
    private VBox endMenu;
    @FXML
    private Label endTitle;
    @FXML
    private Button endReplayButton;
    @FXML
    private Button endQuitButton;
    @FXML
    private StackPane pane;
    @FXML
    private Canvas gameBoard;
    @FXML
    private Canvas overlay;
    @FXML
    private VBox pauseMenu;
    @FXML
    private Label pauseTitle;
    @FXML
    private Button resumeButton;
    @FXML
    private Button saveNewButton;
    @FXML
    private Button saveOverwriteButton;
    @FXML
    private Button quitButton;
    private ButtonsListener buttonsListener;

    public StackPane getPane() {
        return pane;
    }

    public Canvas getGameBoard() {
        return gameBoard;
    }

    public Canvas getOverlay() {
        return overlay;
    }

    @Override
    public void applyLanguage() {
        pauseTitle.setText(Resources.getI18NString("game.pause"));
        resumeButton.setText(Resources.getI18NString("game.resume"));
        saveNewButton.setText(Resources.getI18NString("save.saveAndQuit"));
        saveOverwriteButton.setText(Resources.getI18NString("save.overwrite"));
        quitButton.setText(Resources.getI18NString("save.quit"));
        endTitle.setText(Resources.getI18NString("game.end"));
        endReplayButton.setText(Resources.getI18NString("game.replay"));
        endQuitButton.setText(Resources.getI18NString("game.quit"));
    }

    /**
     * Set the listener for the buttons.
     *
     * @param listener the listener
     */
    public void setButtonsListener(ButtonsListener listener) {
        this.buttonsListener = listener;
    }

    /**
     * Set the visibility of the pause menu.
     *
     * @param visible true to show the pause menu, false to hide it
     * @param hasSave true if there is a save to overwrite, false otherwise
     */
    public void setPauseMenuVisible(boolean visible, boolean hasSave) {
        saveNewButton.setText(hasSave ? Resources.getI18NString("save.saveNew") : Resources.getI18NString("save.saveAndQuit"));
        saveOverwriteButton.setDisable(!hasSave);
        pauseMenu.setVisible(visible);
        pauseMenu.setDisable(!visible);
        if (visible) resumeButton.requestFocus();
    }

    /**
     * Set the visibility of the end menu.
     *
     * @param visible true to show the end menu, false to hide it
     */
    public void setEndMenuVisible(boolean visible) {
        endMenu.setVisible(visible);
        endMenu.setDisable(!visible);
        if (visible) endReplayButton.requestFocus();
    }

    @FXML
    private void onClickResume() {
        if (this.buttonsListener == null) return;
        this.buttonsListener.resume();
    }

    @FXML
    private void onClickSaveNew() {
        if (this.buttonsListener == null) return;
        this.buttonsListener.save(false);
    }

    @FXML
    private void onClickSaveOverwrite() {
        if (this.buttonsListener == null) return;
        this.buttonsListener.save(true);
    }

    @FXML
    private void onClickQuit() {
        if (this.buttonsListener == null) return;
        this.buttonsListener.quit();
    }

    public void onClickReplay() {
        if (this.buttonsListener == null) return;
        this.buttonsListener.replay();
    }

    /**
     * The listener for the buttons of the game view.
     */
    public interface ButtonsListener {
        /**
         * Action to perform when the user clicks on the save button.
         *
         * @param overwrite true to overwrite the save, false to create a new one
         */
        void save(boolean overwrite);

        /**
         * Action to perform when the user clicks on the quit button.
         */
        void quit();

        /**
         * Action to perform when the user clicks on the resume button.
         */
        void resume();

        /**
         * Action to perform when the user clicks on the replay button.
         */
        void replay();
    }
}
