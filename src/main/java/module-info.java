module escape {
    requires javafx.controls;
    requires javafx.fxml;

    opens fr.ul.acl.escape to javafx.fxml;
    exports fr.ul.acl.escape;
    exports fr.ul.acl.escape.ui;
    opens fr.ul.acl.escape.ui.views to javafx.fxml;
    exports fr.ul.acl.escape.ui.views;
}