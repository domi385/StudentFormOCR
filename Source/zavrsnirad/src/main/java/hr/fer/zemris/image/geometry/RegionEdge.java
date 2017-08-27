package hr.fer.zemris.image.geometry;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class models region edge defined with points.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class RegionEdge implements IRegionEdge {
    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Points contained in region edge.
     */
    private Set<Point> edgePoints;
    /**
     * Dimensions of bound rectangle of region edge.
     */
    private Dimension dim;
    /**
     * Minimal and maximal x coordinates of region edge.
     */
    private int minX, maxX;
    /**
     * Minimal and maximal y coordinates of region edge.
     */
    private int minY, maxY;

    /**
     * Constructor that initializes model.
     */
    protected RegionEdge() {
        // TODO remove and implement abstract region edge
    }

    /**
     * Method initializes region edge with a collection of points.
     *
     * @param edgePoints
     *            edge points collection
     */
    public RegionEdge(Collection<Point> edgePoints) {
        this.edgePoints = new HashSet<>();
        this.edgePoints.addAll(edgePoints);
        calcRegionDimension();
    }

    /**
     * Method calculates region edge dimension.
     */
    private void calcRegionDimension() {
        int xMax, xMin;
        int yMax, yMin;
        Point tempSetPoint = edgePoints.iterator().next();
        xMax = xMin = tempSetPoint.getX();
        yMax = yMin = tempSetPoint.getY();
        for (Point p : edgePoints) {
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
        this.dim = new Dimension(Math.abs(xMax - xMin), Math.abs(yMax - yMin));
        this.maxX = xMax;
        this.maxY = yMax;
        this.minX = xMin;
        this.minY = yMin;
    }

    @Override
    public int compareTo(RegionEdge o) {
        return Integer.valueOf(this.getDim().width * this.getDim().height).compareTo(
                o.getDim().width * o.getDim().height);
    }

    @Override
    public boolean contains(Point point) {
        Rectangle rect = getBoundingRectange();
        if (point.getX() < rect.x || point.getX() > rect.x + rect.width) {
            return false;
        }
        if (point.getY() < rect.y || point.getY() > rect.y + rect.height) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RegionEdge other = (RegionEdge) obj;
        if (dim == null) {
            if (other.dim != null) {
                return false;
            }
        } else if (!dim.equals(other.dim)) {
            return false;
        }
        if (edgePoints == null) {
            if (other.edgePoints != null) {
                return false;
            }
        } else if (!edgePoints.equals(other.edgePoints)) {
            return false;
        }
        if (maxX != other.maxX) {
            return false;
        }
        if (maxY != other.maxY) {
            return false;
        }
        if (minX != other.minX) {
            return false;
        }
        if (minY != other.minY) {
            return false;
        }
        return true;
    }

    @Override
    public Rectangle getBoundingRectange() {
        return new Rectangle(minX, minY, dim.width, dim.height);
    }

    @Override
    public Point getBoundingRectangleCenter() {
        return new Point(minX + getDim().width / 2, minY + getDim().height / 2);
    }

    @Override
    public Dimension getDim() {
        return dim;
    }

    @Override
    public Set<Point> getPoints() {
        return Collections.unmodifiableSet(this.edgePoints);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dim == null) ? 0 : dim.hashCode());
        result = prime * result + ((edgePoints == null) ? 0 : edgePoints.hashCode());
        result = prime * result + maxX;
        result = prime * result + maxY;
        result = prime * result + minX;
        result = prime * result + minY;
        return result;
    }

}
