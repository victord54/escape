package fr.ul.acl.escape.gui;

import fr.ul.acl.escape.Settings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * Manage the views of the game.
 * Singleton class.
 */
public class ViewManager {
    /**
     * Default width of the window when the game starts.
     */
    private static final int DEFAULT_WIDTH = 800;
    /**
     * Default height of the window when the game starts.
     */
    private static final int DEFAULT_HEIGHT = 600;
    /**
     * The instance of the view manager.
     */
    private static ViewManager instance;
    /**
     * The registered game views in the manager.
     */
    private final HashMap<VIEWS, View> views;
    /**
     * The stage of the game.
     */
    private Stage stage;

    private ViewManager() {
        this.views = new HashMap<>();
    }

    /**
     * Get the instance of the view manager.
     */
    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    /**
     * Set the stage of the game. This method should be called at the start of the game.
     *
     * @param stage The stage of the game.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Register a view in the manager.
     *
     * @param viewName The identifier of the view.
     * @param view     The view to register.
     */
    public void registerView(VIEWS viewName, View view) {
        views.put(viewName, view);
    }

    /**
     * Navigate to the named view.
     *
     * @param viewName The identifier of the view to navigate to.
     */
    public void navigateTo(VIEWS viewName) {
        View view = views.get(viewName);
        view.onViewInit();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(new Group(), DEFAULT_WIDTH, DEFAULT_HEIGHT));
        }
        stage.getScene().setRoot(view.getRoot());
        view.enableRootEvents();
        view.onViewDisplayed();
    }

    /**
     * Set the full screen mode of the game.
     * This method will also update the settings.
     *
     * @param fullScreen True to enable full screen mode, false otherwise.
     */
    public void setFullScreen(boolean fullScreen) {
        Settings.getInstance().setFullScreen(fullScreen);
        stage.setFullScreen(fullScreen);
    }
}
