package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when a function is declared inside a function.
 */
public class InvalidScopeFunctionDeclarationException extends JavaSCompilationException {
    private static final String INVALID_FUNCTION_DECLARATION_MESSAGE = "Invalid function declaration inside" +
                                                                       " a function";
    /**
     * Constructs a new InvalidScopeFunctionDeclaration with the specified line.
     * @param line The line where the invalid function declaration was made.
     */
    public InvalidScopeFunctionDeclarationException(int line) {
        super("In line " + line + ": " + INVALID_FUNCTION_DECLARATION_MESSAGE);
    }
}
