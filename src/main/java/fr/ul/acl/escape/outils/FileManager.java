package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.Escape;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static fr.ul.acl.escape.outils.FileManager.FileType.ENCRYPTED;
import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;

/**
 * Utility class to read and write JSON files from the app data folder or the resources.
 */
public class FileManager {
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
            System.err.println("Could not encrypt content");
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
     * @return the JSON object read, or an empty object if the file could not be read
     */
    public static JSONObject readFile(String path, boolean isEncrypted) {
        String content = readString(path);

        if (isEncrypted) {
            SecretKey key = getSecretKey();
            if (key == null) return new JSONObject();

            byte[] decrypted;
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                decrypted = cipher.doFinal(Base64.getDecoder().decode(content));
            } catch (GeneralSecurityException e) {
                System.err.println("Could not decrypt content");
                return new JSONObject();
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
        String fullpath = getPathFor(path);
        if (fullpath == null) return null;

        if (!new File(fullpath).exists()) {
            return null;
        }

        try {
            return new String(Files.readAllBytes(Paths.get(fullpath)));
        } catch (Exception e) {
            System.err.println("Could not read '" + path + "' file");
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
     * @return the list of JSON objects read, or an empty list if the folder could not be read
     */
    public static List<JSONObject> readDirectory(String folder, boolean filesAreEncrypted) {
        String fullpath = getPathFor(folder);
        if (fullpath == null) return new ArrayList<>();

        File dir = new File(fullpath);
        if (!dir.exists() || !dir.isDirectory()) {
            return new ArrayList<>();
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(files)
                .filter(file -> file.getName().toLowerCase().endsWith((filesAreEncrypted ? ENCRYPTED : JSON).extension) && file.isFile())
                .map(file -> readFile(folder + File.separator + file.getName(), filesAreEncrypted))
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
    // FIXME: is this method really useful? if not, remove dependency to Spring
    public static List<JSONObject> readResourceDirectory(String folder) {
        ClassLoader cl = Escape.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);

        Resource[] resources;
        try {
            resources = resolver.getResources("classpath:" + Resources.getPackagePath() + "/" + folder + "/**/*.json");
        } catch (IOException e) {
            System.err.println("Could not read '" + folder + "' folder from resources");
            return new ArrayList<>();
        }

        return Arrays.stream(resources).map(resource -> readResourceFile(folder + "/" + resource.getFilename())).toList();
    }

    /**
     * Write the given string to the given path in the app data folder.
     *
     * @param content string to write
     * @param path    path to write to, relative to the app data folder, use {@link File#separator} as a separator
     * @return whether the file was successfully written
     */
    private static boolean writeString(String content, String path) {
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
            writer.write(content);
        } catch (Exception e) {
            System.err.println("Could not save '" + path + "' file");
            return false;
        }

        return true;
    }

    /**
     * Convert the given string to a JSON object.
     *
     * @param json string to convert
     * @return the JSON object, or an empty object if the string could not be converted
     */
    private static JSONObject readJSONString(String json) {
        if (json == null) return new JSONObject();

        try {
            return new JSONObject(json);
        } catch (Exception e) {
            System.err.println("Could not parse to JSON");
            return new JSONObject();
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
            System.err.println("Could not create SHA-256 digest");
            return null;
        }

        return new SecretKeySpec(keyHash, "AES");
    }

    /**
     * Available file types.
     * Get the extension with {@link FileType#extension}.
     */
    public enum FileType {
        JSON(".json"),
        ENCRYPTED(".enc");

        public final String extension;

        FileType(String extension) {
            this.extension = extension;
        }
    }
}
