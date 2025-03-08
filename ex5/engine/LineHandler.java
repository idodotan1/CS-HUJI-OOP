package ex5.engine;

import ex5.main.JavaSCompilationException;
import ex5.symboltables.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles different lines of code, such as variable declarations, assignments,
 * method declarations, and control statements (e.g., if/while).
 */
public class LineHandler {

    // Constants for tokens and statements
    private static final String FINAL = "final";
    private static final String ASSIGNMENT_OPERATOR = "=";
    private static final String SEMICOLON = ";";
    private static final String CLOSING_BRACKET = "Closing Bracket";
    private static final String RETURN_STATEMENT = "Return Statement";
    private static final String BLANK = "Blank";
    private static final String LEGAL_VALUE = "legalValue";
    private static final String CLOSING_PARENTHESES = ")";
    private final VariablesSymbolTable variablesSymbolTable;
    private final FunctionSymbolTable functionSymbolTable;
    private final VerifierEngine verifierEngine;

    /**
     * Constructs a LineHandler.
     *
     * @param verifierEngine the VerifierEngine that coordinates the verification process.
     */
    public LineHandler(VerifierEngine verifierEngine) {
        this.verifierEngine = verifierEngine;
        variablesSymbolTable = new VariablesSymbolTable(verifierEngine::getLinesCounter);
        functionSymbolTable = new FunctionSymbolTable();
    }

    /**
     * Handles the declaration of variables.
     *
     * @param tokens the tokens representing the variable declaration.
     * @throws JavaSCompilationException if there is an invalid assignment or variable declaration.
     */
    public void handleVarDec(String[] tokens) throws JavaSCompilationException {
        int tokenCounter = 0;
        boolean isFinal = isTokenEqual(tokens[tokenCounter], FINAL);
        if (isFinal) {
            tokenCounter++;
        }
        Types type = getType(tokens[tokenCounter]);
        if (type == null) {
            return;
        }
        while (true) {
            tokenCounter++;
            String variable = tokens[tokenCounter++];
            String value = null;
            if (isTokenEqual(tokens[tokenCounter], ASSIGNMENT_OPERATOR)) {
                tokenCounter++;
                value = tokens[tokenCounter++];
            }
            validateAssignment(value, type);
            addVariableToScope(variable, type, value, isFinal);
            if (isTokenEqual(tokens[tokenCounter], SEMICOLON)) {
                break;
            }
        }
    }

    /**
     * Handles the assignment of values to variables.
     *
     * @param tokens the tokens representing the variable assignment.
     * @throws JavaSCompilationException if the assignment is invalid or the variable does not exist.
     */
    public void handleVarAssign(String[] tokens) throws JavaSCompilationException {
        int tokenCounter = 0;
        while (true) {
            String variable = tokens[tokenCounter++];
            tokenCounter ++;
            String value = tokens[tokenCounter++];
            Types type = getVariableType(variable);
            validateAssignment(value, type);
            variablesSymbolTable.updateVariableValue(variable, value);
            if (isTokenEqual(tokens[tokenCounter], SEMICOLON)) {
                break;
            }
        }
    }

    /**
     * Handles the declaration of a method.
     *
     * @param tokens the tokens representing the method declaration.
     * @throws JavaSCompilationException if the method declaration is invalid.
     */
    public void handleMethodDec(String[] tokens) throws JavaSCompilationException {
        int tokenCounter = 1;
        String functionName = tokens[tokenCounter];
        if (functionSymbolTable.checkFunctionExists(functionName)) {
            throw new FunctionExistsException(verifierEngine.getLinesCounter());
        }
        tokenCounter++;
        tokenCounter++;
        ArrayList<Variable> params = extractMethodParameters(tokens, tokenCounter);
        functionSymbolTable.addFunction(functionName, params);
    }

    /**
     * Handles the body of a method during verification.
     *
     * @param functionName the name of the function being processed.
     * @throws JavaSCompilationException if the function body is invalid.
     * @throws IOException if an I/O error occurs while reading lines.
     */
    public void handleFunctionBody(String functionName) throws JavaSCompilationException, IOException {
        verifierEngine.setInsideMethod(true);
        variablesSymbolTable.startNewMethod();
        ArrayList<Variable> params = functionSymbolTable.getVariables(functionName);
        for (Variable var : params) {
            variablesSymbolTable.addVariableToCurrentScope(var.getVariableName(), var.getType(),
                                                           LEGAL_VALUE, var.isFinal());
        }
        verifierEngine.increaseScopeCounter();
        processFunctionBody();
        variablesSymbolTable.endMethod();
        verifierEngine.setInsideMethod(false);
    }

    /**
     * Handles method calls.
     *
     * @param tokens the tokens representing the method call.
     * @throws JavaSCompilationException if the method call is invalid.
     */
    public void handleMethodCall(String[] tokens) throws JavaSCompilationException {
        int tokenCounter = 0;
        String functionName = tokens[tokenCounter];
        if (!functionSymbolTable.checkFunctionExists(functionName)) {
            throw new FunctionNotFoundException(verifierEngine.getLinesCounter());
        }
        tokenCounter++;
        tokenCounter++;
        validateMethodCall(tokens, tokenCounter, functionName);
    }

    /**
     * Handles if/while statements.
     *
     * @param tokens the tokens representing the statement.
     * @throws JavaSCompilationException if the boolean expression or block is invalid.
     * @throws IOException if an I/O error occurs while reading lines.
     */
    public void handleIfOrWhile(String[] tokens) throws JavaSCompilationException, IOException {
        if (!checkBooleanExpression(tokens)) {
            throw new InvalidBooleanExpressionException(verifierEngine.getLinesCounter());
        }
        variablesSymbolTable.newScope();
        verifierEngine.increaseScopeCounter();
        processBlock();
        variablesSymbolTable.exitScope();
    }

    // Private Methods
    private Types getType(String token) {
        return VerifierEngine.stringMap.get(token);
    }

    private boolean isTokenEqual(String token, String expected) {
        return token.equals(expected);
    }

    private void validateAssignment(String value, Types type) throws JavaSCompilationException {
        if (value != null && !TypeChecker.checkType(value, type, variablesSymbolTable)) {
            throw new InvalidAssignmentException(verifierEngine.getLinesCounter());
        }
    }

    private void addVariableToScope(String variable, Types type, String value, boolean isFinal)
            throws JavaSCompilationException {
        if (verifierEngine.isInsideMethod()) {
            variablesSymbolTable.addVariableToCurrentScope(variable, type, value, isFinal);
        } else {
            variablesSymbolTable.addVariableToGlobalScope(variable, type, value, isFinal);
        }
    }

    private Types getVariableType(String variable) throws JavaSCompilationException {
        Types type = variablesSymbolTable.getType(variable);
        if (type == null) {
            throw new VariableNotFoundException(verifierEngine.getLinesCounter());
        }
        return type;
    }

    private ArrayList<Variable> extractMethodParameters(String[] tokens, int tokenCounter) {
        ArrayList<Variable> params = new ArrayList<>();
        boolean isFirstIteration = true;
        while (!tokens[tokenCounter].equals(CLOSING_PARENTHESES)) {
            if (!isFirstIteration) {
                tokenCounter++;
            }
            boolean isFinal = isTokenEqual(tokens[tokenCounter], FINAL);
            if (isFinal) {
                tokenCounter++;
            }
            Types type = getType(tokens[tokenCounter++]);
            String variableName = tokens[tokenCounter++];
            params.add(new Variable(variableName, type, isFinal));
            isFirstIteration = false;
        }
        return params;
    }

    private void processFunctionBody() throws JavaSCompilationException, IOException {
        boolean isLastLineReturn = false;
        String lineType = verifierEngine.advanceLine();
        while (lineType != null && !lineType.equals(CLOSING_BRACKET)) {
            if (lineType.equals(RETURN_STATEMENT)) {
                isLastLineReturn = true;
            } else if (!lineType.equals(BLANK)) {
                isLastLineReturn = false;
            }
            lineType = verifierEngine.advanceLine();
        }
        if (lineType == null) {
            throw new InvalidScopeClosingException();
        }
        if(!isLastLineReturn) {
            throw new LastLineInMethodReturnException(verifierEngine.getLinesCounter());
        }
    }

    private void validateMethodCall(String[] tokens, int tokenCounter, String functionName)
            throws JavaSCompilationException {
        ArrayList<Variable> parameterList = functionSymbolTable.getVariables(functionName);
        if (tokens[tokenCounter].equals(CLOSING_PARENTHESES)) {
            if (!parameterList.isEmpty()) {
                throw new InvalidParameterListException(verifierEngine.getLinesCounter());
            }
            return;
        }
        for (int i = 0; i < parameterList.size(); i++) {
            if (!TypeChecker.checkType(tokens[tokenCounter++], parameterList.get(i).getType(),
                                       variablesSymbolTable)) {
                throw new InvalidParameterListException(verifierEngine.getLinesCounter());
            }
            tokenCounter++;
            if (tokens[tokenCounter].equals(CLOSING_PARENTHESES) && !(i == parameterList.size()-1)) {
                throw new InvalidParameterListException(verifierEngine.getLinesCounter());
            }
        }
    }

    private void processBlock() throws IOException, JavaSCompilationException {
        String lineType = verifierEngine.advanceLine();
        while (lineType != null && !lineType.equals(CLOSING_BRACKET)) {
            lineType = verifierEngine.advanceLine();
        }
        if (lineType == null) {
            throw new InvalidScopeClosingException();
        }
    }

    private boolean checkBooleanExpression(String[] tokens) {
        int tokenCounter = 1;
        while (!tokens[tokenCounter].equals(CLOSING_PARENTHESES)) {
            tokenCounter++;
            if (!TypeChecker.checkType(tokens[tokenCounter], Types.booleanType, variablesSymbolTable)) {
                return false;
            }
            tokenCounter++;
        }
        return true;
    }
}
