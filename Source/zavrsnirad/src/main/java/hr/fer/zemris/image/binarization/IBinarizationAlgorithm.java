package hr.fer.zemris.image.binarization;

import java.awt.image.BufferedImage;

/**
 * Interface of binarization algorithm that can transform buffered image to binarized buffered image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 30.4.2017.
 */
public interface IBinarizationAlgorithm {
    /**
     * Method transforms source buffered image to new binarized buffered image.
     *
     * @param source
     *            buffered image to be binarized
     * @return binarized buffered image
     */
    BufferedImage toBinary(BufferedImage source);

    @Override
    String toString();
}
