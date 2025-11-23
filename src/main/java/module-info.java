module com.example.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;


    opens com.example.bankingsystem to javafx.fxml;
    exports com.example.bankingsystem;
}