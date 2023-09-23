package fr.ul.acl.escape;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Escape extends Application {
    public static final String GAME_TITLE = "Escape";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.getInstance().setStage(stage);
        ViewManager.getInstance().views.put(VIEWS.HOME, new View(new FXMLLoader(Escape.class.getResource("home-view.fxml")), new ViewEvents() {
            @Override
            public void onDisplayed(ViewController controller) {
                ((HomeController) controller).setGameTitle(GAME_TITLE);
                ((HomeController) controller).setFullScreenCheckBox(Settings.getInstance().isFullScreen());
            }

            @Override
            public void onKeyPressed(ViewController controller, KeyEvent event) {
                if (event.getCode().toString().equals("ESCAPE")) {
                    ((HomeController) controller).setFullScreenCheckBox(false);
                }
            }
        }));
        ViewManager.getInstance().views.put(VIEWS.GAME, new View(new FXMLLoader(Escape.class.getResource("game-view.fxml")), new GameView()));
        ViewManager.getInstance().navigateTo(VIEWS.HOME);

        stage.setTitle(GAME_TITLE);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }
}