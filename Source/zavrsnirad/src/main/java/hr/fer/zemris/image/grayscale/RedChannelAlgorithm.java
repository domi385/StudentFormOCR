package hr.fer.zemris.image.grayscale;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Algorithm selects red channel intensity as gray intensity.
 *
 * @author Domagoj Pluscec
 * @version v1.0 20.5.2017.
 */
public class RedChannelAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage original) {

        int alpha, red;
        int newPixel;

        BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();

                int newval = red;

                // Return back to original format
                newPixel = ImageUtility.colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                rgb.setRGB(i, j, newPixel);

            }

        }

        return rgb;
    }

    @Override
    public String toString() {
        return "Red channel";
    }
}
