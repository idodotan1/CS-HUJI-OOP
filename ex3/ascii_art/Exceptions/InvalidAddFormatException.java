package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid add format.
 */
public class InvalidAddFormatException extends InputException {
    /**
     * Constructs an InvalidAddFormatException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidAddFormatException(String message) {
        super(message);
    }
}
