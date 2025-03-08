package ex5.symboltables;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown referring to a variable name that is not found.
 */
public class VariableNotFoundException extends JavaSCompilationException {
    private static final String VARIABLE_NOT_FOUND_MESSAGE = "Variable not found";

    /**
     * Constructs a new VariableNotFoundException with the specified line.
     * @param line The line where the variable name was not found.
     */
    public VariableNotFoundException(int line) {
        super("In line " + line + ": " + VARIABLE_NOT_FOUND_MESSAGE);
    }
}
