package fr.ul.acl.escape.ui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.ui.ViewController;
import fr.ul.acl.escape.ui.ViewEvents;
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
        ((HomeController) controller).setGameTitle(gameTitle);
        ((HomeController) controller).setFullScreenCheckBox(Settings.getInstance().isFullScreen());
    }

    @Override
    public void onKeyPressed(ViewController controller, KeyEvent event) {
        if (event.getCode().toString().equals("ESCAPE")) {
            ((HomeController) controller).setFullScreenCheckBox(false);
        }
    }

    @Override
    public void onViewDisplayed(ViewController controller) {
    }
}
