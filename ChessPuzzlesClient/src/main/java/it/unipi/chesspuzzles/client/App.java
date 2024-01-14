package it.unipi.chesspuzzles.client;

import it.unipi.chesspuzzles.client.shared.Puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private static Scene scene;

    // Used to pass the userId and the username from the login window to the others
    private static int userId;
    private static String username;

    // Used to pass the selected puzzle from the favourites window to the play window
    private static Puzzle favouritePuzzle;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        App.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        App.username = username;
    }

    public static Puzzle getFavouritePuzzle() {
        return favouritePuzzle;
    }

    public static void setFavouritePuzzle(Puzzle favouritePuzzle) {
        App.favouritePuzzle = favouritePuzzle;
    }

    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            logger.fatal("Unable to load {}.fxml -> {}", fxml, e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws IOException {

        Font.loadFont(Objects.requireNonNull(getClass()
                .getResource("fonts/Montserrat-SemiBold.ttf")).toExternalForm(), 15);

        scene = new Scene(loadFXML("authentication"), 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("stylesheet.css")).toExternalForm());

        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("media/icon.png")).toExternalForm()));
        stage.setTitle("ChessPuzzles");
        stage.setScene(scene);
        stage.show();
    }

}