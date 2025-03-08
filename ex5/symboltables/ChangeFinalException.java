package ex5.symboltables;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when trying to change a final variable value.
 */
public class ChangeFinalException extends JavaSCompilationException {
    private static final String CHANGE_FINAL_MESSAGE = "Can't change a final variable value";

    /**
     * Constructs a new ChangeFinalException with the specified line.
     * @param line The line where the final variable was tried to be changed.
     */
    public ChangeFinalException(int line) {
        super("In line " + line + ": " + CHANGE_FINAL_MESSAGE);
    }
}

