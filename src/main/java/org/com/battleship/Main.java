package org.com.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class of the Battleship game application.
 * This class extends {@link Application} and is responsible for setting up
 * and displaying the initial user interface (UI) for the game.
 */
public class Main extends Application {

    /** Logger instance for logging game events and errors. */
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Loads the main menu when the application starts.
     * It loads the FXML file for the main menu, sets the scene, and displays it in a non-resizable window.
     * If there is an issue loading the FXML, an error is logged.
     *
     * @param stage the primary stage for the application
     */
    @Override
    public void start(Stage stage) {
        try {
            // Load the main menu screen when the application starts using FXMLLoader
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("main_menu.fxml")));

            // Create a new scene with the loaded root element
            Scene scene = new Scene(root);

            // Set the scene for the stage
            stage.setScene(scene);

            // Make the window non-resizable
            stage.setResizable(false);

            // Show the stage
            stage.show();

            // Request focus on the root element of the scene to ensure proper user interaction
            scene.getRoot().requestFocus();
        } catch(Exception e) {
            // Log error if unable to load the main menu due to an issue with the FXML file
            logger.error("Unable to load main menu", e);
        }
    }

    /**
     * The main method which serves as the entry point for launching the JavaFX application.
     * It calls the {@link Application#launch(String...)} method to start the application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        // Launch the JavaFX application with the provided command-line arguments
        launch(args);
    }
}
