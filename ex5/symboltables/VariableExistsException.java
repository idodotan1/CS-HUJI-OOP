package ex5.symboltables;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when declaring a variable with a name that already exists.
 */
public class VariableExistsException extends JavaSCompilationException {
    private static final String VARIABLE_EXISTS_MESSAGE = "Variable already exists";

    /**
     * Constructs a new VariableExistsException with the specified line.
     * @param line The line where the variable already exists.
     */
    public VariableExistsException(int line) {
        super("In line " + line + ": " + VARIABLE_EXISTS_MESSAGE);
    }
}
