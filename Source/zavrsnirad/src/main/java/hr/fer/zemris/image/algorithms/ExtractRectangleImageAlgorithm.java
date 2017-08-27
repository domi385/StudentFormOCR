package hr.fer.zemris.image.algorithms;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Method obtains rectangle subimage of an image and returns a copy of it.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 11.6.2017.
 */
public class ExtractRectangleImageAlgorithm {

    /**
     * Method extracts copy of a subimage defined with rectangle.
     *
     * @param image
     *            image from which to extract rectangle
     * @param rectangle
     *            subimage positions
     * @return extracted region copy
     */
    public static BufferedImage extract(BufferedImage image, Rectangle rectangle) {

        BufferedImage extracedImage = image.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        BufferedImage copyOfImage = new BufferedImage(extracedImage.getWidth(), extracedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(extracedImage, 0, 0, null);
        return copyOfImage;
    }

    /**
     * Private utility class constructor.
     */
    private ExtractRectangleImageAlgorithm() {
    }

}
