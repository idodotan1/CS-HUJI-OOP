package ascii_art.Exceptions;

/**
 * An exception that is thrown when the user inputs an invalid resolution.
 */
public class InvalidResolutionException extends InputException {
    /**
     * Constructs an InvalidResolutionException with the specified message.
     * @param message The message to be displayed when the exception is thrown.
     */
    public InvalidResolutionException(String message) {
        super(message);
    }
}
