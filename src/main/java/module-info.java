module dunbar.c195pa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens dunbar.c195pa.Main to javafx.fxml;
    exports dunbar.c195pa.Main;
    exports dunbar.c195pa.Controller;
    opens dunbar.c195pa.Controller to javafx.fxml;
    opens dunbar.c195pa.Model to javafx.base;
}