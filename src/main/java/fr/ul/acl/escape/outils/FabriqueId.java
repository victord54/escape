package fr.ul.acl.escape.outils;

public class FabriqueId {
    private static FabriqueId instance;

    private FabriqueId() {

    }

    public static FabriqueId getInstance() {
        return instance;
    }
}
