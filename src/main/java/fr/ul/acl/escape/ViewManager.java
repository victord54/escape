package fr.ul.acl.escape;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class ViewManager {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private Stage stage;
    public final HashMap<VIEWS, View> views = new HashMap<>();

    private static ViewManager instance;

    private ViewManager() {}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void navigateTo(VIEWS viewName) {
        View view = views.get(viewName);
        view.onViewInit();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(new Group(), DEFAULT_WIDTH, DEFAULT_HEIGHT));
        }
        stage.getScene().setRoot(view.getRoot());
        view.onViewDisplayed();
    }

    public void setFullScreen(boolean fullScreen) {
        Settings.getInstance().setFullScreen(fullScreen);
        stage.setFullScreen(fullScreen);
    }
}
