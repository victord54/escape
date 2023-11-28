package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.components.SaveComponent;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.Resources;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

import static fr.ul.acl.escape.Settings.locale;

/**
 * Represents a save file.
 */
public class SaveData {
    /**
     * The folder where the saves are stored.
     */
    public static final String FOLDER = "saves";
    /**
     * The filename of the save.
     */
    private final String filename;
    /**
     * The JSON representation of the save.
     */
    private final JSONObject json;
    /**
     * The actions to perform when the save is loaded or deleted.
     */
    private SaveComponent.SaveButtonsListener listener;

    /**
     * Create a SaveData from existing data.
     *
     * @param saveEntry the save data, as a map entry (filename, JSON)
     */
    public SaveData(Map.Entry<String, JSONObject> saveEntry) {
        this.filename = saveEntry.getKey();
        this.json = saveEntry.getValue();
    }

    public String getFilename() {
        return filename;
    }

    public JSONObject getJSON() {
        return json;
    }

    public long getTimestamp() {
        return json.has("date") ? json.getLong("date") : 0;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM).withLocale(locale.get()).withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochMilli(getTimestamp()));
    }

    public int getLevel() {
        return json.has("level") ? json.getInt("level") : -1;
    }

    public String getLife() {
        if (!json.has("entities")) return "-";

        JSONArray entities = json.getJSONArray("entities");
        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            if (entity.getString("type").equals("HERO")) {
                String life = entity.has("life") ? entity.getDouble("life") + "" : "-";
                String maxLife = entity.has("maxLife") ? entity.getDouble("maxLife") + "" : "-";
                return life + "/" + maxLife;
            }
        }

        return "-";
    }

    public String getModeStr() {
        GameMode mode = json.has("mode") ? GameMode.valueOf(json.getString("mode")) : null;
        return mode != null ? Resources.getI18NString("mode." + mode.name().toLowerCase()) : "-";
    }

    public void setListener(SaveComponent.SaveButtonsListener listener) {
        this.listener = listener;
    }

    /**
     * Should load the save and start the game.
     */
    public void onLoad() {
        listener.onLoad();
    }

    /**
     * Should delete the save.
     */
    public void onDelete() {
        listener.onDelete();
    }

    /**
     * Delete the save file from the file system.
     */
    public void deleteFromFS() {
        FileManager.delete(this.getFilename());
    }
}
