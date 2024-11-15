package org.com.battleship.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller class for handling scene transitions.
 * This class listens for key events to start the game and switch scenes.
 */
public class SceneController {

    /**
     * Logger for logging events and errors.
     */
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Starts the game when any key is pressed from the main menu.
     *
     * @param keyEvent the KeyEvent triggered by the user's input.
     */
    @FXML
    public void startGame(KeyEvent keyEvent) {
        try {
            Stage stage = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow(); // Get the current stage
            GameController gameController = new GameController(stage);
            Scene scene = new Scene(gameController.createBoard()); // Create a new scene with the game board layout
            stage.setScene(scene); // Set the new scene to the stage
            stage.setResizable(false); // Make the stage non-resizable to maintain consistent UI layout
            stage.show(); // Display the new scene
            logger.info("Starting game"); // Log that the game is starting
        } catch (Exception e) {
            logger.error("Unable to start game", e); // Log an error if the game fails to start
        }
    }
}
