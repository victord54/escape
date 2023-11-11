package fr.ul.acl.escape;

import fr.ul.acl.escape.outils.Donnees;
import org.json.JSONObject;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;

public class Settings {
    /**
     * Path to the settings file.
     */
    private static final String SETTINGS_FILE_PATH = Donnees.APPDATA_FOLDER + File.separatorChar + "settings.json";
    /**
     * Whether the auto save has been initialized.
     */
    private static boolean autoSaveInitialized = false;
    /**
     * Common {@link PropertyChangeSupport} instance.
     */
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(Settings.class);

    public static final Property<Boolean> fullScreen = new Property<>(pcs, "fullScreen", false).setLog(DEBUG);
    public static final Property<Boolean> showFps = new Property<>(pcs, "showFps", false).setLog(DEBUG);
    public static final Property<Locale> locale = new Property<>(pcs, "locale", Locale.getDefault()).setLog(DEBUG);

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
        if (!ensureSettingsFileExists()) return;

        JSONObject json = new JSONObject();
        Arrays.asList(fullScreen, showFps, locale).forEach(property -> json.put(property.getName(), property.get()));

        try (FileWriter writer = new FileWriter(SETTINGS_FILE_PATH)) {
            writer.write(json.toString(4));
        } catch (Exception e) {
            System.err.println("Could not save settings");
        }
    }

    /**
     * Load the settings from the settings file.
     */
    public static void load() {
        if (!ensureSettingsFileExists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(SETTINGS_FILE_PATH)));
            JSONObject json = new JSONObject(content);

            if (json.has(fullScreen.getName())) fullScreen.set(json.getBoolean(fullScreen.getName()));
            if (json.has(showFps.getName())) showFps.set(json.getBoolean(showFps.getName()));
            if (json.has(locale.getName())) locale.set(new Locale(json.getString(locale.getName())));
        } catch (Exception e) {
            System.err.println("Could not load settings");
        }

        initAutoSave();
    }

    /**
     * Ensure that the settings file exists.
     *
     * @return Whether the settings file exists.
     */
    private static boolean ensureSettingsFileExists() {
        String path = Donnees.APPDATA_FOLDER;
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Could not create app data folder");
                return false;
            }
        }

        if (new File(SETTINGS_FILE_PATH).exists()) return true;

        try (FileWriter writer = new FileWriter(SETTINGS_FILE_PATH)) {
            writer.write(new JSONObject().toString(4));
        } catch (Exception e) {
            System.err.println("Could not create settings file");
            return false;
        }

        return true;
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
