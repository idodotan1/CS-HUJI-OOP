package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid output format.
 */
public class InvalidOutputFormatException extends InputException {
    /**
     * Constructs an InvalidOutputFormatException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidOutputFormatException(String message) {
        super(message);
    }
}
