package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.views.SaveComponent;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static fr.ul.acl.escape.Settings.locale;

public class SaveData {
    private SaveComponent.SaveButtonsListener listener;
    private final long timestamp;
    private final int level;
    private final int life;

    public SaveData(JSONObject json) {
        this.timestamp = json.has("date") ? json.getLong("date") : 0;
        this.level = json.has("level") ? json.getInt("level") : -1;
        this.life = json.has("life") ? json.getInt("life") : -1;
    }

    public void setListener(SaveComponent.SaveButtonsListener listener) {
        this.listener = listener;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM)
                .withLocale(locale.get())
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochMilli(timestamp));
    }

    public int getLevel() {
        return level;
    }

    public int getLife() {
        return life;
    }

    public void load() {
        listener.onLoad();
    }

    public void delete() {
        listener.onDelete();
    }
}
