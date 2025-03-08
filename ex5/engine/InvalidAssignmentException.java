package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when an invalid assignment is made.
 */
public class InvalidAssignmentException extends JavaSCompilationException {
    private static final String INVALID_ASSIGNMENT_MESSAGE = "Invalid assignment";

    /**
     * Constructs a new InvalidAssignmentException with the specified line.
     * @param line The line where the invalid assignment was made.
     */
    public InvalidAssignmentException(int line) {
        super("In line " + line + ": " + INVALID_ASSIGNMENT_MESSAGE);
    }
}
