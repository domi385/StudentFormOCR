package hr.fer.zemris.image.geometry;

import java.io.Serializable;

/**
 * Class models a geometrical line.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 28.4.2017.
 */
public class Line implements Serializable {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Start point.
     */
    private Point p1;
    /**
     * End point.
     */
    private Point p2;

    /**
     * Line length if it's calculated, otherwise null.
     */
    private Integer length;

    /**
     * Constructor that initializes line with two points.
     *
     * @param p1
     *            starting point
     * @param p2
     *            ending point
     */
    public Line(Point p1, Point p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Method calculates line length.
     *
     * @return length of a line
     */
    public Integer calcLength() {
        if (length != null) {
            return length;
        }
        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();
        length = (int) Math.hypot(dx, dy);
        return length;
    }

    /**
     * Method obtains line copy.
     *
     * @return copy of a line
     */
    public Line copy() {
        return new Line(p1.copy(), p2.copy());
    }

    /**
     * Two lines are equal if they have same starting and ending point.
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
        Line other = (Line) obj;
        if (p1 == null) {
            if (other.p1 != null) {
                return false;
            }
        } else if (!p1.equals(other.p1)) {
            return false;
        }
        if (p2 == null) {
            if (other.p2 != null) {
                return false;
            }
        } else if (!p2.equals(other.p2)) {
            return false;
        }
        return true;
    }

    /**
     * Method obtains copy of a first line point.
     *
     * @return line starting point
     */
    public Point getP1() {
        return p1.copy();
    }

    /**
     * Method obtains copy of an ending line point.
     *
     * @return line ending point
     */
    public Point getP2() {
        return p2.copy();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
        result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
        return result;
    }

    /**
     * Method returns string representation in form of "p1 - p2", where p1 and p2 are point string representations.
     */
    @Override
    public String toString() {
        return p1 + "-" + p2;

    }

}
