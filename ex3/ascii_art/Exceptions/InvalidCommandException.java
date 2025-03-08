package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid command.
 */
public class InvalidCommandException extends InputException {
    /**
     * Constructs an InvalidCommandException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
