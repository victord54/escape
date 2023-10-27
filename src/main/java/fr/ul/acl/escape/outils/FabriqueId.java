package fr.ul.acl.escape.outils;

public class FabriqueId {
    private static FabriqueId instance;
    private int id;

    private FabriqueId() {
        id = 0;
    }

    public static FabriqueId getInstance() {
        if (instance == null) instance = new FabriqueId();
        return instance;
    }

    public int getId() {
        id++;
        return id;
    }
}
