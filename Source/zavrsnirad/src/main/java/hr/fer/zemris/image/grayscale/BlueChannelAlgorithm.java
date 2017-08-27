package hr.fer.zemris.image.grayscale;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Algorithm selects blue channel intensity as gray intensity.
 *
 * @author Domagoj Pluscec
 * @version v1.0 20.5.2017.
 */
public class BlueChannelAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage original) {

        int alpha, blue;
        int newPixel;
        BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                // get alpha and blue channel
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                blue = new Color(original.getRGB(i, j)).getBlue();
                // Return back to original format
                newPixel = ImageUtility.colorToRGB(alpha, blue, blue, blue);
                // Write pixels into image
                rgb.setRGB(i, j, newPixel);
            }
        }

        return rgb;
    }

    @Override
    public String toString() {
        return "Blue channel";
    }
}
