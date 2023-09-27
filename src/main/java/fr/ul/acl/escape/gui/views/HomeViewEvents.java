package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewEvents;
import javafx.scene.input.KeyEvent;

public class HomeViewEvents implements ViewEvents {
    /**
     * The title of the game that will be displayed in the home view.
     */
    private final String gameTitle;

    public HomeViewEvents(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    @Override
    public void onViewInit(ViewController controller) {
        ((HomeViewController) controller).setGameTitle(gameTitle);
        ((HomeViewController) controller).setFullScreenCheckBox(Settings.getInstance().isFullScreen());
    }

    @Override
    public void onViewDisplayed(ViewController controller) {
    }

    @Override
    public void onKeyPressed(ViewController controller, KeyEvent event) {
        if (event.getCode().toString().equals("ESCAPE")) {
            ((HomeViewController) controller).setFullScreenCheckBox(false);
        }
    }

    @Override
    public void onKeyReleased(ViewController controller, KeyEvent event) {
    }
}
