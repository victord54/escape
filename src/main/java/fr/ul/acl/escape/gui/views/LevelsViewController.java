package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.LevelData;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.components.LevelComponent;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Collection;

public class LevelsViewController extends ViewController {
    @FXML
    private Label levelsTitle;
    @FXML
    private ListView<LevelData> levelsListView;
    @FXML
    private Label levelsListViewEmptyMsg;
    @FXML
    private Button openFolderButton;
    @FXML
    private Button backButton;

    private ObservableList<LevelData> levelsList;

    public void init() {
        levelsList = levelsListView.getItems();
        levelsListView.setItems(levelsList);
        levelsListView.setCellFactory(param -> new LevelComponent());
    }

    @Override
    public void applyLanguage() {
        levelsTitle.setText(Resources.getI18NString("levels"));
        levelsListViewEmptyMsg.setText(Resources.getI18NString("levels.empty"));
        openFolderButton.setText(Resources.getI18NString("levels.openFolder"));
        backButton.setText(Resources.getI18NString("back"));
        levelsListView.refresh();
    }

    public void setLevels(Collection<LevelData> levelData) {
        levelsList.setAll(levelData);
    }

    @FXML
    private void openFolder() {
        FileManager.openFolder(LevelData.FOLDER);
    }

    @FXML
    private void onClickBack() {
        ViewManager.getInstance().navigateTo(VIEWS.GAME_MODE);
    }
}
