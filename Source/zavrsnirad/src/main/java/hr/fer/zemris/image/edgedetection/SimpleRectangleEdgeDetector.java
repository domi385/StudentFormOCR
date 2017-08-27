package hr.fer.zemris.image.edgedetection;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.RectangleRegionEdge;
import hr.fer.zemris.image.geometry.RegionEdge;

import java.awt.Rectangle;

/**
 * Class implements an algorithm for detecting rectangle region edge from given point.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class SimpleRectangleEdgeDetector {

    /**
     * Boolean value of background color.
     */
    private static final boolean BACKGROUND = false;

    /**
     * Boolean value of foreground color.
     */
    private static final boolean FOREGROUND = true;
    /**
     * Current binary image.
     */
    private IBinaryImage image;

    /**
     * Constructor that initalizes algorithm with an image.
     *
     * @param image
     *            image on which to detect edges
     */
    public SimpleRectangleEdgeDetector(IBinaryImage image) {
        this.image = image;
    }

    /**
     * Method obtains rectangle from given starting point.
     *
     * @param startPoint
     *            starting point.
     * @param regionColor
     *            region color
     * @return region rectangle edge
     */
    private Rectangle findRectangleParameters(Point startPoint, boolean regionColor) {
        boolean change = true;
        int xMin, xMax;
        xMin = xMax = startPoint.getX();
        int yMin, yMax;
        yMin = yMax = startPoint.getY();

        while (change) {
            change = false;
            int dxMin = xMin - 1;
            int dyMin = yMin - 1;
            int dxMax = xMax + 1;
            int dyMax = yMax + 1;

            if (dxMin > 0 && image.getPixel(dxMin, startPoint.getY()) == regionColor) {
                xMin = dxMin;
                change = true;
            }
            if (dyMin > 0 && image.getPixel(startPoint.getX(), dyMin) == regionColor) {
                yMin = dyMin;
                change = true;
            }

            if (dxMax < image.getWidth() && image.getPixel(dxMax, startPoint.getY()) == regionColor) {
                xMax = dxMax;
                change = true;
            }
            if (dyMax < image.getHeight() && image.getPixel(startPoint.getX(), dyMax) == regionColor) {
                yMax = dyMax;
                change = true;
            }

        }
        return new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);

    }

    /**
     * Method obtains edges of a region from given starting point.
     *
     * @param startPoint
     *            detection starting point
     * @param regionColor
     *            region color
     * @return region edge
     */
    public RegionEdge getRegionsEdge(Point startPoint, boolean regionColor) {
        if (image.getPixel(startPoint) != regionColor) {
            throw new IllegalArgumentException("Start point must be in region");
        }
        Rectangle regionRectangle = findRectangleParameters(startPoint, regionColor);

        return new RectangleRegionEdge(regionRectangle);

    }

}
