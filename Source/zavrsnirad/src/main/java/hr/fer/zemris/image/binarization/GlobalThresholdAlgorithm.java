package hr.fer.zemris.image.binarization;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Binarization algorithm that uses fixed global threshold.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class GlobalThresholdAlgorithm implements IBinarizationAlgorithm {

    /**
     * Threshold value.
     */
    private int threshold;

    /**
     * Constructor initializes algorithm with given threshold value.
     *
     * @param threshold
     *            threshold value
     */
    public GlobalThresholdAlgorithm(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public BufferedImage toBinary(BufferedImage original) {

        int red;
        int newPixel;

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        final int maxPixelValue = 255;
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = maxPixelValue;
                } else {
                    newPixel = 0;
                }
                newPixel = ImageUtility.colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);

            }
        }

        return binarized;

    }

    @Override
    public String toString() {
        return "Global threshold " + threshold;
    }
}
