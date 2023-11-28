package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameModeViewController extends ViewController {

    @FXML
    private Label gameModeTitle;
    @FXML
    private Button campaignButton;
    @FXML
    private Button customButton;
    @FXML
    private Button backButton;

    @Override
    public void applyLanguage() {
        gameModeTitle.setText(Resources.getI18NString("mode.title"));
        campaignButton.setText(Resources.getI18NString("mode.campaign"));
        customButton.setText(Resources.getI18NString("mode.custom"));
        backButton.setText(Resources.getI18NString("back"));
    }

    @FXML
    private void onClickCampaign() {
        ViewManager.getInstance().navigateTo(VIEWS.GAME);
    }

    @FXML
    private void onClickCustom() {
        ViewManager.getInstance().navigateTo(VIEWS.LEVELS);
    }

    @FXML
    private void onClickBack() {
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }
}
