package hr.fer.zemris.image.transformation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Transformation that normalizes image inside a square.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class ScaleInSquareAlgorithm implements I2DTransformationAlgorithm {

    /**
     * Square dimension.
     */
    private int squareDimension;

    /**
     * Constructor that initializes scale in square transformation.
     *
     * @param squareDimension
     *            square dimension
     */
    public ScaleInSquareAlgorithm(int squareDimension) {
        super();
        this.squareDimension = squareDimension;
    }

    @Override
    public BufferedImage transform(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int maxDim = Integer.max(image.getWidth(), image.getHeight());
        double scalingFactor = ((double) squareDimension) / maxDim;
        int newHeight = (int) Math.round(height * scalingFactor);
        int newWidth = (int) Math.round(width * scalingFactor);

        int heightOffset = (squareDimension - newHeight) / 2;
        int widthOffset = (squareDimension - newWidth) / 2;

        BufferedImage resized = new BufferedImage(squareDimension, squareDimension, image.getType());
        Graphics2D g = resized.createGraphics();
        g.setPaint(Color.WHITE);
        g.setBackground(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, widthOffset, heightOffset, newWidth + widthOffset, newHeight + heightOffset, 0, 0, width,
                height, null);
        g.fillRect(0, 0, widthOffset + 1, newHeight + 1);
        g.fillRect(newWidth + widthOffset, 0, widthOffset + 1, newHeight + 1);
        g.fillRect(0, 0, heightOffset + 1, newWidth + 1);
        g.fillRect(0, newHeight + heightOffset, newWidth + 1, heightOffset + 1);

        g.dispose();

        return resized;

    }
}
