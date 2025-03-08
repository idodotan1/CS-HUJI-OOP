package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid remove format.
 */
public class InvalidRemoveFormatException extends InputException {
    /**
     * Constructs an InvalidRemoveFormatException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidRemoveFormatException(String message) {
        super(message);
    }
}
