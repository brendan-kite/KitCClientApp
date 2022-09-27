module com.example.kitcclientapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.mail;
    requires jsch;

    opens com.example.kitcclientapp to javafx.fxml;
    exports com.example.kitcclientapp;
}