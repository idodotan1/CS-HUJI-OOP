package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for matching characters to their corresponding
 * brightness values using a TreeMap. This class is initialized with a set of characters.
 */
public class SubImgCharMatcher {
    private static final int BOLLEAN_TABLE_SIZE = 16;
    private TreeMap<Double, PriorityQueue<Character>> charMap;
    private final HashMap<Character,Double> charMapUnNormalized;
    private double minBrightness;
    private double maxBrightness;
    private boolean roundUp;
    private boolean roundDown;
    private boolean newNormaliztionNeeded;

    /**
     * Constructs a SubImgCharMatcher with the specified set of characters.
     *
     * @param charSet an array of characters to be used for matching
     */
    public SubImgCharMatcher(char[] charSet) {
        maxBrightness = 0;
        minBrightness = 1;
        charMap = new TreeMap<>();
        charMapUnNormalized = new HashMap<>();
        newNormaliztionNeeded = true;
        fillCharMap(charSet);
        roundDown = false;
        roundUp = false;

    }

    /**
     * Adds a character to the charMap with its calculated brightness.
     * @param c the character to be added
     */
    public void addChar(char c) {
        if (charMapUnNormalized.containsKey(c)) {
            return;
        }
        double brightness = calculateBrightness(c);
        charMapUnNormalized.put(c,brightness);
        checkNewNormalization(brightness);
        if (!newNormaliztionNeeded) {
            brightness = normalizeBrightness(brightness);
            if (!charMap.containsKey(brightness)) {
                charMap.put(brightness, new PriorityQueue<>());
            }
            charMap.get(brightness).add(c);
        }
        if (newNormaliztionNeeded) {
            charMap = normalizeAllBrightness();
            newNormaliztionNeeded = false;
        }
    }
    private void calculateNewMinMaxBrightness(){
        minBrightness = 1;
        maxBrightness = 0;
        for (double brightness : charMapUnNormalized.values()) {
            if (brightness < minBrightness) {
                minBrightness = brightness;
            }
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
            }
        }
    }
    /**
     * Removes a character from the charMap.
     * @param c the character to be removed
     */
    public void removeChar(char c) {
        if (!charMapUnNormalized.containsKey(c)) {
            return;
        }
        double brightness = calculateBrightness(c);
        if (brightness == maxBrightness || brightness == minBrightness) {
            newNormaliztionNeeded = true;
        }
        brightness = normalizeBrightness(brightness);
        PriorityQueue<Character> currentHeap = charMap.get(brightness);
        if (currentHeap != null) {
            currentHeap.remove(c);
        }
        if (currentHeap == null || currentHeap.isEmpty()) {
            charMap.remove(brightness);
        }
        charMapUnNormalized.remove(c);
        if (newNormaliztionNeeded) {
            calculateNewMinMaxBrightness();
            charMap = normalizeAllBrightness();
            newNormaliztionNeeded = false;
        }
    }

    /**
     * Removes all characters from the class.
     */
    public void removeAllChars(){
        charMap.clear();
        charMapUnNormalized.clear();
        minBrightness = 1;
        maxBrightness = 0;
    }

    /**
     * Retrieves a character from the charMap based on the given brightness and the rounding mechanism
     * determined
     *
     * @param brightness the brightness value to match
     * @return the character that matches the given brightness
     */
    public char getCharByImageBrightness(double brightness) {
        Double biggerBrightness = charMap.ceilingKey(brightness);
        Double smallerBrightness = charMap.floorKey(brightness);
        if (biggerBrightness == null || (roundDown && smallerBrightness != null)) {
            return charMap.get(smallerBrightness).peek();
        }
        if (smallerBrightness == null || roundUp) {
            return charMap.get(biggerBrightness).peek();
        }
        double ceilingDiff = Math.abs(biggerBrightness - brightness);
        double floorDiff = Math.abs(smallerBrightness - brightness);
        if (floorDiff > ceilingDiff) {
            return charMap.get(biggerBrightness).peek();
        }
        return charMap.get(smallerBrightness).peek();
    }

    private double normalizeBrightness(double brightness) {
        if (brightness == minBrightness) {
            return 0;
        }
        if (brightness == maxBrightness) {
            return 1;
        }
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    private void fillCharMap(char[] charSet) {
        for (char c : charSet) {
            addChar(c);
        }
    }

    private TreeMap<Double, PriorityQueue<Character>> normalizeAllBrightness() {
        TreeMap<Double, PriorityQueue<Character>> normalizedMap = new TreeMap<>();
        calculateNewMinMaxBrightness();
        for (Map.Entry<Character, Double> c : charMapUnNormalized.entrySet()) {
            double newBrightness = normalizeBrightness(c.getValue());
            if (!normalizedMap.containsKey(newBrightness)) {
                normalizedMap.put(newBrightness, new PriorityQueue<>());
            }
            normalizedMap.get(newBrightness).add(c.getKey());
        }
        return normalizedMap;
    }

    private void checkNewNormalization(double brightness) {
        if (brightness < minBrightness) {
            minBrightness = brightness;
            newNormaliztionNeeded = true;
        }
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
            newNormaliztionNeeded = true;
        }
    }

    private double calculateBrightness(char c) {
        boolean[][] charSquare = CharConverter.convertToBoolArray(c);
        int counter = 0;
        for (int i = 0; i < BOLLEAN_TABLE_SIZE; i++) {
            for (int j = 0; j < BOLLEAN_TABLE_SIZE; j++) {
                if (charSquare[i][j]) {
                    counter++;
                }
            }
        }
        return (double) counter / (BOLLEAN_TABLE_SIZE * BOLLEAN_TABLE_SIZE);
    }
    /**
     * Returns the characters in the charMap in ascending order.
     */
    public TreeSet<Character> getCharSet(){
        return new TreeSet<>(charMapUnNormalized.keySet());
    }

    /**
     * Changes the rounding mechanism of the class.
     * @param round the rounding mechanism to be used
     */
    public void changeRounding(String round){
        switch (round){
            case "up":
                roundUp = true;
                roundDown = false;
                break;
            case "down":
                roundDown = true;
                roundUp = false;
                break;
            case "abs":
                roundDown = false;
                roundUp = false;
                break;
        }
    }
}