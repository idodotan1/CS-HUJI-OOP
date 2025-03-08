package ex5.symboltables;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the function symbol table.
 */
public class FunctionSymbolTable {
    HashMap<String, ArrayList<Variable>> functions;

    /**
     * Constructs a new FunctionSymbolTable.
     */
    public FunctionSymbolTable() {
        functions = new HashMap<>();
    }

    /**
     * Adds a function to the symbol table.
     * @param name The name of the function.
     * @param variables The variables of the function.
     */
    public void addFunction(String name, ArrayList<Variable> variables) {
        functions.put(name, variables);
    }

    /**
     * Checks if a function exists.
     * @param name The name of the function.
     * @return True if the function exists, false otherwise.
     */
    public boolean checkFunctionExists(String name) {
        return functions.containsKey(name);
    }

    /**
     * Gets the variables of a function.
     * @param name The name of the function.
     * @return The variables of the function.
     */
    public ArrayList<Variable> getVariables(String name) {
        return functions.get(name);
    }
}
