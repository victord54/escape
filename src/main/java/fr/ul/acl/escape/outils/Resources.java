package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Escape;
import fr.ul.acl.escape.Settings;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
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
     * Get a resource from the 'resources/fr/ul/acl/escape' directory.
     *
     * @param path The path of the resource.
     * @return The URL of the resource.
     */
    public static URL get(String path) {
        return Escape.class.getResource(path);
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
     *
     * @param path The path of the asset.
     * @return The asset.
     */
    public static Image getAsset(String path) {
        if (!assets.containsKey(path)) {
            assets.put(path, new Image(get(path).toString()));
            System.out.println("Loaded asset: " + path);
        }
        return assets.get(path);
    }

    /**
     * Get the internationalization (i18n) bundle.
     *
     * @return The internationalization bundle.
     */
    public static ResourceBundle getI18NBundle() {
        return ResourceBundle.getBundle(Escape.class.getPackage().getName().replace('.', '/') + "/i18n/strings", Settings.locale);
    }

    /**
     * Get a string from the internationalization (i18n) bundle.
     *
     * @param key The key of the string.
     * @return The string.
     */
    public static String getI18NString(String key) {
        return getI18NBundle().getString(key);
    }
}
