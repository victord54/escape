package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Save;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        List<JSONObject> saves1 = FileManager.readDirectory("saves", true);
        List<JSONObject> saves = new ArrayList<>(saves1);
        saves.add(new JSONObject() {{
            put("date", 0);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 1);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 2);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 3);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 4);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 5);
            put("level", -1);
            put("life", -1);
        }});
        saves.add(new JSONObject() {{
            put("date", 6);
            put("level", -1);
            put("life", -1);
        }});

        List<Save> savesList = new ArrayList<>();
        for (JSONObject save : saves) {
            Save s = new Save(save);
            s.setListener(new SaveComponent.SaveButtonsListener() {
                @Override
                public void onLoad() {
                    // TODO: load file
                    System.out.println("load");
                }

                @Override
                public void onDelete() {
                    savesList.remove(s);
                    controller.setSaves(savesList);
                    // TODO: delete file
                }
            });
            savesList.add(s);
        }
        savesList.sort(Comparator.comparingLong(Save::getTimestamp).reversed());
        controller.initialize();
        controller.setSaves(savesList);
        controller.applyLanguage();
    }
}
