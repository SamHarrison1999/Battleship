package org.com.battleship.exceptions;

/**
 * Custom exception class to handle errors encountered during ship placement on the game board.
 * <p>
 * This exception extends {@link RuntimeException}, meaning it is unchecked and does not need
 * to be declared in a method's `throws` clause. It is typically used when ship placement
 * constraints are violated or unexpected conditions occur.
 * </p>
 *
 * Example usage:
 * <pre>
 * throw new ShipPlacementException("Invalid ship placement at coordinates", cause);
 * </pre>
 *
 * @see RuntimeException
 */
public class ShipPlacementException extends RuntimeException {

    /**
     * Constructs a new {@link ShipPlacementException} with the specified detail message
     * and cause of the exception.
     *
     * @param message the detail message that provides additional context about the exception.
     *                This can be accessed later using {@link Throwable#getMessage()}.
     * @param cause   the cause of the exception, which can be another throwable that led to this exception.
     *                If {@code null}, the cause is considered nonexistent or unknown.
     */
    public ShipPlacementException(String message, Throwable cause) {
        super(message, cause); // Call the parent constructor to initialize the exception
    }
}
