package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid rounding command.
 */
public class InvalidRoundingException extends InputException {
    /**
     * Constructs an InvalidRoundingException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidRoundingException(String message) {
        super(message);
    }
}
