module com.example.escape {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.escape to javafx.fxml;
    exports com.example.escape;
}