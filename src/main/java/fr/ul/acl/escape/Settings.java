package fr.ul.acl.escape;

import java.beans.PropertyChangeSupport;
import java.util.Locale;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;

public class Settings {
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(Settings.class);

    public static final Property<Boolean> FULL_SCREEN = new Property<>(pcs, "FULL_SCREEN", false).setLog(DEBUG);
    public static final Property<Boolean> SHOW_FPS = new Property<>(pcs, "SHOW_FPS", false).setLog(DEBUG);
    public static final Property<Locale> LOCALE = new Property<>(pcs, "LOCALE", Locale.getDefault()).setLog(DEBUG);

    public static void forceApply() {
        FULL_SCREEN.forceFire();
        SHOW_FPS.forceFire();
        LOCALE.forceFire();
    }
}
