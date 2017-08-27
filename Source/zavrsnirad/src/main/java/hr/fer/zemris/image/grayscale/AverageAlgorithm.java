package hr.fer.zemris.image.grayscale;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Algorithm uses average of color channels for gray intensity.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class AverageAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage original) {
        int alpha, red, green, blue;
        int newPixel;

        BufferedImage avgGray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] avgLUT = new int[766];
        for (int i = 0; i < avgLUT.length; i++) {
            avgLUT[i] = i / 3;
        }

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                newPixel = red + green + blue;
                newPixel = avgLUT[newPixel];
                // Return back to original format
                newPixel = ImageUtility.colorToRGB(alpha, newPixel, newPixel, newPixel);

                // Write pixels into image
                avgGray.setRGB(i, j, newPixel);

            }
        }

        return avgGray;
    }

    @Override
    public String toString() {
        return "Average";
    }

}
