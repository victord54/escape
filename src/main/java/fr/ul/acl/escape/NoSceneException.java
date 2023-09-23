package fr.ul.acl.escape;

public class NoSceneException extends RuntimeException {
    public NoSceneException() {
        super("No scene set in view manager");
    }
}
