package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingsView extends View {
    public SettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/settings-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

        ((SettingsViewController) controller).setFullScreenCheckBox(Settings.fullScreen.get());

        ComboBox<String> languageComboBox = ((SettingsViewController) controller).getLanguageComboBox();
        Map<String, Locale> availableLangs = Donnees.SUPPORTED_LOCALES.stream().map(locale -> {
            String lang = Resources.getI18NString("language", locale);
            return Map.entry(lang, locale);
        }).collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);

        languageComboBox.onActionProperty().set(null);
        languageComboBox.getItems().setAll(availableLangs.keySet());
        languageComboBox.getSelectionModel().select(Resources.getI18NString("language", Settings.locale.get()));
        languageComboBox.onActionProperty().set((evt) -> {
            String lang = languageComboBox.getSelectionModel().getSelectedItem();
            Settings.locale.set(availableLangs.get(lang));
        });
    }
}