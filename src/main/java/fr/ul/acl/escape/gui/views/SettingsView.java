package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SettingsView extends View {
    public SettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/settings-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onViewInit() {
        ((SettingsViewController) controller).setFullScreenCheckBox(Settings.fullScreen);
    }
}
