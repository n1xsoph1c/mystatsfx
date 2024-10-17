module com.ghostcompany.mystats {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jdi;
    requires java.sql;
    requires atlantafx.base;

    opens com.ghostcompany.mystats to javafx.fxml;
    opens com.ghostcompany.mystats.Controller to javafx.fxml;
    exports com.ghostcompany.mystats;
}