package fr.ul.acl.escape.gui.views;

import fr.ul.acl.escape.Property;
import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.gui.View;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SettingsView extends View {
    private boolean comboBoxPreventEvent = false;

    private final Property.MyPropertyChangeListener<Boolean> fullScreenListener = (evt, oldValue, newValue) -> {
        ((SettingsViewController) controller).setFullScreenCheckBox(newValue);
    };
    private final Property.MyPropertyChangeListener<Locale> localeListener = (evt, oldValue, newValue) -> {
        ComboBox<String> languageComboBox = ((SettingsViewController) controller).getLanguageComboBox();
        comboBoxPreventEvent = true;
        languageComboBox.getSelectionModel().select(newValue.getDisplayName(newValue));
        comboBoxPreventEvent = false;
    };

    public SettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.get("gui/settings-view.fxml"));
        loader.setResources(Resources.getI18NBundle());
        this.root = loader.load();
        this.controller = loader.getController();
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        SettingsViewController controller = (SettingsViewController) this.controller;

        // set the full screen check box
        controller.setFullScreenCheckBox(Settings.fullScreen.get());

        // populate the language combo box
        ComboBox<String> languageComboBox = controller.getLanguageComboBox();
        languageComboBox.onActionProperty().set(null);
        List<Locale> supportedLocales = Donnees.SUPPORTED_LOCALES;
        languageComboBox.getItems().setAll(supportedLocales.stream().map(locale -> locale.getDisplayName(locale)).toArray(String[]::new));

        // select the current locale
        Locale currentLocale = Settings.locale.get();
        languageComboBox.getSelectionModel().select(currentLocale.getDisplayName(currentLocale));

        // update the locale when the user selects a new one
        languageComboBox.onActionProperty().set((evt) -> {
            if (comboBoxPreventEvent) return;
            String selectedLocaleName = languageComboBox.getSelectionModel().getSelectedItem();
            Settings.locale.set(supportedLocales.stream()
                    .filter((locale) -> locale.getDisplayName(locale).equals(selectedLocaleName))
                    .findFirst()
                    .orElse(Locale.getDefault()));
        });

        // update properties when they change from outside the view
        Settings.fullScreen.subscribeIfNot(fullScreenListener);
        Settings.locale.subscribeIfNot(localeListener);
    }

    @Override
    public void onViewDisplayed() {
        super.onViewDisplayed();
        ((SettingsViewController) controller).resetFocus();
    }
}
