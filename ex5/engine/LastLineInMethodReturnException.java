package ex5.engine;
import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when the last line in a method is not a return statement.
 */
public class LastLineInMethodReturnException extends JavaSCompilationException {
    private static final String RETURN_STATEMENT_MISSING_MESSAGE = "Last line in method must be a " +
                                                                   "return statement";

    /**
     * Constructs a new LastLineInMethodReturnException with the specified line.
     * @param line The line where the last line in a method is not a return statement.
     */
    public LastLineInMethodReturnException(int line) {
        super("In line " + line + ": " + RETURN_STATEMENT_MISSING_MESSAGE);
    }
}
