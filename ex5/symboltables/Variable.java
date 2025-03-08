package ex5.symboltables;

/**
 * Represents a variable in the symbol table.
 */
public class Variable {
    private static final String NULL_VALUE = "null";
    private final String variableName;
    private final Types type;
    private String value;
    private final boolean isFinal;

    /**
     * Constructs a new Variable.
     * @param variableName The name of the variable.
     * @param type The type of the variable.
     * @param value The value of the variable.
     * @param isFinal True if the variable is final, false otherwise.
     */
    public Variable(String variableName, Types type, String value, boolean isFinal) {
        this.variableName = variableName;
        this.type = type;
        this.value = value;
        this.isFinal = isFinal;
    }

    /**
     * Constructs a new Variable with a null value.
     * @param variableName The name of the variable.
     * @param type The type of the variable.
     * @param isFinal True if the variable is final, false otherwise.
     */
    public Variable(String variableName, Types type, boolean isFinal) {
        this.variableName = variableName;
        this.type = type;
        this.value = NULL_VALUE;
        this.isFinal = isFinal;
    }

    /**
     * Gets the name of the variable.
     * @return The name of the variable.
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Gets the type of the variable.
     * @return The type of the variable.
     */
    public Types getType() {
        return type;
    }

    /**
     * Checks if the variable is final.
     * @return True if the variable is final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Gets the value of the variable.
     * @return The value of the variable.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the variable.
     * @param value The new value of the variable.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
