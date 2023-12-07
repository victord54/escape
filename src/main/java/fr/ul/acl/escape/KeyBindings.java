package fr.ul.acl.escape;

import fr.ul.acl.escape.outils.Donnees;
import javafx.scene.input.KeyCode;
import org.json.JSONObject;

import java.util.*;

public class KeyBindings {
    private final Map<KeyAction, KeyCode> keys = new HashMap<>() {{
        putAll(Arrays.stream(KeyAction.values()).collect(
                HashMap::new,
                (map, key) -> map.put(key, key.getDefaultKeyCode()),
                HashMap::putAll));
    }};

    public KeyBindings() {
    }

    public KeyBindings(JSONObject keys) throws IllegalArgumentException {
        keys.keySet().forEach(key -> {
            KeyAction actionKey = KeyAction.valueOf(key);
            if (this.keys.containsKey(actionKey)) {
                this.keys.put(actionKey, KeyCode.valueOf(keys.getString(key)));
            }
        });
    }

    public KeyCode getKey(String inputId) {
        return getKey(KeyAction.fromInputId(inputId));
    }

    public KeyCode getKey(KeyAction key) {
        return keys.get(key);
    }

    public void setKey(String inputId, KeyCode code) {
        setKey(KeyAction.fromInputId(inputId), code);
    }

    public void setKey(KeyAction key, KeyCode code) throws IllegalArgumentException {
        if (keys.containsKey(key)) {
            keys.put(key, code);
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        keys.forEach((key, code) -> json.put(key.name(), code.name()));
        return json;
    }

    public List<KeyAction> getConflictingKeys() {
        List<KeyAction> conflictingKeys = new ArrayList<>();
        keys.forEach((keyAction, code) -> {
            if (keyAction.isDebugOnly() && !Donnees.DEBUG) return;
            if (keys.keySet().stream().anyMatch(keyAction2 -> (!keyAction2.isDebugOnly() || Donnees.DEBUG) && !keyAction2.equals(keyAction) && keys.get(keyAction2).equals(code))) {
                conflictingKeys.add(keyAction);
            }
        });
        return conflictingKeys;
    }
}
