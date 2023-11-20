package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.SaveData;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SavesView extends View {
    public SavesView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/saves-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    /**
     * Create a {@link SaveData} from a {@link Map.Entry}
     *
     * @param saveEntry  the entry
     * @param savesList  the list of saves
     * @param controller the view controller
     * @return the save data
     */
    private static SaveData getSaveData(Map.Entry<String, JSONObject> saveEntry, List<SaveData> savesList, SavesViewController controller) {
        SaveData saveData = new SaveData(saveEntry);
        saveData.setListener(new SaveComponent.SaveButtonsListener() {
            @Override
            public void onLoad() {
                ViewManager.getInstance().navigateTo(VIEWS.GAME, saveData);
            }

            @Override
            public void onDelete() {
                savesList.remove(saveData);
                controller.setSaves(savesList);
                saveData.deleteFromFS();
            }
        });
        return saveData;
    }

    @Override
    public void onViewDisplayed(Object... args) {
        super.onViewDisplayed();
        SavesViewController controller = (SavesViewController) this.controller;

        Map<String, JSONObject> saves = FileManager.readDirectory(SaveData.FOLDER, true);

        List<SaveData> savesList = new ArrayList<>();
        for (Map.Entry<String, JSONObject> save : saves.entrySet()) {
            SaveData saveData = getSaveData(save, savesList, controller);
            savesList.add(saveData);
        }
        savesList.sort(Comparator.comparingLong(SaveData::getTimestamp).reversed());

        controller.init();
        controller.setSaves(savesList);
        controller.applyLanguage();
    }
}
