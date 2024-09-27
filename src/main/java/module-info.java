module com.guiyomi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.guiyomi to com.google.gson, javafx.fxml;
    
    exports com.guiyomi;
}
