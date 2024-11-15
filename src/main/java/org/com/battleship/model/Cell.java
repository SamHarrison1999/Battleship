package org.com.battleship.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a single cell on the board.
 * Each cell may contain a ship and can be shot during the game.
 * The cell maintains its position, state (whether it has been shot), and appearance.
 */
public class Cell extends Rectangle {

    /** Logger instance for logging game events and errors. */
    protected static final Logger logger = LogManager.getLogger();

    public final int x; // The x-coordinate of the cell on the board.
    public final int y; // The y-coordinate of the cell on the board.

    private Ship ship; // The ship placed in this cell, if any.
    private boolean wasShot; // Tracks whether this cell has been shot at.

    private final Board board; // The board to which this cell belongs.

    /**
     * Retrieves the ship placed in the cell if there is one.
     *
     * @return the {@link Ship} in this cell, or {@code null} if no ship is placed
     */
    public Ship getShip() {
        return ship; // Returns the ship occupying the cell, or null if no ship is present.
    }

    /**
     * Sets a ship in the cell.
     *
     * @param ship the {@link Ship} to place in the cell
     */
    public void setShip(Ship ship) {
        this.ship = ship; // Puts the ship in the cell.
    }

    /**
     * Constructor for creating a Cell instance.
     * Initializes the rectangle representing the cell using the builder pattern.
     *
     * @param builder the builder that provides configuration for this cell
     */
    private Cell(Builder builder) {
        super(30, 30); // Set the default size for the cell (30x30 pixels)
        this.x = builder.x; // Set the x-coordinate of the cell.
        this.y = builder.y; // Set the y-coordinate of the cell.
        this.board = builder.board; // Set the board the cell belongs to.
        this.ship = builder.ship; // Set the ship (if any) that is in the cell.
        this.wasShot = builder.wasShot; // Set whether this cell has been shot at.
        setFill(builder.fill); // Set the background fill color of the cell.
        setStroke(builder.stroke); // Set the border colour of the cell.
    }

    /**
     * Shoots at the cell, changing its colour to indicate whether a ship was hit or not.
     * If a ship is present, it is marked as hit, and the ship's health is reduced.
     *
     * @return {@code true} if the shot hits a ship, {@code false} otherwise
     */
    public boolean shoot() {
        wasShot = true; // Mark the cell shot.
        setFill(Color.BLACK); // Set the colour to black to indicate the cell was shot.

        if (ship != null) {
            ship.hit(); // Mark the ship as hit.
            setFill(Color.RED); // Change the cell colour to red to indicate a hit.
            if (!ship.isAlive()) {
                board.setShips(board.getShips() - 1); // Decrease remaining ships if the ship is sunk.
            }
            logger.info("Ship Hit");
            return true; // Return true if the shot hit a ship.
        }
        logger.info("No ship hit");
        return false; // Return false if the shot did not hit a ship.
    }

    /**
     * Checks if the cell has already been shot at.
     *
     * @return {@code true} if the cell has been shot, {@code false} otherwise
     */
    public boolean getWasShot() {
        return wasShot; // Returns whether the cell has been shot at.
    }

    /**
     * Sets whether the cell has been shot at.
     *
     * @param wasShot {@code true} if the cell has been shot at, {@code false} otherwise
     */
    public void setWasShot(boolean wasShot) {
        this.wasShot = wasShot; // Sets the shot status of the cell.
    }

    /**
     * Builder class for constructing {@link Cell} instances.
     * This allows for flexible configuration of the cell's properties before building the actual cell.
     */
    public static class Builder {
        private int x; // The x-coordinate of the cell.
        private int y; // The y-coordinate of the cell.
        private Board board; // The board to which the cell belongs.
        private Ship ship = null;  // Default is no ship in the cell.
        private boolean wasShot = false; // Default is that the cell hasn't been shot.
        private Color fill = Color.LIGHTGRAY; // Default fill colour for the cell.
        private Color stroke = Color.BLACK;  // Default stroke colour for the cell.

        /**
         * Sets the x-coordinate of the cell on the board.
         *
         * @param x the x-coordinate of the cell
         * @return the Builder instance for chaining
         */
        public Builder setX(int x) {
            this.x = x; // Set the x-coordinate of the cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets the y-coordinate of the cell on the board.
         *
         * @param y the y-coordinate of the cell
         * @return the Builder instance for chaining
         */
        public Builder setY(int y) {
            this.y = y; // Set the y-coordinate of the cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets the board that this cell belongs to.
         *
         * @param board the {@link Board} this cell is part of
         * @return the Builder instance for chaining
         */
        public Builder setBoard(Board board) {
            this.board = board; // Set the board this cell belongs to.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets the ship to be placed in this cell.
         *
         * @param ship the {@link Ship} to place in the cell
         * @return the Builder instance for chaining
         */
        public Builder setShip(Ship ship) {
            this.ship = ship; // Set the ship to be placed in this cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets whether the cell has been shot at.
         *
         * @param wasShot {@code true} if the cell has been shot at, {@code false} otherwise
         * @return the Builder instance for chaining
         */
        public Builder setWasShot(boolean wasShot) {
            this.wasShot = wasShot; // Set the shot status of the cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets the background colour of the cell.
         *
         * @param fill the fill colour for the cell
         * @return the Builder instance for chaining
         */
        public Builder setFill(Color fill) {
            this.fill = fill; // Set the background colour of the cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Sets the boarder colour of the cell.
         *
         * @param stroke the stroke colour for the cell
         * @return the Builder instance for chaining
         */
        public Builder setStroke(Color stroke) {
            this.stroke = stroke; // Set the border colour of the cell.
            return this; // Return the builder for method chaining.
        }

        /**
         * Builds and returns a new {@link Cell} instance based on the configured values.
         *
         * @return the newly constructed {@link Cell} instance
         */
        public Cell build() {
            return new Cell(this); // Create and return a new Cell instance using the configured values.
        }
    }
}
