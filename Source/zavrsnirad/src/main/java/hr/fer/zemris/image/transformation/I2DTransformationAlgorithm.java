package hr.fer.zemris.image.transformation;

import java.awt.image.BufferedImage;

/**
 * Interface defines 2D transformation on buffered image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public interface I2DTransformationAlgorithm {

    /**
     * Transformation that transforms given image.
     *
     * @param image
     *            image to be transformed
     * @return transformed image
     */
    BufferedImage transform(BufferedImage image);
}
