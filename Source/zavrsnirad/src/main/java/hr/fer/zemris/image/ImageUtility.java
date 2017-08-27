package hr.fer.zemris.image;

import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.binary.BinaryImageUtility;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * https://bostjan-cigan.com/java-color-image-to-grayscale-conversion-algorithms /
 * https://bostjan-cigan.com/java-image-binarization-using-otsus-algorithm/
 * http://www.tannerhelland.com/3643/grayscale-image-algorithm-vb6/
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class ImageUtility {

    /**
     * Method converts values of RGBA channels to a one integer representation, where each channel is represented with
     * one byte. See {@link http://www.easyrgb.com/index.php?X=MATH&H=18} for more details.
     *
     * @param alpha
     *            alpha channel value from 0 to 255
     * @param red
     *            red channel value from 0 to 255
     * @param green
     *            green channel value from 0 to 255
     * @param blue
     *            blue channel value from 0 to 255
     * @return integer representation of ARGB
     */
    public static int colorToRGB(int alpha, int red, int green, int blue) {
        final int colorBitSize = 8;
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << colorBitSize;
        newPixel += red;
        newPixel = newPixel << colorBitSize;
        newPixel += green;
        newPixel = newPixel << colorBitSize;
        newPixel += blue;

        return newPixel;

    }

    public static IBinaryImage defalutToBinary(BufferedImage image) {
        return toBinary(image, new AverageAlgorithm(), new GlobalThresholdAlgorithm(250));
    }

    public static int findMax(int[] pixel) {

        int max = pixel[0];

        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] > max) {
                max = pixel[i];
            }
        }

        return max;

    }

    public static int findMin(int[] pixel) {

        int min = pixel[0];

        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] < min) {
                min = pixel[i];
            }
        }

        return min;

    }

    /**
     * Method calculates histogram on vertical axis of given image.
     *
     * @param image
     *            binarized image
     * @return histogram of pixels on image height
     */
    public static int[] calcVerticalHistogram(BufferedImage image) {
        IBinaryImage img = ImageUtility.defalutToBinary(image);
        return BinaryImageUtility.calcVerticalHistogram(img);
    }

    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }

        return histogram;

    }

    /**
     * Method applies given grayscale and binarization algorithms to produce binarized image.
     *
     * @param image
     *            original buffered image
     * @param grayAlgorithm
     *            grayscaling algorithm
     * @param binaryAlgorithm
     *            binarization algorithm
     * @return binarized image
     */
    public static IBinaryImage toBinary(BufferedImage image, IGrayscaleAlgorithm grayAlgorithm,
            IBinarizationAlgorithm binaryAlgorithm) {
        return new BinaryImage(binaryAlgorithm.toBinary(grayAlgorithm.toGrayscale(image)));
    }

    /**
     * Private utility class constructor.
     */
    private ImageUtility() {
    }

}
