package ex5.symboltables;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when declaring a function with a name that already exists.
 */
public class FunctionExistsException extends JavaSCompilationException {
    private static final String FUNCTION_EXISTS_MESSAGE = "Function already exists";

    /**
     * Constructs a new FunctionExistsException with the specified line.
     * @param line The line where the function already exists.
     */
    public FunctionExistsException(int line) {
        super("In line " + line + ": " + FUNCTION_EXISTS_MESSAGE);
    }
}
