module com.example.masroofy {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.masroofy.View to javafx.fxml;
    exports com.example.masroofy;
}