package hr.fer.zemris.image.binary;

import hr.fer.zemris.image.geometry.Point;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

/**
 * Interface that defines binary image. Positive x axis is from upper left corner to upper right corner and y axis is
 * from upper left corner to lower left corner. White is defined as true and black is defined as false.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 26.5.2017.
 */
public interface IBinaryImage extends Serializable {

    // TODO define black and white
    /**
     * Method obtains images height.
     *
     * @return images height
     */
    int getHeight();

    /**
     * Method obtains images pixels with given coordinates.
     *
     * @param x
     *            pixel x coordinate
     * @param y
     *            pixel y coordinate
     * @return pixel value on given location
     */
    boolean getPixel(int x, int y);

    /**
     * Method obtains pixel with given point coordinates.
     *
     * @param p
     *            pixel point
     * @return pixel value
     */
    default boolean getPixel(Point p) {
        return getPixel(p.getX(), p.getY());
    }

    /**
     * Method obtains all pixels in one double array where white represents 1 and black represents 0.
     *
     * @return array pixels represented as array with 0 and 1 as values
     */
    double[] getPixels();

    /**
     * Method obtains coordinates of pixels with given boolean value.
     *
     * @param pixelValue
     *            pixel value
     * @return list of pixel points
     */
    List<Point> getPixels(boolean pixelValue);

    /**
     * Method obtains subimage view of current image.
     *
     * @param x
     *            start x coordinate of an subimage
     * @param y
     *            start y coordinate of an subimage
     * @param width
     *            width of a subimage
     * @param height
     *            height of a subimage
     * @return subimage view of current image
     */
    IBinaryImage getSubimage(int x, int y, int width, int height);

    /**
     * Method obtains images width.
     *
     * @return width of an image
     */
    int getWidth();

    /**
     * Method transforms binary image to buffered image.
     *
     * @return buffered image representation of a binary image
     */
    BufferedImage toImage();
}
