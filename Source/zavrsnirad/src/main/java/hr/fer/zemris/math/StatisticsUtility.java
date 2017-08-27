package hr.fer.zemris.math;

/**
 * Utility class for calculating simple statistical features.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class StatisticsUtility {

    /**
     * Method calculates mean value of given integer array.
     *
     * @param xs
     *            integer array
     * @return average value of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */
    public static double mean(int[] xs) throws IllegalArgumentException {
        if (xs == null || xs.length == 0) {
            throw new IllegalArgumentException("Cannot calculate mean value of empty array.");
        }
        double sum = 0;
        for (int i = 0; i < xs.length; i++) {
            sum += xs[i];
        }
        return sum / xs.length;
    }

    /**
     * Method calculates standard deviation of given integer array.
     *
     * @param xs
     *            integer array
     * @return standard deviation of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */
    public static double standardDeviation(int[] xs) throws IllegalArgumentException {
        return Math.sqrt(variance(xs));
    }

    /**
     * Method calculates variance of given integer array.
     *
     * @param xs
     *            integer array
     * @return variance of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */
    public static double variance(int[] xs) throws IllegalArgumentException {
        if (xs == null || xs.length == 0) {
            throw new IllegalArgumentException();
        }
        double avg = mean(xs);

        double sum = 0;
        for (int i = 0; i < xs.length; i++) {
            sum += Math.pow(avg - xs[i], 2);
        }
        return sum / xs.length;
    }

    /**
     * Private utility class constructor.
     */
    private StatisticsUtility() {
    }

    /**
     * Method calculates standard deviation of given double array.
     *
     * @param xs
     *            double array
     * @return standard deviation of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */
    public static double standardDeviation(double[] xs) throws IllegalArgumentException {
        return Math.sqrt(variance(xs));
    }

    /**
     * Method calculates mean value of given double array.
     *
     * @param xs
     *            double array
     * @return average value of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */

    public static double mean(double[] xs) {
        if (xs == null || xs.length == 0) {
            throw new IllegalArgumentException("Cannot calculate mean value of empty array.");
        }
        double sum = 0;
        for (int i = 0; i < xs.length; i++) {
            sum += xs[i];
        }
        return sum / xs.length;

    }

    /**
     * Method calculates variance of given double array.
     *
     * @param xs
     *            double array
     * @return variance of given array
     * @throws IllegalArgumentException
     *             if given array has no elements
     */
    public static double variance(double[] xs) throws IllegalArgumentException {
        if (xs == null || xs.length == 0) {
            throw new IllegalArgumentException();
        }
        double avg = mean(xs);

        double sum = 0;
        for (int i = 0; i < xs.length; i++) {
            sum += Math.pow(avg - xs[i], 2);
        }
        return sum / xs.length;
    }

}
