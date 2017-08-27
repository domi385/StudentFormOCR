package hr.fer.zemris.image.grayscale;

import hr.fer.zemris.image.ImageUtility;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Algorithm uses desaturatization method for grayscaling image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class DesaturationAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage original) {
        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage des = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] desLUT = new int[511];
        for (int i = 0; i < desLUT.length; i++) {
            desLUT[i] = i / 2;
        }

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

                int newval = ImageUtility.findMax(pixel) + ImageUtility.findMin(pixel);
                newval = desLUT[newval];

                // Return back to original format
                newPixel = ImageUtility.colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                des.setRGB(i, j, newPixel);

            }
        }

        return des;
    }

    @Override
    public String toString() {
        return "Desaturization";
    }
}
