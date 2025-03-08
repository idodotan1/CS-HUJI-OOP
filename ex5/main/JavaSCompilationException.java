package ex5.main;

/**
 * This is abstract base class for javaS compilation errors.
 */
public abstract class JavaSCompilationException extends Exception {

    /**
     * Constructs a new JavaSCompilationException with the specified message.
     * @param message The message of the exception.
     */
    public JavaSCompilationException(String message) {
        super(message);
    }
}
