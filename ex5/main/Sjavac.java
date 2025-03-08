package ex5.main;

import ex5.engine.VerifierEngine;
import java.io.IOException;

/**
 * This class is the main class of the Sjavac compiler program.
 */
public class Sjavac {

    private static final String INVALID_NUM_OF_ARGS_MESSAGE = "Invalid number of arguments";
    private static final String SJAVA_FILE_ENDING = ".sjava";
    private static final String INVALID_FILE_TYPE_MESSAGE = "Invalid file type: Expected a .sjava file";
    private static final String COMPILATION_SUCCESS_MESSAGE = "Compilation successful";
    private static final String LEGAL_CODE_INDICATOR = "0";
    private static final String ILLEGAL_CODE_INDICATO = "1";
    private static final String IO_ERROR_INDICATOR = "2";

    /**
     * The main method of the Sjavac compiler program.
     * @param args The arguments of the program, should contain only one argument - file path of the
     *            .Sjava file.
     */
    public static void main(String[] args) {
        String filePath = args[0];
        try {
            if (args.length > 1) {
                throw new IOException(INVALID_NUM_OF_ARGS_MESSAGE);
            }
            if (!filePath.endsWith(SJAVA_FILE_ENDING)) {
                throw new IOException(INVALID_FILE_TYPE_MESSAGE);
            }
            VerifierEngine verifierEngine = new VerifierEngine(filePath);
            verifierEngine.run();
            System.out.println(LEGAL_CODE_INDICATOR);
            System.out.println(COMPILATION_SUCCESS_MESSAGE);
        } catch (JavaSCompilationException e) {
            System.out.println(ILLEGAL_CODE_INDICATO);
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(IO_ERROR_INDICATOR);
            System.err.println(e.getMessage());
        }

    }
}

