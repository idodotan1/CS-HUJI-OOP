package ascii_art.Exceptions;

/**
 * An exception that is thrown when the character set to be used is invalid.
 */
public class InvalidCharSetException extends InputException {
    /**
     * Constructs an InvalidCharSetException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidCharSetException(String message) {
        super(message);
    }

}
