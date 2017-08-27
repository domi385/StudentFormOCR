package hr.fer.zemris.image.geometry;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 * Method models rectangle region edge.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class RectangleRegionEdge extends RegionEdge {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Region rectangle.
     */
    private Rectangle regionRectangle;

    /**
     * Constructor that initializes region edge with rectangle.
     *
     * @param rectangle
     *            rectangle describing region edge
     */
    public RectangleRegionEdge(Rectangle rectangle) {
        this.regionRectangle = rectangle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RectangleRegionEdge other = (RectangleRegionEdge) obj;
        if (regionRectangle == null) {
            if (other.regionRectangle != null) {
                return false;
            }
        } else if (!regionRectangle.equals(other.regionRectangle)) {
            return false;
        }
        return true;
    }

    @Override
    public Rectangle getBoundingRectange() {
        // TODO copy
        return regionRectangle;
    }

    @Override
    public Point getBoundingRectangleCenter() {
        return new Point(regionRectangle.x + regionRectangle.width / 2, regionRectangle.y + regionRectangle.height / 2);

    }

    @Override
    public Dimension getDim() {
        return new Dimension(regionRectangle.width, regionRectangle.height);
    }

    @Override
    public Set<Point> getPoints() {
        Set<Point> edgePoints = new HashSet<>();
        for (int x = regionRectangle.x, endX = regionRectangle.x + regionRectangle.width; x < endX; x++) {
            for (int y = regionRectangle.y, endY = regionRectangle.y + regionRectangle.height; y < endY; y++) {
                edgePoints.add(new Point(x, y));
            }
        }
        return edgePoints;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((regionRectangle == null) ? 0 : regionRectangle.hashCode());
        return result;
    }

}
