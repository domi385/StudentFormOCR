package hr.fer.zemris.image.binarization;

import java.awt.image.BufferedImage;

/**
 * Empty binarizaion algorithm that returns given image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class EmptyBinarizationAlgorithm implements IBinarizationAlgorithm {

    @Override
    public BufferedImage toBinary(BufferedImage source) {
        return source;
    }

}
