module com.ghostcompany.mystats {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.ghostcompany.mystats to javafx.fxml;
    exports com.ghostcompany.mystats;
}