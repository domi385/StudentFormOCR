package hr.fer.zemris.image.transformation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Transformation that rotates given image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class RotateImageCenterAlgorithm implements I2DTransformationAlgorithm {

    /**
     * Rotation angle.
     */
    private double angle;

    /**
     * Constructor that initializes rotation transformation with given angle.
     *
     * @param angle
     *            rotation angle
     */
    public RotateImageCenterAlgorithm(double angle) {
        this.angle = angle;
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        // TODO
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = (Graphics2D) rotatedImage.getGraphics();
        // g2d.rotate(angle, width / 2, height / 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        AffineTransform xform = AffineTransform.getRotateInstance(angle, width / 2., height / 2.);
        g2d.transform(xform);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

}
