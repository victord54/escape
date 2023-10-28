package fr.ul.acl.escape;

import java.beans.PropertyChangeSupport;
import java.util.Locale;

import static fr.ul.acl.escape.outils.Donnees.DEBUG;

public class Settings {
    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(Settings.class);

    public static final Property<Boolean> fullScreen = new Property<>(pcs, "fullScreen", false).setLog(DEBUG);
    public static final Property<Boolean> showFps = new Property<>(pcs, "showFps", false).setLog(DEBUG);
    public static final Property<Locale> locale = new Property<>(pcs, "locale", Locale.getDefault()).setLog(DEBUG);

    public static void forceApply() {
        fullScreen.forceFire();
        showFps.forceFire();
        locale.forceFire();
    }
}
