module escape {
    requires javafx.controls;
    requires javafx.fxml;

    opens fr.ul.acl.escape to javafx.fxml;
    exports fr.ul.acl.escape;
    exports fr.ul.acl.escape.gui;
    opens fr.ul.acl.escape.gui.views to javafx.fxml;
    exports fr.ul.acl.escape.gui.views;
}
