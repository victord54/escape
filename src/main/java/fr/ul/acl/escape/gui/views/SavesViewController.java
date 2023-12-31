package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.SaveData;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewController;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.components.SaveComponent;
import fr.ul.acl.escape.outils.Resources;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Collection;

public class SavesViewController extends ViewController {
    @FXML
    private Label savesTitle;
    @FXML
    private ListView<SaveData> savesListView;
    @FXML
    private Label savesListViewEmptyMsg;
    @FXML
    private Button backButton;

    private ObservableList<SaveData> savesList;

    public void init() {
        savesList = savesListView.getItems();
        savesListView.setItems(savesList);
        savesListView.setCellFactory(param -> new SaveComponent());
    }

    @Override
    public void applyLanguage() {
        savesTitle.setText(Resources.getI18NString("saves"));
        savesListViewEmptyMsg.setText(Resources.getI18NString("saves.empty"));
        backButton.setText(Resources.getI18NString("back"));
        savesListView.refresh();
    }

    public void setSaves(Collection<SaveData> saveData) {
        savesList.setAll(saveData);
    }

    @FXML
    private void onClickBack() {
        ViewManager.getInstance().navigateTo(VIEWS.HOME);
    }
}
