package ascii_art;

import image.Image;
import image.ImageOperator;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class is responsible for converting an image to ASCII art.
 */
public class AsciiArtAlgorithm {
    private static double[][] brightnessArray;
    private Image image;
    private final int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private final ImageOperator imageOperator;

    /**
     * Constructs an AsciiArtAlgorithm with the specified image, resolution, and SubImgCharMatcher.
     * @param image The image to be converted to ASCII art.
     * @param resolution The resolution of the ASCII art.
     * @param subImgCharMatcher The SubImgCharMatcher to be used for
     * matching characters to brightness values.
     * @param needToSetBrightness A boolean indicating whether the brightness of the image needs to be reset.
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher subImgCharMatcher,
                             boolean needToSetBrightness) {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
        this.imageOperator = new ImageOperator();
        this.image = imageOperator.addPadding(image);
        if (needToSetBrightness) {
            setBrightness();
        }
    }

    private void setBrightness() {
        Image[][] subImages = imageOperator.divideImageNonSquare(image, resolution);
        int subImageSize = image.getWidth() / resolution;
        int rows = image.getHeight() / subImageSize;
        brightnessArray = new double[rows][resolution];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < resolution; j++) {
                double subImageBrightness = imageOperator.calculateBrightness(subImages[i][j]);
                brightnessArray[i][j] = subImageBrightness;
            }
        }
    }
    /**
     * Converts the image to ASCII art.
     * @return A 2D char array representing the ASCII art.
     */
    public char[][] run() {
        int subImageSize = image.getWidth() / resolution;
        int rows = image.getHeight() / subImageSize;
        char[][] newImage = new char[rows][resolution];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < resolution; j++) {
                double subImageBrightness = brightnessArray[i][j];
                char currentChar = subImgCharMatcher.getCharByImageBrightness(subImageBrightness);
                newImage[i][j] = currentChar;
            }
        }
        return newImage;
    }
}