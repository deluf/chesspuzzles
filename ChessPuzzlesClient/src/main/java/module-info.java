module it.unipit.chesspuzzlesclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires org.apache.logging.log4j;

    exports it.unipi.chesspuzzles.client;
    opens it.unipi.chesspuzzles.client to com.google.gson, javafx.fxml;

    exports it.unipi.chesspuzzles.client.controllers;
    opens it.unipi.chesspuzzles.client.controllers to javafx.fxml;

    exports it.unipi.chesspuzzles.client.shared;
    opens it.unipi.chesspuzzles.client.shared to com.google.gson;
}
