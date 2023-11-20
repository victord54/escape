package fr.ul.acl.escape;

import fr.ul.acl.escape.outils.FileManager;
import org.apache.commons.lang3.LocaleUtils;
import org.json.JSONObject;

import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Locale;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;
import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

public class Settings {
    /**
     * Path to the settings file.
     */
    private static final String SETTINGS_FILEPATH = "settings" + JSON.extension;
    /**
     * Common {@link PropertyChangeSupport} instance.
     */
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(Settings.class);

    public static final Property<Boolean> fullScreen = new Property<>(pcs, "fullScreen", false).setLog(DEBUG);
    public static final Property<Boolean> showFps = new Property<>(pcs, "showFps", false).setLog(DEBUG);
    public static final Property<Locale> locale = new Property<>(pcs, "locale", (Locale) null).setLog(DEBUG);

    /**
     * Whether the auto save has been initialized.
     */
    private static boolean autoSaveInitialized = false;

    /**
     * Restore the default settings.
     */
    public static void reset() {
        fullScreen.set(false);
        showFps.set(false);
        locale.set(Locale.getDefault());
    }

    /**
     * Save the settings to the settings file.
     */
    public static void save() {
        JSONObject json = new JSONObject();
        Arrays.asList(fullScreen, showFps, locale).forEach(property -> json.put(property.getName(), property.get()));
        FileManager.write(json, SETTINGS_FILEPATH, false);
    }

    /**
     * Load the settings from the settings file.
     */
    public static void load() {
        initAutoSave();

        JSONObject json = FileManager.readFile(SETTINGS_FILEPATH, false);
        if (json == null) return;

        if (json.has(fullScreen.getName())) fullScreen.set(json.getBoolean(fullScreen.getName()));
        if (json.has(showFps.getName())) showFps.set(json.getBoolean(showFps.getName()));
        if (json.has(locale.getName())) locale.set(LocaleUtils.toLocale(json.getString(locale.getName())));
        else locale.set(Locale.getDefault());
    }

    /**
     * Initialize the auto save.
     * Adds a shutdown hook to save the settings when the program exits.
     */
    private static void initAutoSave() {
        if (autoSaveInitialized) return;
        Runtime.getRuntime().addShutdownHook(new Thread(Settings::save));
        autoSaveInitialized = true;
    }
}
