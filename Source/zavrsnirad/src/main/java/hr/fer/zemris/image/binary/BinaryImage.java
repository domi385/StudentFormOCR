package hr.fer.zemris.image.binary;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Standard implementation of binary image that holds boolean array for storing pixels.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 26.5.2017.
 */
public class BinaryImage extends AbstractBinaryImage {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Internal representation of pixels.
     */
    private boolean[][] pixels;

    /**
     * Image height.
     */
    private int height;

    /**
     * Image width.
     */
    private int width;

    /**
     * Constructor that initializes binary image from boolean field.
     *
     * @param pixels
     *            image pixels
     * @param copy
     *            if true given array will be copied, otherwise it will be used as given
     */
    public BinaryImage(boolean[][] pixels, boolean copy) {
        if (copy) {
            boolean[][] pixelsCopy = new boolean[pixels.length][pixels[0].length];
            for (int i = 0; i < pixels.length; i++) {
                pixelsCopy[i] = Arrays.copyOf(pixels[i], pixels[0].length);
            }
            pixels = pixelsCopy;
        }
        this.pixels = pixels;
        this.height = pixels.length;
        this.width = pixels[0].length;

    }

    /**
     * Constructor that initializes binary image from binarized buffered image.
     *
     * @param binarizedImage
     *            binarized buffered image
     */
    public BinaryImage(BufferedImage binarizedImage) {
        if (binarizedImage == null) {
            throw new IllegalArgumentException();
        }
        initializePixelsField(binarizedImage);
        height = binarizedImage.getHeight();
        width = binarizedImage.getWidth();
    }

    /**
     * Method transforms integer RGB value to boolean.
     *
     * @param rgb
     *            integer containing rgb values as 1 byte for each channel
     * @return true for white pixel, false for black pixel
     */
    private boolean booleanFromRGB(int rgb) {
        final int maxSumThreshold = 255 * 3 / 2;
        final int rgbMask = 0x00FFFFFF;
        final int rMask = 0x00FF0000;
        final int rOffset = 16;
        final int gMask = 0x0000FF00;
        final int gOffset = 8;
        final int bMask = 0x000000FF;
        rgb = rgb & rgbMask;
        int r = (rgb & rMask) >> rOffset;
            int g = (rgb & gMask) >> gOffset;
            int b = rgb & bMask;
            int sum = r + g + b;

            if (sum > maxSumThreshold) {
                return true;
            }
            return false;

    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean getPixel(int x, int y) {
        return pixels[y][x];
    }

    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Method initializes pixels from buffered image.
     *
     * @param image
     *            binarized buffered image
     */
    private void initializePixelsField(BufferedImage image) {
        pixels = new boolean[image.getHeight()][image.getWidth()];
        for (int i = 0, endHeight = image.getHeight(); i < endHeight; i++) {
            for (int j = 0, endWidth = image.getWidth(); j < endWidth; j++) {
                pixels[i][j] = booleanFromRGB(image.getRGB(j, i));

            }
        }
    }

}
