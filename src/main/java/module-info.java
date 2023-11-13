module escape {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jgrapht.core;
    requires java.desktop;
    requires net.harawata.appdirs;
    requires org.json;
    requires spring.core;
    requires io.github.cdimascio.dotenv.java;

    opens fr.ul.acl.escape.gui.views to javafx.fxml;

    exports fr.ul.acl.escape;
    exports fr.ul.acl.escape.engine;
    exports fr.ul.acl.escape.cli;
    exports fr.ul.acl.escape.gui;
    exports fr.ul.acl.escape.gui.engine;
    exports fr.ul.acl.escape.gui.views;
    exports fr.ul.acl.escape.monde;
}
