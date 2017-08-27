package hr.fer.zemris.image.geometry;

/**
 * Utility class for calculating geometrical relations.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class GeometryUtility {

    /**
     * Method calculates angle between two lines by subtracting angle from x axis of first line from angle of second
     * line from x axis.
     *
     * @param firstLine
     *            first line
     * @param secondLine
     *            second line
     * @return angle between lines
     * @throws
     */
    public static double getAngleBetweenLines(Line firstLine, Line secondLine) {
        if (firstLine == null || secondLine == null) {
            throw new IllegalArgumentException("Given lines must not be null.");
        }
        double firstLineAngle = getLineAngle(firstLine);
        double secondLineAngle = getLineAngle(secondLine);

        return firstLineAngle - secondLineAngle;
    }

    /**
     * Method obtains line angle from x axis.
     *
     * @param line
     *            line for which angle should be calculated
     * @return angle between line and x axis
     */
    public static double getLineAngle(Line line) {
        int dx = line.getP2().getX() - line.getP1().getX();
        int dy = line.getP2().getY() - line.getP1().getY();

        double angle = Math.atan2(dy, dx);
        return angle < 0 ? angle + 2 * Math.PI : angle;
    }

    /**
     * Private utility class constructor.
     */
    private GeometryUtility() {
    }
}
