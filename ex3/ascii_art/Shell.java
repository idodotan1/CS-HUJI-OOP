package ascii_art;

import ascii_art.Exceptions.*;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.Set;

/**
 * The Shell class provides a command-line interface for interacting with the ASCII art generator.
 * It allows users to add or remove characters, change resolution, change rounding, change output format,
 * and run the ASCII art generation algorithm.
 */
public class Shell {
    private static final int DEFAULT_RESOLUTION = 2;
    private static final char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',};
    private static final String INPUT_SIGN = ">>> ";
    private static final String EXIT_WORD = "exit";
    private static final String VIEW_CHARS_WORD = "chars";
    private static final String ADD_CHAR_WORD = "add";
    private static final char CHAR_FOR_RANGE = '-';
    private static final String ADD_ALL_CHARS_WORD = "all";
    private static final String ADD_SPACE_CHAR_WORD = "space";
    private static final char SPACE_CHAR = ' ';
    private static final String INVALID_ADD = "Did not add due to incorrect format.";
    private static final String REMOVE_CHAR_WORD = "remove";
    private static final String INVALID_REMOVE = "Did not remove due to incorrect format.";
    private static final String CHANGE_RESOLUTION_WORD = "res";
    private static final String DOUBLE_RESOLUTION_WORD = "up";
    private static final String HALF_RESOLUTION_WORD = "down";
    private static final String RESOLUTION_MESSAGE = "Resolution set to ";
    private static final String INVALID_RESOLUTION_CHANGE = "Did not change resolution " +
                                                            "due to exceeding boundaries.";
    private static final String INVALID_RESOLUTION_FORMAT = "Did not change resolution due to incorrect " +
                                                            "format.";
    private static final String CHANGE_ROUNDING_WORD = "round";
    private static final Set<String> ROUND_WORDS = Set.of("up", "down", "abs");
    private static final String INVALID_ROUND_FORMAT = "Did not change rounding method due to incorrect " +
                                                       "format.";
    private static final String CHANGE_OUTPUT_WORD = "output";
    private static final String CHANGE_OUTPUT_TO_HTML = "html";
    private static final String DEFAULT_FONT = "Courier new";
    private static final String DEFAULT_HTML_FILE = "out.html";
    private static final String CHANGE_OUTPUT_TO_CONSOLE = "console";
    private static final String INVALID_OUTPUT_FORMAT = "Did not change output method due to incorrect " +
                                                        "format.";
    private static final String RUN_ALGORITHM_WORD = "asciiArt";
    private static final String INVALID_CHARSET = "Did not execute. Charset is too small.";
    private static final String INVALID_COMMAND = "Did not execute due to incorrect command.";
    private static final char MIN_CHAR = 32;
    private static final char MAX_CHAR = 126;
    private static final int RANGE_LENGTH = 3;
    private static final int RESOLUTION_FACTOR = 2;
    private static final int END_OF_RANGE_INDEX = 2;
    private static final int MIN_CHAR_SET_SIZE = 2;
    private boolean isConsoleOutput;
    private boolean changedResolution;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs a Shell object with a default character set.
     */
    public Shell() {
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
        isConsoleOutput = true;
    }

    private void printError(Exception e) {
        System.out.println(e.getMessage());
    }

    private void processChars(String chars, boolean isAddOperation) throws InputException {

        if (chars.length() == 1) {
            processSingleChar(chars.charAt(0), isAddOperation);
            return;
        }
        if (chars.equals(ADD_ALL_CHARS_WORD)) {
            processAllChars(isAddOperation);
            return;
        }
        if (chars.equals(ADD_SPACE_CHAR_WORD)) {
            if (isAddOperation) {
                subImgCharMatcher.addChar(SPACE_CHAR);
            } else {
                subImgCharMatcher.removeChar(SPACE_CHAR);
            }
            return;
        }
        if (chars.length() == RANGE_LENGTH) {
            processCharRange(chars, isAddOperation);
            return;
        }
        throwInputException(isAddOperation);
    }

    private void throwInputException(boolean isAddOperation) throws InputException {
        if (isAddOperation) {
            throw new InvalidAddFormatException(INVALID_ADD);
        } else {
            throw new InvalidRemoveFormatException(INVALID_REMOVE);
        }
    }

    private void processSingleChar(char c, boolean isAddOperation) throws InputException {
        if (c < MIN_CHAR || c > MAX_CHAR) {
            throwInputException(isAddOperation);
        }
        if (isAddOperation) {
            subImgCharMatcher.addChar(c);
        } else {
            subImgCharMatcher.removeChar(c);
        }
    }

    private void processAllChars(boolean isAddOperation) {
        if (isAddOperation) {
            for (char c = MIN_CHAR; c <= MAX_CHAR; c++) {
                subImgCharMatcher.addChar(c);
            }
            return;
        }
        subImgCharMatcher.removeAllChars();
    }

    private void processCharRange(String chars, boolean isAddOperation) throws InputException {
        char start = chars.charAt(0);
        char middle = chars.charAt(1);
        char end = chars.charAt(END_OF_RANGE_INDEX);

        if (start < MIN_CHAR || start > MAX_CHAR || end < MIN_CHAR || end > MAX_CHAR ||
            middle != CHAR_FOR_RANGE) {
            throwInputException(isAddOperation);
        }

        char minChar = (char) Math.min(start, end);
        char maxChar = (char) Math.max(start, end);

        for (char c = minChar; c <= maxChar; c++) {
            if (isAddOperation) {
                subImgCharMatcher.addChar(c);
            } else {
                subImgCharMatcher.removeChar(c);
            }
        }
    }

    private void printResolution(int resolution) {
        System.out.println(RESOLUTION_MESSAGE + resolution + ".");
    }

    private void printCharSet() {
        for (char c : subImgCharMatcher.getCharSet()) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    /**
     * Runs the shell and processes user commands.
     *
     * @param imageName the name of the image file
     */
    public void run(String imageName) {
        Image image;
        try {
            image = new Image(imageName);
        }
        catch (IOException e){
            return;
        }
        int resolution = DEFAULT_RESOLUTION;
        int maxResolution = image.getWidth();
        int minResolution = Math.max(1, image.getWidth() / image.getHeight());
        boolean firstRun = true;
        while (true) {
            System.out.print(INPUT_SIGN);
            String currentInput = KeyboardInput.readLine();
            String[] currentInputWords = currentInput.split(" ");
            switch (currentInputWords[0]) {
                case EXIT_WORD -> {return;}
                case VIEW_CHARS_WORD -> printCharSet();
                case ADD_CHAR_WORD -> handleAddChar(currentInputWords);
                case REMOVE_CHAR_WORD -> handleRemoveChar(currentInputWords);
                case CHANGE_RESOLUTION_WORD -> resolution =
                        handleChangeResolution(currentInputWords, resolution, maxResolution, minResolution);
                case CHANGE_ROUNDING_WORD -> handleChangeRounding(currentInputWords);
                case CHANGE_OUTPUT_WORD -> handleChangeOutput(currentInputWords);
                case RUN_ALGORITHM_WORD -> {
                    handleRunAlgorithm(image, resolution, firstRun || changedResolution);
                    firstRun = false;
                    changedResolution = false;
                }
                default -> printError(new InvalidCommandException(INVALID_COMMAND));
            }
        }
    }

    private void handleAddChar(String[] inputWords) {
        try {
            processChars(inputWords[1], true);
        } catch (ArrayIndexOutOfBoundsException e) {
            printError(new InvalidAddFormatException(INVALID_ADD));
        } catch (InputException e) {
            printError(e);
        }
    }

    private void handleRemoveChar(String[] inputWords) {
        try {
            processChars(inputWords[1], false);
        } catch (ArrayIndexOutOfBoundsException e) {
            printError(new InvalidRemoveFormatException(INVALID_REMOVE));
        } catch (InputException e) {
            printError(e);
        }
    }

    private int handleChangeResolution(String[] inputWords, int resolution, int maxResolution
            , int minResolution) {
        try {
            String resolutionChange = inputWords[1];
            if (resolutionChange.equals(DOUBLE_RESOLUTION_WORD)) {
                if (resolution * RESOLUTION_FACTOR > maxResolution) {
                    throw new InvalidResolutionException(INVALID_RESOLUTION_CHANGE);
                }
                resolution *= RESOLUTION_FACTOR;
                changedResolution = true;
            } else if (resolutionChange.equals(HALF_RESOLUTION_WORD)) {
                if (resolution / RESOLUTION_FACTOR < minResolution) {
                    throw new InvalidResolutionException(INVALID_RESOLUTION_CHANGE);
                }
                resolution /= RESOLUTION_FACTOR;
                changedResolution = true;
            } else {
                throw new InvalidResolutionException(INVALID_RESOLUTION_FORMAT);
            }
            printResolution(resolution);
        } catch (ArrayIndexOutOfBoundsException e) {
            printResolution(resolution);
        } catch (InvalidResolutionException e) {
            printError(e);
        }
        return resolution;
    }

    private void handleChangeRounding(String[] inputWords) {
        try {
            String roundingChange = inputWords[1];
            if (!ROUND_WORDS.contains(roundingChange)) {
                throw new InvalidRoundingException(INVALID_ROUND_FORMAT);
            }
            subImgCharMatcher.changeRounding(roundingChange);
        } catch (ArrayIndexOutOfBoundsException e) {
            printError(new InvalidRoundingException(INVALID_ROUND_FORMAT));
        } catch (InvalidRoundingException e) {
            printError(e);
        }
    }

    private void handleChangeOutput(String[] inputWords) {
        try {
            String outputChange = inputWords[1];
            if (outputChange.equals(CHANGE_OUTPUT_TO_HTML)) {
                isConsoleOutput = false;
            } else if (outputChange.equals(CHANGE_OUTPUT_TO_CONSOLE)) {
                isConsoleOutput = true;
            } else {
                throw new InvalidOutputFormatException(INVALID_OUTPUT_FORMAT);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printError(new InvalidOutputFormatException(INVALID_OUTPUT_FORMAT));
        } catch (InvalidOutputFormatException e) {
            printError(e);
        }
    }

    private void handleRunAlgorithm(Image image, int resolution, boolean changeResolution) {
        try {
            if (subImgCharMatcher.getCharSet().size() < MIN_CHAR_SET_SIZE) {
                throw new InvalidCharSetException(INVALID_CHARSET);
            }
            AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(image,
                                                                resolution,
                                                                subImgCharMatcher,
                                                                changeResolution);
            char[][] asciiArt = algorithm.run();
            if (isConsoleOutput) {
                ConsoleAsciiOutput consoleOutput = new ConsoleAsciiOutput();
                consoleOutput.out(asciiArt);
            } else {
                HtmlAsciiOutput htmlOutput = new HtmlAsciiOutput(DEFAULT_HTML_FILE, DEFAULT_FONT);
                htmlOutput.out(asciiArt);
            }
        }
        catch (InvalidCharSetException e) {
            printError(e);
        }
    }
    /**
     * The main method to run the shell.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[0]);
    }
}


