package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when an if/while statement or method call is made in global scope
 */
public class InvalidActionInGlobalScopeException extends JavaSCompilationException {
    private static final String INVALID_SCOPE_MESSAGE = "If or while statement or method call or return in "+
                                                        "global scope is not allowed";

    /**
     * Constructs a new InvalidActionInGlobalScopeException with the given line number
     * @param line the line number
     */
    public InvalidActionInGlobalScopeException(int line) {
        super("In line " + line + ": " + INVALID_SCOPE_MESSAGE);
    }
}

