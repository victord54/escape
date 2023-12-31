package fr.ul.acl.escape.outils;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static fr.ul.acl.escape.outils.FileManager.FileType.ENCRYPTED;
import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

/**
 * Utility class to read and write JSON files from the app data folder or the resources.
 */
public class FileManager {
    /**
     * Delete the file at the given path in the app data folder.
     *
     * @param path path to delete, relative to the app data folder, use {@link File#separator} as a separator
     * @return whether the file was successfully deleted
     */
    public static boolean delete(String path) {
        String fullPath = getPathFor(path);
        if (fullPath == null) return false;

        File file = new File(fullPath);
        if (!file.exists()) {
            return false;
        }

        return file.delete();
    }

    /**
     * Write the given JSON object to the given path in the app data folder.
     *
     * @param json        JSON object to write
     * @param path        path to write to, relative to the app data folder, use {@link File#separator} as a separator
     * @param isEncrypted whether the file should be encrypted
     * @return whether the file was successfully written
     */
    public static boolean write(JSONObject json, String path, boolean isEncrypted) {
        if (!isEncrypted) {
            return writeString(json.toString(4), path);
        }

        SecretKey key = getSecretKey();
        if (key == null) return false;

        String content = json.toString();
        byte[] encrypted;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            ErrorBehavior.handle(e, "Could not encrypt content");
            return false;
        }

        String encryptedContent = Base64.getEncoder().encodeToString(encrypted);
        return writeString(encryptedContent, path);
    }

    /**
     * Read the JSON object from the given path in the app data folder.
     *
     * @param path        path to read from, relative to the app data folder, use {@link File#separator} as a separator
     * @param isEncrypted whether the file is encrypted
     * @return the JSON object read, or null if the file could not be read
     */
    public static JSONObject readFile(String path, boolean isEncrypted) {
        String content = readString(path);

        if (isEncrypted) {
            SecretKey key = getSecretKey();
            if (key == null) return null;

            byte[] decrypted;
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                decrypted = cipher.doFinal(Base64.getDecoder().decode(content));
            } catch (Exception e) {
                ErrorBehavior.handle(e, "Could not decrypt content");
                return null;
            }
            content = new String(decrypted, StandardCharsets.UTF_8);
        }

        return readJSONString(content);
    }

    /**
     * Read the string from the given path in the app data folder.
     *
     * @param path path to read from, relative to the app data folder, use {@link File#separator} as a separator
     * @return the string red, or null if the file could not be read
     */
    private static String readString(String path) {
        String fullPath = getPathFor(path);
        if (fullPath == null) return null;

        if (!new File(fullPath).exists()) {
            return null;
        }

        try {
            return new String(Files.readAllBytes(Paths.get(fullPath)));
        } catch (Exception e) {
            ErrorBehavior.handle(e, "Could not read '" + path + "' file");
            return null;
        }
    }

    /**
     * Read all the JSON files from the given path in the app data folder.
     * JSON files should have the {@link FileType#JSON} extension.
     * Encrypted files should have the {@link FileType#ENCRYPTED} extension.
     *
     * @param folder            path to read from, relative to the app data folder, use {@link File#separator} as a separator
     * @param filesAreEncrypted whether the files to read are encrypted
     * @return a map of the files read, with the path as key and the JSON object as value
     */
    public static Map<String, JSONObject> readDirectory(String folder, boolean filesAreEncrypted) {
        String fullPath = getPathFor(folder);
        if (fullPath == null) return new HashMap<>();

        File dir = new File(fullPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return new HashMap<>();
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return new HashMap<>();
        }

        Map<String, JSONObject> map = new HashMap<>();
        Arrays.stream(files).forEach(file -> {
            if (file == null || !file.getName().toLowerCase().endsWith((filesAreEncrypted ? ENCRYPTED : JSON).extension) || !file.isFile()) {
                return;
            }

            String filePath = folder + File.separator + file.getName();
            JSONObject json = readFile(filePath, filesAreEncrypted);
            if (json == null) {
                System.err.println("Could not read '" + filePath + "' file");
                return;
            }
            map.put(filePath, json);
        });

        return map;
    }

    /**
     * Read the JSON object from the given path in the 'resources' folder.
     *
     * @param path path to read from, relative to the 'resources' folder, use / as a separator
     * @return the JSON object read, or null if the file could not be read
     */
    public static JSONObject readResourceFile(String path) {
        try {
            InputStream stream = Resources.getAsStream(path);
            String content = new String(stream.readAllBytes());
            return new JSONObject(content);
        } catch (Exception e) {
            ErrorBehavior.handle(e, "Could not read '" + path + "' file from resources");
            return null;
        }
    }

    /**
     * Write the given string to the given path in the app data folder.
     *
     * @param content string to write
     * @param path    path to write to, relative to the app data folder, use {@link File#separator} as a separator
     * @return whether the file was successfully written
     */
    private static boolean writeString(String content, String path) {
        String fullPath = getPathFor(path);
        if (fullPath == null) return false;

        File parent = new File(fullPath).getParentFile();
        if (!parent.exists() || !parent.isDirectory()) {
            if (!parent.mkdirs()) {
                System.err.println("Could not create '" + parent.getAbsolutePath() + "'");
                return false;
            }
        }

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(content);
        } catch (Exception e) {
            ErrorBehavior.handle(e, "Could not write '" + path + "' file");
            return false;
        }

        return true;
    }

    /**
     * Convert the given string to a JSON object.
     *
     * @param json string to convert
     * @return the JSON object, or null if the string could not be converted
     */
    private static JSONObject readJSONString(String json) {
        if (json == null) return null;

        try {
            return new JSONObject(json);
        } catch (Exception e) {
            ErrorBehavior.handle(e, "Could not parse to JSON");
            return null;
        }
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
     * Derive the encryption key from the environment variable ENCRYPTION_KEY to create a {@link SecretKey}.
     *
     * @return the {@link SecretKey} created, or null if the key could not be created
     */
    private static SecretKey getSecretKey() {
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("ENCRYPTION_KEY");

        if (key == null) {
            System.out.println("[Warning] No encryption key found");
            return null;
        }

        byte[] keyHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            keyHash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            ErrorBehavior.handle(e, "Could not create SHA-256 digest");
            return null;
        }

        return new SecretKeySpec(keyHash, "AES");
    }

    /**
     * Open the given path in the app data folder.
     * If the file/folder does not exist, it will be created.
     *
     * @param path     path to open, relative to the app data folder, use {@link File#separator} as a separator
     *                 if null and isFolder is true, open the app data folder
     * @param isFolder whether the path is a folder
     */
    public static void open(String path, boolean isFolder) {
        if (!Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported");
            return;
        }
        if (path == null && !isFolder) return;

        // get the full path
        String fullPath = path == null ? Donnees.APPDATA_FOLDER : getPathFor(path);
        if (fullPath == null) return;

        // ensure the file/folder exists (create it if it does not)
        File file = new File(fullPath);
        File parent = file.getParentFile();
        if (!parent.exists() || !parent.isDirectory()) {
            if (!parent.mkdirs()) {
                System.err.println("Could not create '" + parent.getAbsolutePath() + "'");
                return;
            }
        }

        if (isFolder) {
            if (!file.exists() || !file.isDirectory()) {
                if (!file.mkdirs()) {
                    System.err.println("Could not create '" + file.getAbsolutePath() + "'");
                    return;
                }
            }
        } else {
            if (!file.exists() || !file.isFile()) {
                try {
                    if (!file.createNewFile()) {
                        System.err.println("Could not create '" + file.getAbsolutePath() + "'");
                        return;
                    }
                } catch (IOException e) {
                    ErrorBehavior.handle(e, "Could not create '" + file.getAbsolutePath() + "'");
                    return;
                }
            }
        }

        // open the file/folder
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            ErrorBehavior.handle(e, "Could not open '" + file.getAbsolutePath() + "'");
        }
    }

    /**
     * Get the file for the given path in the app data folder.
     *
     * @param path path to get, relative to the app data folder, use {@link File#separator} as a separator
     * @return the file, or null if the path is null or empty
     */
    public static File getFile(String path) {
        String fullPath = getPathFor(path);
        if (fullPath == null) return null;
        return new File(fullPath);
    }

    /**
     * Available file types.
     * Get the extension with {@link FileType#extension}.
     */
    public enum FileType {
        JSON(".json"), ENCRYPTED(".enc");

        public final String extension;

        FileType(String extension) {
            this.extension = extension;
        }
    }
}
