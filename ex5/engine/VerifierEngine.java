package ex5.engine;

import ex5.main.JavaSCompilationException;
import ex5.symboltables.Types;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VerifierEngine orchestrates a two-pass compilation process of a JavaS file.
 * It parses and validates syntax, declarations, and function bodies.
 */
public class VerifierEngine {

    private static final String FINAL_VAR_DEC_LINE_TYPE = "Final Variable Declaration";
    private static final String STRING_TYPE = "String";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String INT_TYPE = "int";
    private static final String DOUBLE_TYPE = "double";
    private static final String CHAR_TYPE = "char";
    private static final String BLANK_LINE_TYPE = "Blank";
    private static final String CLOSING_BRACKET_LINE_TYPE = "Closing Bracket";
    private static final String NON_FINAL_VAR_DEC_LINE_TYPE = "Non-Final Variable Declaration";
    private static final String VAR_ASSIGN_LINE_TYPE = "Variable Assignment";
    private static final String FUNC_DEC_LINE_TYPE = "Function Declaration";
    private static final String FUNC_CALL_LINE_TYPE = "Function Call";
    private static final String IF_LINE_TYPE = "If Statement";
    private static final String WHILE_LINE_TYPE = "While Statement";
    private static final String RETURN_LINE_TYPE = "Return Statement";
    private static final String OPENING_BRACKET = "{";
    private static final String CLOSING_BRACKET = "}";

    /** Map to match string of type to enum of type
     */
    public static final HashMap<String, Types> stringMap = new HashMap<>();
    static {
        stringMap.put(STRING_TYPE, Types.stringType);
        stringMap.put(BOOLEAN_TYPE, Types.booleanType);
        stringMap.put(INT_TYPE, Types.intType);
        stringMap.put(DOUBLE_TYPE, Types.doubleType);
        stringMap.put(CHAR_TYPE, Types.charType);
    }

    // Class fields
    private BufferedReader reader;
    private final String filePath;
    private final HashMap<String, Pattern> linePatterns;
    private int scopeCounter;
    private boolean insideMethod;
    private boolean isFirstRun;
    private int linesCounter;
    private final LineHandler lineHandler;

    /**
     * Constructs a VerifierEngine instance.
     *
     * @param filePath The path to the JavaS file to be compiled.
     */
    public VerifierEngine(String filePath) {
        this.filePath = filePath;
        linePatterns = new HashMap<>();
        fillPatterns();
        scopeCounter = 0;
        insideMethod = false;
        isFirstRun = true;
        linesCounter = 0;
        this.lineHandler = new LineHandler(this);
    }

    /**
     * Returns the insideMethod field.
     *
     * @return The insideMethod field.
     */
    public boolean isInsideMethod() {
        return insideMethod;
    }

    /**
     * Sets the insideMethod field to the given value.
     *
     * @param insideMethod The new value of the insideMethod field.
     */
    public void setInsideMethod(boolean insideMethod) {
        this.insideMethod = insideMethod;
    }

    /**
     * Returns the current line number during the compilation process.
     *
     * @return The line number.
     */
    public int getLinesCounter() {
        return linesCounter;
    }

    /**
     * Returns the scope counter.
     *
     * @return The scope counter.
     */
    public int getScopeCounter() {
        return scopeCounter;
    }

    /**
     * Increases the scope counter by 1.
     */
    public void increaseScopeCounter() {
        scopeCounter++;
    }

    /**
     * Decreases the scope counter by 1.
     */
    public void decreaseScopeCounter() {
        scopeCounter--;
    }

    /**
     * Executes a two-pass compilation process on the file.
     *
     * @throws JavaSCompilationException If a Sjava compilation error occurs.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public void run() throws JavaSCompilationException, IOException {
        try (BufferedReader firstPassReader = new BufferedReader(new FileReader(this.filePath))) {
            this.reader = firstPassReader;
            doRun();
        }
        isFirstRun = false;
        try (BufferedReader secondPassReader = new BufferedReader(new FileReader(this.filePath))) {
            this.reader = secondPassReader;
            linesCounter = 0;
            scopeCounter = 0;
            doRun();
        }
        if (getScopeCounter() != 0) {
            throw new InvalidScopeClosingException();
        }
    }

    /**
     * Advances the reader to the next line in the file.
     *
     * @return The type of the line.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws JavaSCompilationException If a Sjava compilation error occurs.
     */
    public String advanceLine() throws IOException, JavaSCompilationException {
        String line = reader.readLine();
        linesCounter++;
        if (line == null) {
            return null;
        }
        if (line.isBlank()) {
            return BLANK_LINE_TYPE;
        }
        for (Map.Entry<String, Pattern> entry : linePatterns.entrySet()) {
            Pattern pattern = entry.getValue();
            String lineType = entry.getKey();
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                compileLine(line, lineType);
                return lineType;
            }
        }
        throw new InvalidSyntaxException(linesCounter);
    }

    // Private Methods

    private void fillPatterns() {
        for (Map.Entry<String, String> entry : RegexPatterns.lineRegexPatterns.entrySet()) {
            String key = entry.getKey();
            String pattern = entry.getValue();
            linePatterns.put(key, Pattern.compile(pattern));
        }
    }

    private void doRun() throws IOException, JavaSCompilationException {
        String lineType = advanceLine();
        while (lineType != null) {
            lineType = advanceLine();
        }
    }


    private void compileLine(String line, String lineType) throws JavaSCompilationException, IOException {
        String[] tokens = line.split(RegexPatterns.LINE_SPLIT_DELIMITERS);
        tokens = Arrays.stream(tokens).filter(token -> !token.isEmpty()).toArray(String[]::new);
        switch (lineType) {
            case NON_FINAL_VAR_DEC_LINE_TYPE, FINAL_VAR_DEC_LINE_TYPE:
                if ((isFirstRun && (!insideMethod)) || (insideMethod && !isFirstRun)) {
                    lineHandler.handleVarDec(tokens);
                }
                break;

            case VAR_ASSIGN_LINE_TYPE:
                if (!(insideMethod && isFirstRun)) {
                    lineHandler.handleVarAssign(tokens);
                }
                break;
            case FUNC_DEC_LINE_TYPE:
                if (insideMethod) {
                    throw new InvalidScopeFunctionDeclarationException(linesCounter);
                }
                if (isFirstRun) {
                    lineHandler.handleMethodDec(tokens);
                    skipFunctionBody();
                } else {
                    String functionName = tokens[1];
                    lineHandler.handleFunctionBody(functionName);
                }
                break;
            case FUNC_CALL_LINE_TYPE:
                if (insideMethod) {
                    lineHandler.handleMethodCall(tokens);
                } else if (!isFirstRun) {
                    throw new InvalidActionInGlobalScopeException(linesCounter);
                }
                break;
            case IF_LINE_TYPE:
            case WHILE_LINE_TYPE:
                if (insideMethod) {
                    lineHandler.handleIfOrWhile(tokens);
                } else if (!isFirstRun) {
                    throw new InvalidActionInGlobalScopeException(linesCounter);
                }
                break;
            case CLOSING_BRACKET_LINE_TYPE:
                decreaseScopeCounter();
                break;
            case RETURN_LINE_TYPE:
                if (!isFirstRun && !insideMethod){
                    throw new InvalidActionInGlobalScopeException(linesCounter);
                }
        }
    }

    private void skipFunctionBody() throws IOException,JavaSCompilationException {
        int openBrackets = 1;
        String line;
        while (openBrackets > 0 && (line = reader.readLine()) != null) {
            linesCounter++;
            if (line.contains(OPENING_BRACKET)) {
                openBrackets++;
            }
            if (line.contains(CLOSING_BRACKET)) {
                openBrackets--;
            }
        }
        if (openBrackets != 0) {
            throw new InvalidScopeClosingException();
        }
    }
}