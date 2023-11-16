package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.views.SaveComponent;
import fr.ul.acl.escape.outils.FileManager;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

import static fr.ul.acl.escape.Settings.locale;

public class SaveData {
    private final String filename;
    private final JSONObject json;
    private SaveComponent.SaveButtonsListener listener;

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

    public void setListener(SaveComponent.SaveButtonsListener listener) {
        this.listener = listener;
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

    public int getLife() {
        return json.has("life") ? json.getInt("life") : -1;
    }

    public void onLoad() {
        listener.onLoad();
    }

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
