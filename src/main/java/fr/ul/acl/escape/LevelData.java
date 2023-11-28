package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.components.LevelComponent;
import fr.ul.acl.escape.outils.FileManager;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class LevelData {
    /**
     * The folder where the levels are stored.
     */
    public static final String FOLDER = "maps";
    /**
     * The filename of the level.
     */
    private final String filename;
    /**
     * The action to perform when the level is loaded.
     */
    private LevelComponent.LevelButtonsListener listener;

    /**
     * Create a LevelData from existing data.
     *
     * @param levelEntry the level data, as a map entry (filename, JSON)
     */
    public LevelData(Map.Entry<String, JSONObject> levelEntry) {
        this.filename = levelEntry.getKey();
    }

    public String getFilename() {
        return filename;
    }

    public String getTitle() {
        File file = FileManager.getFile(this.getFilename());
        if (file == null) return "-";
        return file.getName().replace(JSON.extension, "");
    }

    public void setListener(LevelComponent.LevelButtonsListener listener) {
        this.listener = listener;
    }

    /**
     * Should load the level and start the game.
     */
    public void onLoad() {
        if (listener != null) listener.onLoad();
    }
}
