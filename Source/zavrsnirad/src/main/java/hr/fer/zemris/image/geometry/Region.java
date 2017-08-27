package hr.fer.zemris.image.geometry;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class models image region.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class Region implements Serializable {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Points contained in a region.
     */
    private Set<Point> regionPoints;

    /**
     * Center of a region.
     */
    private Point regionCenter;

    /**
     * Region bounding rectangle.
     */
    private Rectangle boundingRectangle;

    /**
     * Constructor initializes region with points contained in a region.
     *
     * @param regionPoints
     *            collection of points contained in a region
     */
    public Region(Collection<Point> regionPoints) {
        this.regionPoints = new HashSet<>();
        this.regionPoints.addAll(regionPoints);
    }

    /**
     * Method obtains region bounding rectangle.
     *
     * @return region bounding rectangle.
     */
    public Rectangle getBoundingRectangle() {
        if (boundingRectangle != null) {
            return boundingRectangle;
        }
        int xMax, xMin;
        int yMax, yMin;
        Point tempSetPoint = regionPoints.iterator().next();
        xMax = xMin = tempSetPoint.getX();
        yMax = yMin = tempSetPoint.getY();
        for (Point p : regionPoints) {
            if (p.getX() > xMax) {
                xMax = p.getX();
            }
            if (p.getX() < xMin) {
                xMin = p.getX();
            }
            if (p.getY() > yMax) {
                yMax = p.getY();
            }
            if (p.getY() < yMin) {
                yMin = p.getY();
            }
        }
        boundingRectangle = new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
        return boundingRectangle;
    }

    /**
     * Method obtains unmodifiable view of region points.
     *
     * @return unmodifiable view of region points
     */
    public Set<Point> getPoints() {

        return Collections.unmodifiableSet(regionPoints);
    }

    /**
     * Method obtains center point of a region.
     *
     * @return center of mass of a region
     */
    public Point getRegionCenter() {
        if (regionCenter != null) {
            return regionCenter;
        }
        if (regionPoints.size() == 0) {
            throw new UnsupportedOperationException("Cannot calculate center when there are no points.");
        }
        int sumX, sumY;
        sumX = sumY = 0;
        for (Point p : regionPoints) {
            sumX += p.getX();
            sumY += p.getY();
        }
        regionCenter = new Point(sumX / regionPoints.size(), sumY / regionPoints.size());
        return regionCenter;
    }

}
