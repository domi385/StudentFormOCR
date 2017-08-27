package hr.fer.zemris.image.transformation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Class implements scaling transformation algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class ScaleAlgorithm implements I2DTransformationAlgorithm {

    /**
     * Scaling factor.
     */
    private double scaleFactor;

    /**
     * Constructor initializes scaling algorithm with scaling factor.
     *
     * @param scaleFactor
     *            scaling factor
     */
    public ScaleAlgorithm(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        double parameter = scaleFactor;
        int newWidth = (int) Math.ceil(image.getWidth() * parameter);
        int newHeight = (int) Math.ceil(image.getHeight() * parameter);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        return image;
    }

}
