package fr.ul.acl.escape.gui;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.Resources;
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
        Settings.locale.subscribe((evt, oldValue, newValue) -> {
            stage.setTitle(Resources.getI18NString("game.title"));
            views.forEach((viewName, view) -> view.controller.applyLanguage());
        });
        Settings.fullScreen.subscribe((evt, oldValue, newValue) -> stage.setFullScreen(newValue));
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
            stage.setScene(new Scene(new Group(), Donnees.WINDOW_DEFAULT_WIDTH, Donnees.WINDOW_DEFAULT_HEIGHT));
        }
        stage.getScene().setRoot(view.getRoot());
        view.enableRootEvents();
        view.onViewDisplayed();
    }

    /**
     * Quit the game.
     */
    public void quit() {
        stage.close();
    }
}
