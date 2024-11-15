package org.com.battleship.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.battleship.exceptions.BoardInitializationException;
import org.com.battleship.exceptions.ShipPlacementException;

/**
 * Represents the game board.
 * This class is responsible for handling ship placement, validating placements,
 * retrieving neighbouring cells, and resetting the board.
 */
public class Board extends Parent {

    /** Logger instance for logging game events and errors. */
    protected static final Logger logger = LogManager.getLogger();

    private final VBox rows = new VBox();  // Holds rows of cells (10x10 grid)
    private final boolean enemy;           // Indicates if this board belongs to the enemy
    private int ships;                     // Counter for the number of ships on the board

    /**
     * Private constructor to create a Board instance using the Builder pattern.
     *
     * @param builder the Builder instance used for setting board properties
     */
    private Board(Builder builder) {
        this.enemy = builder.enemy;
        this.ships = builder.ships;
        initializeGrid(builder.handler);
    }

    /**
     * Initializes the 10x10 grid with Cell instances and attaches event handlers.
     *
     * @param handler the event handler for mouse click events
     */
    private void initializeGrid(EventHandler<? super MouseEvent> handler) {
        logger.info("Initializing the grid.");
        try {
            for (int y = 0; y < 10; y++) {
                HBox row = new HBox();  // Each row containing 10 cells
                for (int x = 0; x < 10; x++) {
                    Cell cell = new Cell.Builder().setX(x).setY(y).setBoard(this).build();
                    if (handler != null) cell.setOnMouseClicked(handler);  // Attach event handler if provided
                    row.getChildren().add(cell);  // Add cell to the row
                }
                rows.getChildren().add(row);  // Add row to the VBox
            }
            getChildren().add(rows);  // Add rows to the parent container
            logger.info("Grid initialized successfully.");
        } catch (Exception e) {
            logger.error("Error initializing the grid.", e);
            throw new BoardInitializationException("Failed to initialize the grid.", e);
        }
    }

    /**
     * Returns the number of ships currently placed on the board.
     *
     * @return the number of ships
     */
    public int getShips() {
        return ships;
    }

    /**
     * Sets the number of ships on the board.
     *
     * @param ships the new number of ships
     */
    public void setShips(int ships) {
        this.ships = ships;
    }

    /**
     * Retrieves the cell located at the specified (x, y) coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the Cell at the specified coordinates
     */
    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    /**
     * Provides access to all cells on the board as a 2D array.
     *
     * @return a 2D array representing the cells on the board
     */
    public Cell[][] getCells() {
        Cell[][] cells = new Cell[10][10];
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                cells[y][x] = getCell(x, y);
            }
        }
        return cells;
    }

    /**
     * Places a ship on the board at the specified coordinates if the placement is valid.
     *
     * @param ship the Ship to place
     * @param x    the starting x-coordinate
     * @param y    the starting y-coordinate
     * @return {@code true} if the ship was successfully placed, {@code false} otherwise
     */
    public boolean placeShip(Ship ship, int x, int y) {
        logger.info("Attempting to place ship at ({}, {}).", x, y);
        if (!canPlaceShip(ship, x, y)) {
            logger.warn("Invalid ship placement at ({}, {}).", x, y);
            return false;
        }

        try {
            // Place the ship by iterating through each cell in the specified direction
            int length = ship.getType();
            IntStream.range(0, length).forEach(i -> {
                int targetX = x + (ship.isVertical() ? 0 : i);
                int targetY = y + (ship.isVertical() ? i : 0);
                Cell cell = getCell(targetX, targetY);
                cell.setShip(ship);  // Assign the ship to each cell
                if (!enemy) {  // Adjust visuals if this is the player's board
                    cell.setFill(Color.WHITE);
                    cell.setStroke(Color.GREEN);
                }
            });
            logger.info("Ship placed successfully at ({}, {}).", x, y);
            return true;
        } catch (Exception e) {
            logger.error("Error placing ship at ({}, {}).", x, y, e);
            throw new ShipPlacementException("Error placing ship.", e);
        }
    }

    /**
     * Determines if a ship can be placed at the specified coordinates.
     * Ensures that the ship is within bounds and does not overlap with other ships.
     *
     * @param ship the Ship to place
     * @param x    the starting x-coordinate
     * @param y    the starting y-coordinate
     * @return {@code true} if the placement is valid, {@code false} otherwise
     */
    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.getType();
        int dx = ship.isVertical() ? 0 : 1;
        int dy = ship.isVertical() ? 1 : 0;

        return IntStream.range(0, length).allMatch(i -> {
            int targetX = x + i * dx;
            int targetY = y + i * dy;
            if (!isValidPoint(targetX, targetY)) return false;  // Check board bounds
            if (getCell(targetX, targetY).getShip() != null) return false;  // Check if cell is occupied
            return isSafeFromNeighbors(targetX, targetY);  // Check neighbouring cells
        });
    }

    /**
     * Verifies that all neighbouring cells of a target cell are unoccupied.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return {@code true} if all neighbouring cells are empty, {@code false} otherwise
     */
    private boolean isSafeFromNeighbors(int x, int y) {
        return getNeighbors(x, y).stream().allMatch(neighbor -> neighbor.getShip() == null);
    }

    /**
     * Retrieves the neighbouring cells.
     *
     * @param x the x-coordinate of the target cell
     * @param y the y-coordinate of the target cell
     * @return a list of neighbouring Cell objects
     */
    private List<Cell> getNeighbors(int x, int y) {
        Point2D[] points = {
                new Point2D(x - 1.0, y), new Point2D(x + 1.0, y),
                new Point2D(x, y - 1.0), new Point2D(x, y + 1.0)
        };

        List<Cell> neighbors = new ArrayList<>();
        for (Point2D point : points) {
            if (isValidPoint(point)) {
                neighbors.add(getCell((int) point.getX(), (int) point.getY()));
            }
        }
        return neighbors;
    }

    /**
     * Checks if a given point is within the board's bounds.
     *
     * @param point the Point2D to validate
     * @return {@code true} if the point is within bounds, {@code false} otherwise
     */
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    /**
     * Checks if a given (x, y) coordinate is within the board's bounds.
     *
     * @param x the x-coordinate to validate
     * @param y the y-coordinate to validate
     * @return {@code true} if the coordinates are within bounds, {@code false} otherwise
     */
    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    /**
     * Resets the board.
     */
    public void clear() {
        rows.getChildren().forEach(row -> ((HBox) row).getChildren().forEach(cell -> {
            ((Cell) cell).setShip(null);  // Remove any ship from the cell
            ((Cell) cell).setFill(Color.LIGHTBLUE);  // Reset to default color
            ((Cell) cell).setStroke(Color.BLACK);    // Reset stroke colour
        }));
        this.ships = 0;  // Reset ship count
    }

    /**
     * Builder class for constructing Board instances.
     * This allows for flexible configuration of the board's properties.
     */
    public static class Builder {
        private boolean enemy = false;
        private int ships = 5;
        private EventHandler<? super MouseEvent> handler;

        public Builder setEnemy(boolean enemy) {
            this.enemy = enemy;
            return this;
        }

        public Builder setShips(int ships) {
            this.ships = ships;
            return this;
        }

        public Builder setHandler(EventHandler<? super MouseEvent> handler) {
            this.handler = handler;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
