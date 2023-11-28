package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.GameMode;
import fr.ul.acl.escape.LevelData;
import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.outils.ErrorBehavior;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LevelsView extends View {
    public LevelsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("levels-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    /**
     * Create a {@link LevelData} from a {@link Map.Entry}
     *
     * @param levelEntry the entry
     * @param levelsList the list of saves
     * @param controller the view controller
     * @return the save data
     */
    private static LevelData getLevelData(Map.Entry<String, JSONObject> levelEntry, List<LevelData> levelsList, LevelsViewController controller) {
        LevelData levelData = new LevelData(levelEntry);
        levelData.setListener(() -> {
            try {
                File file = FileManager.getFile(levelData.getFilename());
                if (file == null) {
                    throw new Exception("File not found");
                }
                Monde monde = Monde.fromMap(file.getName(), GameMode.CUSTOM);
                ViewManager.getInstance().navigateTo(VIEWS.GAME, monde);
            } catch (Exception e) {
                ErrorBehavior.handle(e, "Failed to load level '" + levelData.getFilename() + "'.");
            }
        });
        return levelData;
    }

    @Override
    public void onViewDisplayed(Object... args) {
        super.onViewDisplayed();
        LevelsViewController controller = (LevelsViewController) this.controller;

        Map<String, JSONObject> levels = FileManager.readDirectory(LevelData.FOLDER, false);

        List<LevelData> levelsList = new ArrayList<>();
        for (Map.Entry<String, JSONObject> level : levels.entrySet()) {
            LevelData levelData = getLevelData(level, levelsList, controller);
            levelsList.add(levelData);
        }
        levelsList.sort(Comparator.comparing(LevelData::getFilename));

        controller.init();
        controller.setLevels(levelsList);
        controller.applyLanguage();
    }
}
