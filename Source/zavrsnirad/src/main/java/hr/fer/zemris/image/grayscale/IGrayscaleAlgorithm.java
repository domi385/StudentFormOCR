package hr.fer.zemris.image.grayscale;

import java.awt.image.BufferedImage;

/**
 * Interface of grayscale algorithm that can transform buffered image to grayscale buffered image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 30.4.2017.
 */
public interface IGrayscaleAlgorithm {

    /**
     * Method transforms source buffered image to new grayscale buffered image.
     *
     * @param source
     *            buffered image to be transformed
     * @return grayscale buffered image
     */
    BufferedImage toGrayscale(BufferedImage source);

    @Override
    String toString();
}
