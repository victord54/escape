package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.views.SaveComponent;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static fr.ul.acl.escape.Settings.locale;

public class Save {
    private SaveComponent.SaveButtonsListener listener;
    private final long timestamp;
    private final String date;
    private final int level;
    private final int life;

    public Save(JSONObject json) {
        if (!json.has("date")) {
            this.date = "";
            this.timestamp = 0;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM)
                    .withLocale(locale.get())
                    .withZone(ZoneId.systemDefault());
            timestamp = json.getLong("date");


            this.date = formatter.format(Instant.ofEpochMilli(timestamp));
        }
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
        return date;
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
