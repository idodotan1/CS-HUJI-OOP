package ex5.symboltables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents the variables symbol table.
 */
public class VariablesSymbolTable {

    private final ArrayList<HashMap<String, Variable>> methodSymbolTables;
    private final HashMap<String, Variable> globalSymbolTable;
    private boolean insideFunction;
    private final Supplier<Integer> lineCounterGetter;

    /**
     * Constructs a new VariablesSymbolTable.
     * @param lineCounterGetter The line counter getter.
     */
    public VariablesSymbolTable(Supplier<Integer> lineCounterGetter) {
        methodSymbolTables = new ArrayList<>();
        globalSymbolTable = new HashMap<>();
        insideFunction = false;
        this.lineCounterGetter = lineCounterGetter;
    }

    /**
     * Initializes data structures for a new method,
     * copies the global symbol table, marks that we are inside a function,
     * and creates a new scope.
     */
    public void startNewMethod() {
        HashMap<String, Variable> globalCopy = new HashMap<>();
        for (Map.Entry<String, Variable> entry : globalSymbolTable.entrySet()) {
            String key = entry.getKey();
            Variable var = entry.getValue();
            Variable varCopy = new Variable(key, var.getType(), var.getValue(), var.isFinal());
            globalCopy.put(key, varCopy);
        }
        methodSymbolTables.add(globalCopy);
        insideFunction = true;
        newScope();
    }

    /**
     * Ends the current method by clearing all local symbol tables
     * and marking that we are no longer inside a function.
     */
    public void endMethod() {
        methodSymbolTables.clear();
        insideFunction = false;
    }

    /**
     * Creates a new, empty scope (local symbol table) on top of the stack.
     */
    public void newScope() {
        methodSymbolTables.add(new HashMap<>());
    }

    /**
     * Removes the most recent scope (local symbol table) from the stack.
     */
    public void exitScope() {
        methodSymbolTables.removeLast();
    }

    /**
     * Adds a new variable to the current scope.
     * @param variable the variable name
     * @param type the variable type
     * @param value the variable value
     * @param isFinal indicates if the variable is final
     * @throws VariableExistsException if the variable is already declared in the current scope
     */
    public void addVariableToCurrentScope(String variable, Types type, String value, boolean isFinal)
            throws VariableExistsException {
        if (!methodSymbolTables.getLast().containsKey(variable)) {
            methodSymbolTables.getLast().put(variable, new Variable(variable, type, value, isFinal));
            return;
        }
        throw new VariableExistsException(this.lineCounterGetter.get());
    }

    /**
     * Updates the value of an existing variable in the current scope chain or global scope.
     * @param variable the variable name
     * @param value the new value
     * @throws VariableNotFoundException if the variable does not exist
     * @throws ChangeFinalException if the variable is marked final
     */
    public void updateVariableValue(String variable, String value) throws
            VariableNotFoundException, ChangeFinalException {
        if (insideFunction) {
            for (int i = methodSymbolTables.size() - 1; i >= 0; i--) {
                if (methodSymbolTables.get(i).containsKey(variable)) {
                    if (methodSymbolTables.get(i).get(variable).isFinal()) {
                        throw new ChangeFinalException(this.lineCounterGetter.get());
                    }
                    methodSymbolTables.get(i).get(variable).setValue(value);
                    return;
                }
            }
            throw new VariableNotFoundException(lineCounterGetter.get());
        }
        if (globalSymbolTable.containsKey(variable)) {
            if (globalSymbolTable.get(variable).isFinal()) {
                throw new ChangeFinalException(this.lineCounterGetter.get());
            }
            globalSymbolTable.get(variable).setValue(value);
            return;
        }
        throw new VariableNotFoundException(this.lineCounterGetter.get());
    }


    /**
     * Adds a new variable to the global scope.
     * @param variable the variable name
     * @param type the variable type
     * @param value the variable value
     * @param isFinal indicates if the variable is final
     * @throws VariableExistsException if the variable is already declared in the global scope
     */
    public void addVariableToGlobalScope(String variable, Types type, String value, boolean isFinal)
            throws VariableExistsException {
        if (!globalSymbolTable.containsKey(variable)) {
            globalSymbolTable.put(variable, new Variable(variable, type, value, isFinal));
            return;
        }
        throw new VariableExistsException(this.lineCounterGetter.get());
    }


    /**
     * Returns the type of specified variable.
     * @param variable the variable name
     * @return the variable type
     * @throws VariableNotFoundException if the variable is not found
     */
    public Types getType(String variable) throws VariableNotFoundException {
        if (insideFunction) {
            for (int i = methodSymbolTables.size() - 1; i >= 0; i--) {
                if (methodSymbolTables.get(i).containsKey(variable)) {
                    return methodSymbolTables.get(i).get(variable).getType();
                }
            }
            throw new VariableNotFoundException(this.lineCounterGetter.get());
        }
        if (globalSymbolTable.containsKey(variable)) {
            return globalSymbolTable.get(variable).getType();
        }
        throw new VariableNotFoundException(this.lineCounterGetter.get());
    }


    /**
     * Returns the value of a specified variable.
     * @param variable the variable name
     * @return the variable value
     * @throws VariableNotFoundException if the variable is not found
     */
    public String getValue(String variable) throws VariableNotFoundException {
        if (insideFunction) {
            for (int i = methodSymbolTables.size() - 1; i >= 0; i--) {
                if (methodSymbolTables.get(i).containsKey(variable)) {
                    return methodSymbolTables.get(i).get(variable).getValue();
                }
            }
            throw new VariableNotFoundException(this.lineCounterGetter.get());
        }
        if (globalSymbolTable.containsKey(variable)) {
            return globalSymbolTable.get(variable).getValue();
        }
        throw new VariableNotFoundException(this.lineCounterGetter.get());
    }
}

