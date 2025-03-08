package ex5.symboltables;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when referring to a function name that is not found.
 */
public class FunctionNotFoundException extends JavaSCompilationException {
    private static final String FUNCTION_NOT_FOUND_MESSAGE = "Function not found";

    /**
     * Constructs a new FunctionNotFoundException with the specified line.
     * @param line The line where the function was not found.
     */
    public FunctionNotFoundException(int line) {
        super("In line " + line + ": " + FUNCTION_NOT_FOUND_MESSAGE);
    }
}
