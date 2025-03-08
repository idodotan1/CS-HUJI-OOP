package ex5.engine;

import ex5.main.JavaSCompilationException;

/**
 * This exception is thrown when a boolean expression is invalid.
 */
public class InvalidBooleanExpressionException extends JavaSCompilationException {
  private static final String INVALID_BOOLEAN_MESSAGE = "Invalid boolean expression";

  /**
   * Constructs a new InvalidBooleanExpressionException with the specified line.
   * @param line The line where the invalid boolean expression was made.
   */
  public InvalidBooleanExpressionException(int line) {
    super("In line " + line + ": " + INVALID_BOOLEAN_MESSAGE);
  }
}
