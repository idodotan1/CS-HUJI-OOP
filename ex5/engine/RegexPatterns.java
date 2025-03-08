package ex5.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds various regex patterns and grouping structures for the JavaS.
 */
public class RegexPatterns {

    /** matches boolean literal
     */
    public static final String BOOLEAN_LITERAL_PATTERN = "true|false";

    /** matches integer literal (optional +/- sign)
     */
    public static final String INT_PATTERN = "[+-]?\\d+";

    /** matches double literal (optional +/- sign, decimal)
     */
    public static final String DOUBLE_PATTERN = "(?:[+-]?\\d*\\.?\\d+)|(?:[+-]?\\d+\\.?)";

    /** matches single-character literal
     */
    public static final String CHAR_PATTERN = "'[^']{1}'";

    /** matches recognized types (int, String, char, boolean, double)
     */
    public static final String TYPES_PATTERN = "(?:int|String|char|boolean|double)";

    /** matches valid function names
     */
    public static final String FUNCTION_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

    /** matches valid variable names
     */
    public static final String VAR_NAME_PATTERN =
            "(?:[_][a-zA-Z0-9][a-zA-Z0-9_]*)|(?:" + FUNCTION_NAME_PATTERN + ")";


    /** matches a general value (strings, chars, numbers, or var names)
     */
    public static final String VALUE_PATTERN =
            "(?:\"[^\"]*\"|'[^']?'|" + INT_PATTERN + "|" + DOUBLE_PATTERN + "|" + VAR_NAME_PATTERN + ")";

    /** matches a single function parameter with optional 'final'
     */
    public static final String PARAMETER_PATTERN =
            "(?:final\\s+)?" + TYPES_PATTERN + "\\s+(?:" + VAR_NAME_PATTERN + ")";

    /** matches a parameter list in parentheses
     */
    public static final String PARAMETER_LIST_PATTERN =
            "\\(\\s*(?:" + PARAMETER_PATTERN + "(?:\\s*,\\s*" + PARAMETER_PATTERN + "\\s*)*)?\\s*\\)";

    /** matches parameters for a function call
     */
    public static final String FUNCTION_CALL_PARAMETERS_PATTERN =
            "\\(\\s*(?:(?:" + VAR_NAME_PATTERN + "|" + VALUE_PATTERN + ")(?:\\s*,\\s*" + "(?:" +
            VAR_NAME_PATTERN + "|" + VALUE_PATTERN + ")" + "\\s*)*)?\\s*\\)";

    /** matches boolean expressions with optional logical operators
     */
    public static final String BOOLEAN_EXPRESSION_PATTERN =
            "(?:" + BOOLEAN_LITERAL_PATTERN + "|" + VAR_NAME_PATTERN + "|" + DOUBLE_PATTERN + "|" +
            INT_PATTERN + ")"
            + "(?:\\s*(?:\\|\\||&&)\\s*(?:" + BOOLEAN_LITERAL_PATTERN + "|" + VAR_NAME_PATTERN + "|"
            + DOUBLE_PATTERN + "|" + INT_PATTERN + "))*";

    /**matches condition parentheses (e.g., for if/while)
     */
    public static final String CONDITION_PATTERN = "\\s*\\(\\s*" +
                                                   BOOLEAN_EXPRESSION_PATTERN + "\\s*\\)\\s*";

    /** delimiters and splits used for tokenizing lines
     */
    public static final String LINE_SPLIT_DELIMITERS =
            "(?<=\\w)(?=[=,;()])" +             // Split when a word character is followed by =, ,, ;, or ()
            "|(?<=[=,()])(?=\\w)" +             // Split when =, ,, ;, or () is followed by a word character
            "|\\s+" +                               // Split on any whitespace
            "|(?<=\\w)(?=\\|\\|)|(?<=\\|\\|)(?=\\w)" + // Split when || is adjacent to a word character
            "|(?<=\\w)(?=&&)|(?<=&&)(?=\\w)" +      // Split when && is adjacent to a word character
            "|(?<=\\d)(?=[;,=()])" +                 // Split when a digit is followed by ; or ,
            "|(?<=[,=()])(?=\\d)" +                 // Split when ; or , is followed by a digit
            "|(?<=\"[^\"]*\")(?=[();,])" +            // Split when a quoted string is followed by ; or ,
            "|(?<=[,=()])(?=\"[^\"]*\")" +     // Split when ; or , or = or () is followed by a quoted string
            "|(?<='[^']*')(?=[;,=()])" +        // Split when a single-quoted string is followed by ; =() or ,
            "|(?<=[,=()])(?='[^']*')" +     //Split when ; =,(), or =  is followed by a single-quoted string
            "|(?<=\\.)(?=[;,])|" +         //Split when . is followed by ;,
            "(?<=[=,()])(?=[\\.\\+-])|" +   //Split when = () , is followed by .,+,-
            "(?<=[()])(?=[;,{)])|"+          //Split when () is followed by ;,{
            "(?<=[\\.\\+-])(?=&&)|(?<=&&)(?=[\\.\\+-])|" + //Split when && is followed or before +-.
            "(?<=[\\.\\+-])(?=\\|\\|)|(?<=\\|\\|)(?=[\\.\\+-])"; //Split when || is followed or before +-.
    /** map for line-level regex patterns
     */
    public static final Map<String, String> lineRegexPatterns = new HashMap<>();
    static {
        // matches final variable declarations
        lineRegexPatterns.put("Final Variable Declaration",
                              "\\s*final\\s+" + TYPES_PATTERN + "\\s+(?:" +
                              VAR_NAME_PATTERN + ")\\s*=\\s*(?:"
                              + VALUE_PATTERN + ")(?:\\s*,\\s*(?:" + VAR_NAME_PATTERN + ")\\s*=\\s*(?:" +
                              VALUE_PATTERN + "))*\\s*;");
        // matches non-final variable declarations
        lineRegexPatterns.put("Non-Final Variable Declaration", "\\s*" + TYPES_PATTERN + "\\s+(?:" +
                                                                VAR_NAME_PATTERN + ")(?:\\s*=\\s*(?:" +
                                                                VALUE_PATTERN + "))?(?:\\s*,\\s*" +
                                                                "(?:" + VAR_NAME_PATTERN + ")(?:\\s*=\\s*(?:"
                                                                + VALUE_PATTERN + "))?)*\\s*;");
        // matches variable assignments
        lineRegexPatterns.put("Variable Assignment",
                              "\\s*(?:" + VAR_NAME_PATTERN +
                              ")(?:\\s*=\\s*(?:" + VALUE_PATTERN + "))(?:\\s*,\\s*" +
                              "(?:" + VAR_NAME_PATTERN + ")(?:\\s*=\\s*(?:"
                              + VALUE_PATTERN + ")))?\\s*;");

        // matches comment lines
        lineRegexPatterns.put("Comment Pattern", "//.*");

        // matches function declaration lines
        lineRegexPatterns.put("Function Declaration",
                              "\\s*void\\s+" + FUNCTION_NAME_PATTERN + "\\s*" +
                              PARAMETER_LIST_PATTERN + "\\s*\\{");

        // matches function calls
        lineRegexPatterns.put("Function Call",
                              "\\s*" + FUNCTION_NAME_PATTERN + "\\s*" + FUNCTION_CALL_PARAMETERS_PATTERN +
                              "\\s*;");

        // matches closing bracket for scope closing
        lineRegexPatterns.put("Closing Bracket", "\\s*\\}\\s*");

        // matches if statements
        lineRegexPatterns.put("If Statement", "\\s*if\\s*" + CONDITION_PATTERN + "\\{\\s*");

        // matches while statements
        lineRegexPatterns.put("While Statement", "\\s*while\\s*" + CONDITION_PATTERN + "\\{\\s*");

        // matches return statements
        lineRegexPatterns.put("Return Statement", "\\s*return\\s*;");
    }
}
