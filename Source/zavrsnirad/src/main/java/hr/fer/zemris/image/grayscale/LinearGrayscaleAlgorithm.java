package hr.fer.zemris.image.grayscale;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;

/**
 * Class implements algorithm for grayscaling that uses linear grayscaling tehnique implemented in java jdk 1.8.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class LinearGrayscaleAlgorithm implements IGrayscaleAlgorithm {

    @Override
    public BufferedImage toGrayscale(BufferedImage source) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

        return op.filter(source, null);
    }

    @Override
    public String toString() {
        return "Linear";
    }
}
