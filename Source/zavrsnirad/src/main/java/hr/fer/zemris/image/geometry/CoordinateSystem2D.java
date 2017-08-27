package hr.fer.zemris.image.geometry;

import java.io.Serializable;

/**
 * Class models a 2D coordinate system.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 28.4.2017.
 */
public class CoordinateSystem2D implements Serializable {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Coordiante origin.
     */
    private Point origin;
    /**
     * Line that connects origin with a point on x axis.
     */
    private Line x;
    /**
     * Line that connects origin with a point on y axis.
     */
    private Line y;

    /**
     * Constructor that initializes coordinate system.
     *
     * @param origin
     *            coordiante system origin
     * @param x
     *            line that connects origin with a point on x axis
     * @param y
     *            line that connects origin with a point on y axis
     */
    public CoordinateSystem2D(Point origin, Line x, Line y) {
        super();
        this.origin = origin;
        this.x = x;
        this.y = y;
    }

    /**
     * Method obtains coordinate system origin.
     *
     * @return coordinate system origin
     */
    public Point getOrigin() {
        return origin.copy();
    }

    /**
     * Method obtains coordinate system x axis line.
     *
     * @return line that connects origin with point on x axis
     */
    public Line getX() {
        return x.copy();
    }

    /**
     * Method obtains coordinate system y axis line.
     *
     * @return line that connects origin with point on y axis
     */
    public Line getY() {
        return y.copy();
    }

    @Override
    public String toString() {
        return "origin: " + origin + ", x axis: " + x + ", y axis: " + y;
    }

}
