package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Escape;

import java.net.URL;

public class Resources {
    /**
     * Get a resource from the 'resources/fr/ul/acl/escape' directory.
     *
     * @param path The path of the resource.
     * @return The URL of the resource.
     */
    public static URL get(String path) {
        return Escape.class.getResource(path);
    }
}
