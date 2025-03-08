package image;

import java.awt.*;



/**
 * A class for dividing an Image instance into sub-images according to a given resolution.
 */
public class ImageOperator {
    private static final int MARGIN_FACTOR = 2;
    private static final double GREEN_TO_GRAY_FACTOR = 0.7152;
    private static final double RED_TO_GRAY_FACTOR = 0.2126;
    private static final double BLUE_TO_GRAY_FACTOR = 0.0722;
    private static final int MAX_RGB = 255;


    /**
     * Pads the given image to the nearest power of 2 dimensions with white pixels.
     *
     * @param image The original Image instance to be padded.
     * @return A new Image instance with the padded dimensions.
     */
    public Image addPadding(Image image){
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int newHeight = calculateNext2Power(imageHeight);
        int newWidth = calculateNext2Power(imageWidth);
        Color[][] paddedPixels = new Color[newHeight][newWidth];
        if(newHeight == imageHeight && newWidth == imageWidth){
            return image;
        }
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                paddedPixels[i][j] = Color.WHITE;
            }
        }
        int widthMargin = (newWidth - imageWidth) / MARGIN_FACTOR;
        int heightMargin = (newHeight - imageHeight) / MARGIN_FACTOR;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                paddedPixels[i + heightMargin][j + widthMargin] = image.getPixel(i, j);
            }
        }
        return new Image(paddedPixels, newWidth, newHeight);
    }


    private int calculateNext2Power(int num) {
        return (num <= 1) ? 1 : Integer.highestOneBit(num - 1) << 1;
    }

    /**
     * Divides the given image into sub-images based on the given resolution.
     *
     * @param image      The original Image instance.
     * @param resolution The number of sub-images.
     * @return A list of sub-images.
     */
    public Image[][] divideImageNonSquare(Image image, int resolution) {
        int subImageSize = image.getWidth() / resolution;
        int rows = image.getHeight() / subImageSize;
        Image[][] subImagesArray = new Image[rows][resolution];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < resolution; j++) {
                Color[][] subPixels = new Color[subImageSize][subImageSize];
                for (int l = 0; l < subImageSize; l++) {
                    for (int k = 0; k < subImageSize; k++) {
                        int globalY = j * subImageSize + k;
                        int globalX = i * subImageSize + l;
                        subPixels[l][k] = image.getPixel(globalX, globalY);
                    }
                }
                subImagesArray[i][j] = new Image(subPixels, subImageSize, subImageSize);
            }
        }

        return subImagesArray;
    }


    /**
     * Calculates brightness of an image using formula with summed gray values of the image pixels.
     *
     * @param image      The original Image instance.
     * @return calculated brightness of the given image.
     */
    public double calculateBrightness(Image image){
        int imageHeight = image.getHeight();
        int imageWidth =  image.getWidth();
        double numOfPixels = imageWidth * imageHeight;
        double grayPixelsValue = 0.0;
        for(int i =0; i < imageHeight;i++){
            for(int j =0; j< imageWidth;j++){
                Color curColor = image.getPixel(i,j);
                grayPixelsValue += (curColor.getRed() * RED_TO_GRAY_FACTOR  + curColor.getGreen()
                                                                              * GREEN_TO_GRAY_FACTOR  +
                                    curColor.getBlue() * BLUE_TO_GRAY_FACTOR);
            }
        }
        return grayPixelsValue / (numOfPixels * MAX_RGB);
    }
}
