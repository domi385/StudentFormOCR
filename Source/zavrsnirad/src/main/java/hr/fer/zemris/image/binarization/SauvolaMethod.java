package hr.fer.zemris.image.binarization;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.math.StatisticsUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Class models Sauvola binarization algorithm for text binarization. Threshold is calculated by formula avg * (1 + k *
 * (dev / R - 1)).
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class SauvolaMethod implements IBinarizationAlgorithm {

    /**
     * R algorithm coefficient.
     */
    private static final double R = 128;
    /**
     * k algorithm coefficient.
     */
    private double k; // from 0.2 to 0.5
    /**
     * Neighborhood distance parameter.
     */
    private int radius = 2;
    /**
     * Minimal default value of k coefficient.
     */
    private static final double MIN_DEFAULT_K = 0.2;
    /**
     * Maximal default value of k coefficient.
     */
    private static final double MAX_DEFAULT_K = 0.5;

    /**
     * Sauvola binarization method constructor.
     *
     * @param k
     *            algorithm k coefficient
     * @param radius
     *            neighborhood radius
     */
    public SauvolaMethod(double k, int radius) {
        super();
        if (k < MIN_DEFAULT_K || k > MAX_DEFAULT_K) {
            System.err.println("Sauvola koeficient out of expected range");
        }
        this.k = k;
        this.radius = radius;
    }

    /**
     * Method calculates binarized value of a pixel.
     *
     * @param x
     *            pixels x coordiante
     * @param y
     *            pixels y coordinate
     * @param img
     *            image
     * @return pixel binarized value
     */
    private int binarize(int x, int y, BufferedImage img) {
        int[] xs = getWindow(img, x, y);
        int intensity = intensity(img, x, y);
        double thresholdVal = threshold(xs);

        final int maxIntensity = 255;
        if (intensity >= thresholdVal) {
            return maxIntensity;
        } else {
            return 0;
        }
    }

    /**
     * Method obtains window of pixels with current neighborhood size transformed into array.
     *
     * @param img
     *            image
     * @param x
     *            pixels x coordinate
     * @param y
     *            pixels y coordinate
     * @return array of pixel values in neighborhood window
     */
    private int[] getWindow(BufferedImage img, int x, int y) {
        int xMin = Math.max(0, x - radius);
        int xMax = Math.min(x + radius, img.getWidth() - 1);
        int yMin = Math.max(0, y - radius);
        int yMax = Math.min(y + radius, img.getHeight() - 1);

        int[] xs = new int[(xMax - xMin) * (yMax - yMin)];
        int k = 0;
        for (int i = xMin; i < xMax; i++) {
            for (int j = yMin; j < yMax; j++) {
                int intensity = intensity(img, i, j);

                xs[k] = intensity;
                k++;
            }
        }

        return xs;
    }

    /**
     * Method obtains pixel intensity by taking value of red channel.
     *
     * @param img
     *            image
     * @param x
     *            pixels' x coordinate
     * @param y
     *            pixels' y coordinate
     * @return intesity of a pixel
     */
    private int intensity(BufferedImage img, int x, int y) {
        return new Color(img.getRGB(x, y)).getRed();
    }

    /**
     * Method calculates threshold of an array according to Sauvola formula.
     *
     * @param xs
     *            array of pixel values
     * @return threshold value
     */
    private double threshold(int[] xs) {
        double avg = StatisticsUtility.mean(xs);
        double dev = StatisticsUtility.standardDeviation(xs);
        return avg * (1 + k * (dev / R - 1));
    }

    @Override
    public BufferedImage toBinary(BufferedImage original) {
        int newPixel;

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                newPixel = binarize(i, j, original);
                newPixel = ImageUtility.colorToRGB(alpha, newPixel, newPixel, newPixel);

                binarized.setRGB(i, j, newPixel);

            }
        }

        return binarized;
    }

    @Override
    public String toString() {
        return "Sauvola method k: " + k + ", r:" + radius;
    }
}
