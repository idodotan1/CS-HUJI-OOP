package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when the scopes closed doesn't match the scopes opened.
 */
public class InvalidScopeClosingException extends JavaSCompilationException {
    private static final String INVALID_SCOPE_MESSAGE = "The scopes closed doesnt match the scopes opened";

    /**
     * Constructs a new InvalidScopeClosingException.
     */
    public InvalidScopeClosingException() {
        super(INVALID_SCOPE_MESSAGE);
    }
}
