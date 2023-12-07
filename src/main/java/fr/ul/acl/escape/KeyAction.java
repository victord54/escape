package fr.ul.acl.escape;


import javafx.scene.input.KeyCode;

public enum KeyAction {
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

    KeyAction(KeyCode code, String inputId) {
        this.defaultKeyCode = code;
        this.inputId = inputId;
        this.debugOnly = false;
    }

    KeyAction(KeyCode code, String inputId, boolean debugOnly) {
        this.defaultKeyCode = code;
        this.inputId = inputId;
        this.debugOnly = debugOnly;
    }

    public KeyCode getDefaultKeyCode() {
        return defaultKeyCode;
    }

    /**
     * @return the id of the corresponding input in the settings view
     */
    public String getInputId() {
        return inputId;
    }

    /**
     * @return true if the key should only be used in debug mode
     */
    public boolean isDebugOnly() {
        return debugOnly;
    }

    /**
     * Get the KeyActions corresponding to the given input id
     *
     * @param inputId the id of the input in the settings view
     * @return the corresponding KeyActions
     */
    public static KeyAction fromInputId(String inputId) {
        for (KeyAction key : values()) {
            if (key.getInputId().equals(inputId)) {
                return key;
            }
        }
        return null;
    }
}
