package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.SaveData;
import fr.ul.acl.escape.gui.View;
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

    @Override
    public void onViewDisplayed() {
        super.onViewDisplayed();
        SavesViewController controller = (SavesViewController) this.controller;

        Map<String, JSONObject> saves = FileManager.readDirectory("saves", true);

        List<SaveData> savesList = new ArrayList<>();
        for (Map.Entry<String, JSONObject> save : saves.entrySet()) {
            SaveData saveData = getSaveData(save, savesList, controller);
            savesList.add(saveData);
        }
        savesList.sort(Comparator.comparingLong(SaveData::getTimestamp).reversed());

        controller.initialize();
        controller.setSaves(savesList);
        controller.applyLanguage();
    }

    private static SaveData getSaveData(Map.Entry<String, JSONObject> saveEntry, List<SaveData> savesList, SavesViewController controller) {
        SaveData saveData = new SaveData(saveEntry.getValue());
        saveData.setListener(new SaveComponent.SaveButtonsListener() {
            @Override
            public void onLoad() {
                // TODO: load file
                System.out.println("load");
            }

            @Override
            public void onDelete() {
                savesList.remove(saveData);
                controller.setSaves(savesList);
                FileManager.delete(saveEntry.getKey());
            }
        });
        return saveData;
    }
}
