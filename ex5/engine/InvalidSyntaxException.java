package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when the syntax is invalid.
 */
public class InvalidSyntaxException extends JavaSCompilationException {
    private static final String INVALID_SYNTAX_MESSAGE = "Invalid syntax";

    /**
     * Constructs a new InvalidSyntaxException with the specified line.
     * @param line The line where the invalid syntax was found.
     */
    public InvalidSyntaxException(int line) {
        super("In line " + line + ": " + INVALID_SYNTAX_MESSAGE);
    }
}
