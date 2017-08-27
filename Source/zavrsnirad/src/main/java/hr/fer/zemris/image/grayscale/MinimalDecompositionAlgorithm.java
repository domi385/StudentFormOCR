package hr.fer.zemris.image.grayscale;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Algorithm uses minimal channel intensity of every pixel.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class MinimalDecompositionAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage original) {

        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = ImageUtility.findMin(pixel);

                // Return back to original format
                newPixel = ImageUtility.colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                decomp.setRGB(i, j, newPixel);

            }
        }

        return decomp;
    }

    @Override
    public String toString() {
        return "Minimal decomposition";
    }

}
