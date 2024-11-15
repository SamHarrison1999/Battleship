package org.com.battleship.exceptions;

/**
 * Custom exception class for handling errors during the initialization of the game board.
 * <p>
 * This exception is a subclass of {@link RuntimeException}, meaning it is unchecked and
 * can be thrown at runtime without being explicitly declared in a method's `throws` clause.
 * </p>
 *
 * Example usage:
 * <pre>
 * throw new BoardInitializationException("Error initializing the board", cause);
 * </pre>
 *
 * @see RuntimeException
 */
public class BoardInitializationException extends RuntimeException {

    /**
     * Constructs a new {@link BoardInitializationException} with the specified detail message
     * and cause of the exception.
     *
     * @param message the detail message that provides more context about the exception.
     *                This message is accessible via the {@link Throwable#getMessage()} method.
     * @param cause   the cause of the exception, which can be retrieved using {@link Throwable#getCause()}.
     *                A {@code null} value indicates that the cause is unknown or nonexistent.
     */
    public BoardInitializationException(String message, Throwable cause) {
        super(message, cause); // Calls the constructor of the superclass (RuntimeException)
    }
}
