package fr.ul.acl.escape.outils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class to read and write JSON files from the app data folder or the resources.
 */
public class FileManager {
    /**
     * Write the given JSON object to the given path in the app data folder.
     *
     * @param json JSON object to write
     * @param path path to write to, relative to the app data folder, use {@link File#separator} as a separator
     * @return whether the file was successfully written
     */
    public static boolean write(JSONObject json, String path) {
        String fullpath = getPathFor(path);
        if (fullpath == null) return false;

        File parent = new File(fullpath).getParentFile();
        if (!parent.exists() || !parent.isDirectory()) {
            if (!parent.mkdirs()) {
                System.err.println("Could not create '" + parent.getAbsolutePath() + "'");
                return false;
            }
        }

        try (FileWriter writer = new FileWriter(fullpath)) {
            writer.write(json.toString(4));
        } catch (Exception e) {
            System.err.println("Could not save '" + path + "' file");
            return false;
        }

        return true;
    }

    /**
     * Read the JSON object from the given path in the app data folder.
     *
     * @param path path to read from, relative to the app data folder, use {@link File#separator} as a separator
     * @return the JSON object read, or an empty object if the file could not be read
     */
    public static JSONObject readFile(String path) {
        String fullpath = getPathFor(path);
        if (fullpath == null) return new JSONObject();

        if (!new File(fullpath).exists()) {
            return new JSONObject();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(fullpath)));
            return new JSONObject(content);
        } catch (Exception e) {
            System.err.println("Could not read '" + path + "' file");
            return new JSONObject();
        }
    }

    /**
     * Read all the JSON files from the given path in the app data folder.
     *
     * @param folder path to read from, relative to the app data folder, use {@link File#separator} as a separator
     * @return the list of JSON objects read, or an empty list if the folder could not be read
     */
    public static List<JSONObject> readDirectory(String folder) {
        String fullpath = getPathFor(folder);
        if (fullpath == null) return new ArrayList<>();

        return getJSONFiles(new File(fullpath))
                .map(file -> readFile(folder + File.separator + file.getName()))
                .toList();
    }

    /**
     * Read the JSON object from the given path in the 'resources' folder.
     *
     * @param path path to read from, relative to the 'resources' folder, use / as a separator
     * @return the JSON object read, or an empty object if the file could not be read
     */
    public static JSONObject readResourceFile(String path) {
        try {
            InputStream stream = Resources.getAsStream(path);
            String content = new String(stream.readAllBytes());
            return new JSONObject(content);
        } catch (Exception e) {
            System.err.println("Could not read '" + path + "' file from resources");
            return new JSONObject();
        }
    }

    /**
     * Read all the JSON files from the given path in the 'resources' folder.
     *
     * @param folder path to read from, relative to the 'resources' folder, use / as a separator
     * @return the list of JSON objects read, or an empty list if the folder could not be read
     */
    public static List<JSONObject> readResourceDirectory(String folder) {
        URL url = Resources.get(folder);
        if (url == null) return new ArrayList<>();

        return getJSONFiles(new File(url.getFile()))
                .map(file -> readResourceFile(folder + "/" + file.getName()))
                .toList();
    }

    /**
     * Get the full path for the given path in the app data folder.
     *
     * @param path relative path, use {@link File#separator} as a separator
     * @return the full path, or null if the path is null or empty
     */
    private static String getPathFor(String path) {
        if (path == null || path.isEmpty()) return null;
        return Donnees.APPDATA_FOLDER + File.separator + path;
    }

    /**
     * Get the JSON files from the given directory.
     *
     * @param dir directory to read from
     * @return the stream of JSON files
     */
    private static Stream<File> getJSONFiles(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return Stream.empty();
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return Stream.empty();
        }
        return Arrays.stream(files)
                .filter(file -> file.getName().toLowerCase().endsWith(".json") && file.isFile());
    }
}
