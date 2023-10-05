package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Escape;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    /**
     * Loaded assets.
     */
    private static final Map<String, Image> assets = new HashMap<>();

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
}
