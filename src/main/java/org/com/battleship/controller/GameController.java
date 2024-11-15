package org.com.battleship.controller;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.battleship.model.Board;
import org.com.battleship.model.Cell;
import org.com.battleship.model.Ship;

import java.util.*;

/**
 * Controller class for managing the game logic and board interaction.
 * This class creates the game board, places ships, handles turns,
 * provides enemy AI, and manages game state.
 */
public class GameController {

    /** Logger instance for logging game events and errors. */
    protected static final Logger logger = LogManager.getLogger();

    /** Turn indicators. */
    public static final String ENEMY_TURN = "Enemy's Turn";
    public static final String YOUR_TURN = "Your Turn";

    /** Number of ships to be placed by the player. */
    private int shipsToPlace = 5;

    /** Flag indicating whether the game is currently running. */
    private boolean running = false;

    /** The enemy's game board. */
    private Board enemyBoard;

    /** The player's game board. */
    private Board playerBoard;

    /** Flag indicating if it is the enemy's turn. */
    private boolean enemyTurn = false;

    /** Random number generator used for AI's moves, ship placement and ship orientation. */
    private final Random random = new Random();

    /** Set of cells that have been shot to avoid repeated shots. */
    private final Set<Cell> alreadyShotCells = new HashSet<>();

    /** Queue of cells to target in AI's hunt-and-kill mode when a ship is hit. */
    private final Queue<Cell> huntQueue = new LinkedList<>();

    /** List to track cells of ships that have been hit but not sunk. */
    private final List<Cell> hitCells = new ArrayList<>();

    /** Label to indicate the current turn (player or enemy). */
    private Label turnIndicator;

    /** Reference to the primary stage to display game end screens. */
    private Stage primaryStage;

    /** Flag to determine if the game is paused. */
    private boolean isPaused = false;

    /**
     * Constructor that initializes the game controller with the primary stage.
     *
     * @param stage the primary stage used for showing the game end screen.
     */
    public GameController(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Creates the game board for both players
     * and sets up event handlers for player interactions.
     *
     * @return the root Parent container with the game board layout.
     */
    public Parent createBoard() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        // Sidebar setup for turn indicator, pause, and restart buttons
        VBox sidebar = new VBox(10);
        sidebar.setAlignment(Pos.CENTER);

        // Label to show whose turn it is (player or enemy)
        turnIndicator = new Label(YOUR_TURN);
        sidebar.getChildren().add(turnIndicator);

        // Button to pause and resume the game
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> togglePause());
        sidebar.getChildren().add(pauseButton);

        // Button to restart the game
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());
        sidebar.getChildren().add(restartButton);

        root.setRight(sidebar);

        // Initialize the enemy board with an event handler for gameplay
        enemyBoard = new Board.Builder().setEnemy(true).setHandler(event -> {
            if (!running || enemyTurn || isPaused) return;

            Cell cell = (Cell) event.getSource();
            if (cell.getWasShot()) return;

            enemyTurn = !cell.shoot();  // switch to the computer's turn if a ship was not hit
            updateTurnIndicator(); // Update the turn indicator

            // Check if the player has won
            if (enemyBoard.getShips() == 0) {
                endGame("You Win!");
            } else if (enemyTurn) {
                enemyMove();
            }
        }).build();

        // Initialize player board with an event handler for gameplay
        playerBoard = new Board.Builder().setEnemy(false).setHandler(event -> {
            if (running || isPaused) return;

            Cell cell = (Cell) event.getSource();
            // Place player's ship
            if (playerBoard.placeShip(new Ship.Builder().type(shipsToPlace)
                    .vertical(event.getButton() == MouseButton.PRIMARY).health().build(), cell.x, cell.y)
                    && --shipsToPlace == 0) {
                startGame(); // Start the game once all ships are placed
            }
        }).build();

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);

        return root;
    }

    /**
     * Begins the game by placing enemy ships randomly and setting the game to running.
     */
    private void startGame() {
        placeEnemyShipsRandomly();
        running = true;
        turnIndicator.setText(YOUR_TURN);
    }

    /**
     * Places enemy ships randomly on the board at the start of the game.
     */
    private void placeEnemyShipsRandomly() {
        int type = 5;
        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            // Attempt to place a ship on a random cell
            if (enemyBoard.placeShip(new Ship.Builder().type(type).vertical(Math.random() < 0.5).health().build(), x, y)) {
                type--;
            }
        }
    }

    /**
     * Executes the enemy's move using a hunt-and-kill strategy to target player ships.
     */
    private void enemyMove() {
        turnIndicator.setText(ENEMY_TURN);

        while (enemyTurn) {
            if (huntQueue.isEmpty()) {
                fireRandomShot();  // Hunt mode: firing randomly until a hit is made
            } else {
                fireAtAdjacentCells(); // Kill mode: firing at adjacent cells
            }

            if (playerBoard.getShips() == 0) {
                endGame("You Lose!");
            }
        }

        updateTurnIndicator();
    }

    /**
     * Fires a random shot at an un-shot cell on the player's board.
     */
    private void fireRandomShot() {
        Cell randomCell = getRandomCell();
        if (!alreadyShotCells.contains(randomCell)) {
            enemyTurn = randomCell.shoot();
            if (enemyTurn && !randomCell.getWasShot()) {
                hitCells.add(randomCell);
                huntQueue.add(randomCell);
            }
        }
    }

    /**
     * Fires at cells adjacent to a hit cell, aiming to sink a detected ship.
     */
    private void fireAtAdjacentCells() {
        Cell targetCell = huntQueue.poll();
        List<Cell> adjacentCells = getAdjacentCells(Objects.requireNonNull(targetCell));

        for (Cell adjCell : adjacentCells) {
            if (!alreadyShotCells.contains(adjCell)) {
                enemyTurn = adjCell.shoot();
                if (enemyTurn) {
                    hitCells.add(adjCell);
                    huntQueue.add(adjCell);
                    break;
                }
            }
        }
    }

    /**
     * Retrieves a random un-shot cell from the player's board.
     *
     * @return a randomly selected cell that has not been shot at.
     */
    private Cell getRandomCell() {
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        return playerBoard.getCell(x, y);
    }

    /**
     * Returns a list of cells adjacent to the given cell on the board.
     *
     * @param cell the cell for which adjacent cells are required.
     * @return a list of cells adjacent to the given cell.
     */
    private List<Cell> getAdjacentCells(Cell cell) {
        List<Cell> adjacentCells = new ArrayList<>();
        int x = (int) cell.getX();
        int y = (int) cell.getY();

        // Ensure adjacent cells are within board bounds
        if (x > 0) adjacentCells.add(playerBoard.getCell(x - 1, y));
        if (x < 9) adjacentCells.add(playerBoard.getCell(x + 1, y));
        if (y > 0) adjacentCells.add(playerBoard.getCell(x, y - 1));
        if (y < 9) adjacentCells.add(playerBoard.getCell(x, y + 1));

        return adjacentCells;
    }

    /**
     * Updates the turn indicator label based on whose turn it is.
     */
    private void updateTurnIndicator() {
        turnIndicator.setText(enemyTurn ? ENEMY_TURN : YOUR_TURN);
    }

    /**
     * Ends the game and displays the end screen.
     *
     * @param result Message indicating the game result (win or lose).
     */
    private void endGame(String result) {
        running = false;
        logger.info(result);
        StackPane endScreen = new StackPane();
        endScreen.setPrefSize(400, 200);
        Label resultLabel = new Label(result);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());
        Button restartButton = new Button("Play Again");
        restartButton.setOnAction(e -> restartGame());

        VBox layout = new VBox(20, resultLabel, restartButton, closeButton);
        layout.setAlignment(Pos.CENTER);
        endScreen.getChildren().add(layout);

        Scene endScene = new Scene(endScreen);
        primaryStage.setScene(endScene);
    }

    /**
     * Toggles the game's paused state and updates the turn indicator accordingly.
     */
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            turnIndicator.setText("Game Paused");
            logger.info("Game Paused");
        }
        else {
            turnIndicator.setText(enemyTurn ? ENEMY_TURN : YOUR_TURN);
            logger.info("Game Resumed");
        }
    }

    /**
     * Restarts the game by resetting all game state variables, boards, and the turn indicator.
     */
    private void restartGame() {
        logger.info("Restarting Game");
        shipsToPlace = 5;
        running = false;
        enemyTurn = false;
        isPaused = false;
        alreadyShotCells.clear();
        huntQueue.clear();
        hitCells.clear();

        // Clear boards and reset turn indicator
        enemyBoard.clear();
        playerBoard.clear();
        turnIndicator.setText(YOUR_TURN);

        // Reset the scene to the main game board
        primaryStage.setScene(new Scene(createBoard()));
        logger.info("Game Restarted");
    }
}
