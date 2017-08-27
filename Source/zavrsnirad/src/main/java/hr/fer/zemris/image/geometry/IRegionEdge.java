package hr.fer.zemris.image.geometry;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Set;

/**
 * Interface that models region edge.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public interface IRegionEdge extends Comparable<RegionEdge>, Serializable {

    /**
     * Method checks if region edge bounding rectangle contains a point.
     *
     * @param point
     *            point that needs to be checked
     * @return true if the point is contained in the bounding rectangle, false otherwise
     */
    boolean contains(Point point);

    /**
     * Method obtains region bounding rectangle.
     *
     * @return bounding rectangle
     */
    Rectangle getBoundingRectange();

    /**
     * Method obtains bounding rectangle center of a region.
     *
     * @return bounding rectangle center
     */
    Point getBoundingRectangleCenter();

    /**
     * Method obtains bounding rectangle dimensions.
     *
     * @return bounding rectangle dimension
     */
    Dimension getDim();

    /**
     * Method obtains set of points contained in a region edge.
     *
     * @return region edge points
     */
    Set<Point> getPoints();
}
