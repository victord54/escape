package fr.ul.acl.escape;


import javafx.scene.input.KeyCode;

public enum KeyActions {
    UP(KeyCode.Z, "keyUp"),
    LEFT(KeyCode.Q, "keyLeft"),
    DOWN(KeyCode.S, "keyDown"),
    RIGHT(KeyCode.D, "keyRight"),
    TAKE(KeyCode.E, "keyTake"),
    ATTACK(KeyCode.SPACE, "keyAttack"),
    PAUSE(KeyCode.ESCAPE, "keyPause"),
    SHOW_FPS(KeyCode.F, "keyShowFps"),
    GRID(KeyCode.G, "keyShowGrid", true);

    private final KeyCode defaultKeyCode;
    private final String inputId;
    private final boolean debugOnly;

    KeyActions(KeyCode code, String inputId) {
        this.defaultKeyCode = code;
        this.inputId = inputId;
        this.debugOnly = false;
    }

    KeyActions(KeyCode code, String inputId, boolean debugOnly) {
        this.defaultKeyCode = code;
        this.inputId = inputId;
        this.debugOnly = debugOnly;
    }

    public KeyCode getDefaultKeyCode() {
        return defaultKeyCode;
    }

    public String getInputId() {
        return inputId;
    }

    public boolean isDebugOnly() {
        return debugOnly;
    }

    public static KeyActions fromInputId(String inputId) {
        for (KeyActions key : values()) {
            if (key.getInputId().equals(inputId)) {
                return key;
            }
        }
        return null;
    }
}
