package hr.fer.zemris;

/**
 * Class that provides utility functions for testing.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 17.3.2017.
 *
 */
public class TestUtility {

    /**
     * Double comparison constant.
     */
    public static final double DOUBLE_COMPARISON_CONSTANT = 1e-6;

    /**
     * Method compares two double values.
     *
     * @param first
     *            first value
     * @param second
     *            second value
     * @return true if the values are same to the double comparison constant, false otherwise
     */
    public static boolean compareDoubles(double first, double second) {
        return Math.abs(first - second) < DOUBLE_COMPARISON_CONSTANT;
    }

    /**
     * Private utility class constructor.
     */
    private TestUtility() {
    }

}
