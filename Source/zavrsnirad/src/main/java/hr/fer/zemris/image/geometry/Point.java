package hr.fer.zemris.image.geometry;

import java.io.Serializable;

/**
 * Class models a geometrical point.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 28.4.2017.
 */
public class Point implements Serializable {
    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * x coordinate of a point.
     */
    private int x;
    /**
     * y coordinate of a point.
     */
    private int y;

    /**
     * Constructor construct point with x and y coordinate.
     *
     * @param x
     *            x coordinate of a point
     * @param y
     *            y coordinate of a point
     */
    public Point(int x, int y) {
        super();
        this.setX(x);
        this.setY(y);
    }

    /**
     * Method creates copy of a point.
     *
     * @return point's copy
     */
    public Point copy() {
        return new Point(x, y);
    }

    /**
     * Two points are equal if their coordinates are equal.
     */
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
        Point other = (Point) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    /**
     * Method obtains x coordinate of a point.
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Method obtains y coordinate of a point.
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Method sets point location.
     *
     * @param x
     *            point x coordinate
     * @param y
     *            point y coordinate
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method sets point x coordinate.
     *
     * @param x
     *            new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Method sets point y coordinate.
     *
     * @param y
     *            new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * String representation in form of (x, y) where x and y are point coordinates.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
