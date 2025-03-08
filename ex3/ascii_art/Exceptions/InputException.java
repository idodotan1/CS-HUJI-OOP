package ascii_art.Exceptions;

/**
 * An exception that is thrown when the input is invalid.
 */
public abstract class InputException extends Exception {
    /**
     * Constructs an InputException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InputException(String message) {
        super(message);
    }
}
