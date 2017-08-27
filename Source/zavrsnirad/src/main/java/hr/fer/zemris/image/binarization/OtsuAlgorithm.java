package hr.fer.zemris.image.binarization;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Binarization algorithm that uses otsu binarization method.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class OtsuAlgorithm implements IBinarizationAlgorithm {

    /**
     * Method calculates threshold using Otsu's method.
     *
     * @param original
     *            original image
     * @return otsu threshold
     */
    private static int otsuTreshold(BufferedImage original) {

        int[] histogram = ImageUtility.imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        final int maxIntensityLength = 256;
        for (int i = 0; i < maxIntensityLength; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < maxIntensityLength; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;

            if (wF == 0) {
                break;
            }

            sumB += i * histogram[i];
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }

    @Override
    public BufferedImage toBinary(BufferedImage original) {

        int red;
        int newPixel;

        int threshold = otsuTreshold(original);
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        final int maxIntensity = 255;
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = maxIntensity;
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
        return "Otsu";
    }

}
