package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Escape;
import fr.ul.acl.escape.Settings;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class Resources {
    /**
     * Loaded assets.
     */
    private static final Map<String, Image> assets = new HashMap<>();

    private Resources() {
    }

    /**
     * Get a stream from a resource from the 'resources/fr/ul/acl/escape' directory.
     *
     * @param path The path of the resource.
     * @return The stream of the resource.
     */
    public static InputStream getAsStream(String path) {
        return Escape.class.getResourceAsStream(path);
    }

    /**
     * Get an asset from the 'resources/fr/ul/acl/escape' directory.
     * The asset is loaded only once.
     * Image will be null if the program is launched in CLI mode.
     *
     * @param path The path of the asset.
     * @return The asset (or null if it failed to load).
     */
    public static Image getAsset(String path) {
        if (!assets.containsKey(path)) {
            InputStream is = getAsStream(path);
            if (Donnees.CLI_MODE || is == null) {
                assets.put(path, null);
                if (Donnees.DEBUG && is == null) System.out.println("Failed to load asset: " + path);
            } else {
                assets.put(path, new Image(is));
                if (Donnees.DEBUG) System.out.println("Loaded asset: " + path);
            }
        }
        return assets.get(path);
    }

    /**
     * Get the internationalization (i18n) bundle.
     *
     * @param locale The locale of the bundle.
     * @return The internationalization bundle.
     * @see Resources#getI18NBundle()
     */
    public static ResourceBundle getI18NBundle(Locale locale) {
        if (locale == null) return ResourceBundle.getBundle(getPackagePath() + "/i18n/strings");
        return ResourceBundle.getBundle(getPackagePath() + "/i18n/strings", locale);
    }

    /**
     * Get the internationalization (i18n) bundle.
     *
     * @return The internationalization bundle.
     * @see Resources#getI18NBundle(Locale)
     */
    public static ResourceBundle getI18NBundle() {
        return getI18NBundle(Settings.locale.get());
    }

    /**
     * Get a string from the internationalization (i18n) bundle.
     *
     * @param key    The key of the string.
     * @param locale The locale of the string.
     * @return The string.
     * @see Resources#getI18NString(String)
     */
    public static String getI18NString(String key, Locale locale) {
        return getI18NBundle(locale).getString(key);
    }

    /**
     * Get a string from the internationalization (i18n) bundle.
     *
     * @param key The key of the string.
     * @return The string.
     * @see Resources#getI18NString(String, Locale)
     */
    public static String getI18NString(String key) {
        return getI18NBundle().getString(key);
    }

    /**
     * @return The relative path of resources folder.
     */
    public static String getPackagePath() {
        return Escape.class.getPackage().getName().replace('.', '/');
    }
}
