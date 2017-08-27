package hr.fer.zemris.image.grayscale;

import java.awt.image.BufferedImage;

/**
 * Empty implementation of a grayscaling algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 6.9.2017.
 */
public class EmptyGrayscaleAlgorithm implements IGrayscaleAlgorithm {

    /**
     * Constructor that initializes grayscale algorithm.
     */
    public EmptyGrayscaleAlgorithm() {
    }

    @Override
    public BufferedImage toGrayscale(BufferedImage source) {
        return source;
    }

    @Override
    public String toString() {
        return "No grayscale algorithm";
    }
}
