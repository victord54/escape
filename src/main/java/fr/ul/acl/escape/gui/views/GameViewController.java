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
    }

    public void setButtonsListener(ButtonsListener listener) {
        this.buttonsListener = listener;
    }

    public void setPauseMenuVisible(boolean visible, boolean hasSave) {
        saveNewButton.setText(hasSave ? Resources.getI18NString("save.saveNew") : Resources.getI18NString("save.saveAndQuit"));
        saveOverwriteButton.setDisable(!hasSave);
        pauseMenu.setVisible(visible);
        pauseMenu.setDisable(!visible);
        if (visible) resumeButton.requestFocus();
    }

    @FXML
    private void onClickResume() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.resume();
        }
    }

    @FXML
    private void onClickSaveNew() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.save(false);
        }
    }

    @FXML
    private void onClickSaveOverwrite() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.save(true);
        }
    }

    @FXML
    private void onClickQuit() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.quit();
        }
    }

    public interface ButtonsListener {
        void save(boolean overwrite);

        void quit();

        void resume();
    }
}
