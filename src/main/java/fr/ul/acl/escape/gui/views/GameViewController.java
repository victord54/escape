package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
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
    private Button saveAndQuitButton;
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
        saveAndQuitButton.setText(Resources.getI18NString("game.saveAndQuit"));
        quitButton.setText(Resources.getI18NString("game.quit"));
    }

    public void setButtonsListener(ButtonsListener listener) {
        this.buttonsListener = listener;
    }

    public void setPauseMenuVisible(boolean visible) {
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
    private void onClickSaveAndQuit() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.save();
            this.buttonsListener.quit();
        }

        // go back to the home view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    @FXML
    private void onClickQuit() {
        // call the listener
        if (this.buttonsListener != null) {
            this.buttonsListener.quit();
        }

        // go back to the home view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }

    public interface ButtonsListener {
        void save();

        void quit();

        void resume();
    }
}
