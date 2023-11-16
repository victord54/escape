module escape {
    requires io.github.cdimascio.dotenv.java;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires org.jgrapht.core;
    requires org.json;
    requires net.harawata.appdirs;
    requires spring.core;

    opens fr.ul.acl.escape.gui.views to javafx.fxml;

    exports fr.ul.acl.escape;
    exports fr.ul.acl.escape.engine;
    exports fr.ul.acl.escape.cli;
    exports fr.ul.acl.escape.gui;
    exports fr.ul.acl.escape.gui.engine;
    exports fr.ul.acl.escape.gui.views;
    exports fr.ul.acl.escape.monde;
    exports fr.ul.acl.escape.monde.entities;
    exports fr.ul.acl.escape.monde.environment;
}
