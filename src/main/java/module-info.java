module com.hms.hosteler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires javax.mail.api;

    opens com.hms.hosteler to javafx.fxml;
    exports com.hms.hosteler;
}

