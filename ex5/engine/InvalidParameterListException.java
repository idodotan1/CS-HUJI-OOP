package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when the parameter list of a function is invalid.
 */
public class InvalidParameterListException extends JavaSCompilationException {
    private static final String INVALID_PARAMETER_LIST_MESSAGE = "Invalid parameter list";

    /**
     * Constructs a new InvalidParameterListException with the specified line.
     * @param line The line where the invalid parameter list was found.
     */
    public InvalidParameterListException(int line) {
        super("In line " + line + ": " + INVALID_PARAMETER_LIST_MESSAGE);
    }
}
