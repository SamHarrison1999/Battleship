package org.com.battleship.model;

import javafx.scene.Parent;

/**
 * Represents a ship in the game.
 * Each ship has a type (size), orientation (vertical or horizontal), and health.
 * A ship is considered alive as long as it has health remaining.
 */
public class Ship extends Parent {

    private final int type;     // The type of the ship that is determined by its size
    private final boolean vertical; // Whether the ship is placed vertically
    private int health;         // The health of the ship

    /**
     * Private constructor to create a Ship instance using the Builder pattern.
     *
     * @param builder the Builder instance used for setting board properties
     */
    private Ship(Builder builder) {
        this.type = builder.type;
        this.vertical = builder.vertical;
        this.health = builder.health;
    }

    /**
     * Reduces the health of the ship by 1 when it is hit.
     * If the ship is already destroyed, the health won't be reduced.
     */
    public void hit() {
        if (isAlive()) {
            health--;
        }
    }

    /**
     * Checks if the ship is still alive.
     *
     * @return {@code true} if the ship is alive (health > 0), {@code false} otherwise
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Gets the type of the ship.
     *
     * @return the type (size) of the ship
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the health of the ship.
     *
     * @return the health of the ship (equal to the ship's size)
     */
    public int getHealth() {
        return health;
    }

    /**
     * Checks if the ship is placed vertically on the board.
     *
     * @return {@code true} if the ship is vertical, {@code false} if it is horizontal
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Returns a string representation of the ship, including its type, orientation, and health.
     *
     * @return a string representation of the ship
     */
    @Override
    public String toString() {
        return "Ship{" +
                "type=" + type +
                ", vertical=" + vertical +
                ", health=" + health +
                '}';
    }

    /**
     * Builder class for constructing {@link Ship} instances.
     * This allows for flexible configuration of ship attributes before building the ship object.
     */
    public static class Builder {

        private int type;          // Ship type (size)
        private boolean vertical;  // Whether the ship is vertical or horizontal
        private int health;        // Health is based on ship type (size)

        /**
         * Sets the type (size) of the ship.
         *
         * @param type the type (size) of the ship
         * @return the Builder instance for chaining
         */
        public Builder type(int type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the orientation of the ship (vertical or horizontal).
         *
         * @param vertical {@code true} for vertical placement, {@code false} for horizontal placement
         * @return the Builder instance for chaining
         */
        public Builder vertical(boolean vertical) {
            this.vertical = vertical;
            return this;
        }

        /**
         * Sets the health of the ship. By default, health is equal to the ship's size (type).
         *
         * @return the Builder instance for chaining
         */
        public Builder health() {
            this.health = this.type;
            return this;
        }

        /**
         * Builds and returns a new {@link Ship} instance configured with the values set in the Builder.
         *
         * @return a new {@link Ship} instance
         */
        public Ship build() {
            return new Ship(this);
        }
    }
}
