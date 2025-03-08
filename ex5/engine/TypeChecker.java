package ex5.engine;

import ex5.symboltables.Types;
import ex5.symboltables.VariableNotFoundException;
import ex5.symboltables.VariablesSymbolTable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class used for checking matching types when assigning a variable
 */
public class TypeChecker {

    private static final Pattern intPattern = Pattern.compile(RegexPatterns.INT_PATTERN);
    private static final Pattern doublePattern = Pattern.compile(RegexPatterns.DOUBLE_PATTERN);
    private static final Pattern charPattern = Pattern.compile(RegexPatterns.CHAR_PATTERN);
    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";

    /**
     * Checks if the given value string matches the specified type.
     * @param value the value to be checked(could be a variable name too, in this case checks the valur of
     *              the variable)
     * @param type the type to check against (e.g., int, double, boolean, etc.)
     * @return true if the value matches the type, false otherwise
     */
    public static boolean checkType(String value, Types type, VariablesSymbolTable symbolTable) {
        Matcher intMatcher = intPattern.matcher(value);
        Matcher doubleMatcher = doublePattern.matcher(value);
        Matcher charMatcher = charPattern.matcher(value);
        return switch (type) {
            case intType -> isInt(value, symbolTable, intMatcher);
            case doubleType -> isDouble(value, symbolTable, doubleMatcher);
            case booleanType -> isBoolean(value, symbolTable, doubleMatcher);
            case charType -> isChar(value, symbolTable, charMatcher);
            case stringType -> isString(value, symbolTable);
            default -> false;
        };
    }

    private static boolean isInt(String value, VariablesSymbolTable symbolTable, Matcher matcher) {
        if (matcher.matches()) {
            return true;
        }
            try {
                return symbolTable.getType(value) == Types.intType && symbolTable.getValue(value) != null;
            }
            catch (VariableNotFoundException e1) {
                return false;
            }
        }

    private static boolean isDouble(String value, VariablesSymbolTable symbolTable, Matcher matcher) {

        if (matcher.matches()) {
            return true;
        }
        try {
            return (symbolTable.getType(value) == Types.intType ||
                    symbolTable.getType(value) == Types.doubleType) && symbolTable.getValue(value) != null;
        }
            catch (VariableNotFoundException e) {
            return false;
            }
        }

    private static boolean isBoolean(String value, VariablesSymbolTable symbolTable, Matcher matcher) {
        if (value.equals(TRUE_VALUE) || value.equals(FALSE_VALUE)) {
            return true;
        }
        if (matcher.matches()) {
            return true;
        }
        try {
            return (symbolTable.getType(value) == Types.intType ||
                    symbolTable.getType(value) == Types.doubleType
                    || symbolTable.getType(value) == Types.booleanType) && symbolTable.getValue(value)
                                                                           != null;
        }
        catch (VariableNotFoundException e) {
            return false;
        }
    }

    private static boolean isChar(String value, VariablesSymbolTable symbolTable, Matcher matcher) {
        if (matcher.matches()) {
            return true;
        }
        try {
            return symbolTable.getType(value) == Types.charType && symbolTable.getValue(value) != null;
        }
        catch (VariableNotFoundException e) {
            return false;
        }
    }

    private static boolean isString(String value, VariablesSymbolTable symbolTable) {
        if (value.startsWith("\"") && value.endsWith("\"")){
            return true;
        }
        try {
            return symbolTable.getType(value) == Types.stringType && symbolTable.getValue(value) != null;
        }
        catch (VariableNotFoundException e) {
            return false;
        }
    }
}
