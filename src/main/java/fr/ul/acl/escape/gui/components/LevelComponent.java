package fr.ul.acl.escape.gui.components;

import fr.ul.acl.escape.LevelData;
import fr.ul.acl.escape.outils.ErrorBehavior;
import fr.ul.acl.escape.outils.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LevelComponent extends ListCell<LevelData> {
    @FXML
    private Pane pane;
    @FXML
    private Label levelName;
    @FXML
    private Button loadButton;

    private FXMLLoader loader;

    @Override
    protected void updateItem(LevelData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            init();

            levelName.setText(item.getTitle());

            loadButton.setOnAction(this::load);
            loadButton.setText(Resources.getI18NString("load"));

            setText(null);
            setGraphic(pane);
        }
    }

    /**
     * Initialize the loader and load the FXML file.
     */
    private void init() {
        if (loader == null) {
            loader = new FXMLLoader(getClass().getResource("level-component.fxml"));
            loader.setResources(Resources.getI18NBundle());
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                ErrorBehavior.handle(e, "Failed to load 'level-component.fxml' component.");
            }
        }
    }

    @FXML
    private void load(ActionEvent event) {
        getItem().onLoad();
    }

    /**
     * The listener for the buttons of the level component.
     */
    public interface LevelButtonsListener {
        /**
         * Called when the load button is clicked.
         */
        void onLoad();
    }
}
