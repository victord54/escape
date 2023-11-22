package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.SaveData;
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

public class SaveComponent extends ListCell<SaveData> {
    @FXML
    private Pane pane;
    @FXML
    private Label date;
    @FXML
    private Label dateValue;
    @FXML
    private Label level;
    @FXML
    private Label levelValue;
    @FXML
    private Label life;
    @FXML
    private Label lifeValue;
    @FXML
    private Button loadButton;
    @FXML
    private Button deleteButton;

    private FXMLLoader loader;

    @Override
    protected void updateItem(SaveData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            init();

            date.setText(Resources.getI18NString("save.date") + " ");
            dateValue.setText(item.getDate());

            level.setText(Resources.getI18NString("save.level") + " ");
            levelValue.setText(item.getLevel() + "");

            life.setText(Resources.getI18NString("save.life") + " ");
            lifeValue.setText(item.getLife());

            loadButton.setOnAction(this::load);
            loadButton.setText(Resources.getI18NString("load"));

            deleteButton.setOnAction(this::delete);
            deleteButton.setText(Resources.getI18NString("delete"));

            setText(null);
            setGraphic(pane);
        }
    }

    /**
     * Initialize the component if it is not already done.
     */
    private void init() {
        if (loader == null) {
            loader = new FXMLLoader(Resources.get("gui/save-component.fxml"));
            loader.setResources(Resources.getI18NBundle());
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                ErrorBehavior.handle(e, "Failed to load 'save-component.fxml' component.");
            }
        }
    }

    @FXML
    private void load(ActionEvent actionEvent) {
        getItem().onLoad();
    }

    @FXML
    private void delete(ActionEvent actionEvent) {
        getItem().onDelete();
    }

    /**
     * The listener for the buttons of the save component.
     */
    public interface SaveButtonsListener {
        /**
         * Called when the load button is clicked.
         */
        void onLoad();

        /**
         * Called when the delete button is clicked.
         */
        void onDelete();
    }
}
