package hr.fer.zemris.image.algorithms;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implements hough lines detecting algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 6.9.2017.
 */
public class HoughLinesAlgorithm {

    /**
     * Hough algorithm line result.
     *
     * @author Domagoj Pluscec
     * @version v1.0, 11.6.2017.
     */
    public static class Line {
        /**
         * Line segment radius distance.
         */
        private double rho;
        /**
         * Line segment angle.
         */
        private double theta;

        /**
         * Constructor that initializes line segment with radius distance and angle.
         *
         * @param rho
         *            radius distance
         * @param theta
         *            line segment angle
         */
        public Line(double rho, double theta) {
            super();
            this.rho = rho;
            this.theta = theta;
        }

        /**
         * Method obtains value of y coordinate on line that contains current line segment ith given x coordinate.
         *
         * @param x
         *            x coordinate of a point
         * @return y coordinate of a point on line
         */
        public int getY(int x) {
            int y = (int) (-Math.cos(theta) / Math.sin(theta) * x + rho / Math.sin(theta));
            return y;
        }

        @Override
        public String toString() {
            return "rho: " + rho + ", theta: " + theta;
        }
    }

    /**
     * Maximal line angle.
     */
    private static final int MAX_THETA = 180;

    /**
     * Theta search step.
     */
    private static final double THETA_STEP = Math.PI / 180;

    /**
     * Current image.
     */
    private IBinaryImage image;
    /**
     * List of found lines.
     */
    private ArrayList<Line> lines;

    /**
     * Method obtains detected lines.
     *
     * @return detected lines
     */
    public ArrayList<Line> getLines() {
        return lines;
    }

    /**
     * Algorithm accumulator.
     */
    private int[][] accumulator;

    /**
     * Cosines lookup table.
     */
    private double[] cosTable;
    /**
     * Sine lookup table.
     */
    private double[] sinTable;

    /**
     * The coordinates of the center of the image.
     */
    private double centerX, centerY;

    /**
     * The height of the Hough array.
     */
    private int houghHeight;

    /**
     * Double the Hough height (allows for negative numbers).
     */
    private int doubleHeight;

    //
    /**
     * The number of points that have been added.
     */
    private int numPoints;

    /**
     * The size of the neighbourhood in which to search for other local maxima.
     */
    private final int neighbourhoodSize = 4;

    /**
     * Constructor that initializes Hough algorithm.
     *
     * @param image
     *            image on which to search for lines
     */
    public HoughLinesAlgorithm(IBinaryImage image) {
        this.image = image;
        lines = new ArrayList<>();
        initializeTables();
        initializeAccumulator();
        detectLines();
    }

    /**
     * Method paints algorithms accumulator on an image.
     *
     * @return image representation of accumulator
     */
    public BufferedImage accumulatorToImage() {
        int[][] acc = accumulator;
        BufferedImage plot = new BufferedImage(acc.length, acc[0].length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = plot.getRaster();

        int maxValue = 0;
        for (int i = 0; i < acc.length; i++) {
            for (int j = 0; j < acc[i].length; j++) {
                if (acc[i][j] > maxValue) {
                    maxValue = acc[i][j];
                }
            }
        }

        final int maxIntensity = 255;
        for (int i = 0; i < acc.length; i++) {
            for (int j = 0; j < acc[0].length; j++) {
                int value = (int) (((double) acc[i][j]) / maxValue * maxIntensity);
                raster.setSample(i, j, 0, value);
            }
        }
        return plot;
    }

    /**
     * Method detects lines from accumulator.
     */
    private void addLines() {

        if (numPoints == 0) {
            return;
        }

        final int threshold = (int) (0.45 * findMax());

        for (int t = 0; t < MAX_THETA; t++) {
            for (int r = neighbourhoodSize; r < doubleHeight - neighbourhoodSize; r++) {
                if (accumulator[t][r] < threshold) {
                    continue;
                }

                int peak = accumulator[t][r];

                boolean truePeak = true;
                // Check that this peak is indeed the local maxima
                for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++) {
                    for (int dy = -neighbourhoodSize; dy <= neighbourhoodSize; dy++) {
                        int dt = t + dx;
                        int dr = r + dy;
                        if (dt < 0) {
                            dt = dt + MAX_THETA;
                        } else if (dt >= MAX_THETA) {
                            dt = dt - MAX_THETA;
                        }
                        if (accumulator[dt][dr] > peak) {
                            truePeak = false;
                            break;
                        }
                        if (!truePeak) {
                            break;
                        }
                    }
                }

                if (!truePeak) {
                    continue;
                }
                double theta = t * THETA_STEP;
                lines.add(new Line(r, Math.toDegrees(theta)));
            }

        }

    }

    /**
     * Method detects lines in initialized image.
     */
    private void detectLines() {

        List<Point> edges = getEdges();
        for (int i = 0; i < edges.size(); i++) {
            int x = edges.get(i).getX();
            int y = edges.get(i).getY();
            for (int t = 0; t < MAX_THETA; t++) {
                int rho = (int) (((x - centerX) * cosTable[t]) + ((y - centerY) * sinTable[t]));
                rho += houghHeight; // because of negatives

                if (rho < 0 || rho >= doubleHeight) {
                    continue;
                }
                accumulator[t][rho] += 1;
            }
        }

        numPoints++;
        addLines();
    }

    /**
     * Method searches for accumulator maximal value.
     *
     * @return max value from accumulator
     */
    private int findMax() {
        int maxValue = 0;

        int[][] acc = accumulator;
        for (int i = 0; i < acc.length; i++) {
            for (int j = 0; j < acc[i].length; j++) {
                if (acc[i][j] > maxValue) {
                    maxValue = acc[i][j];

                }
            }
        }
        return maxValue;
    }

    /**
     * Method obtains points that belong to the edge of the form image.
     *
     * @return list of edge points.
     */
    private List<Point> getEdges() {
        List<Point> edges = new ArrayList<Point>();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if ((i > image.getHeight() * 52 / 2338 && i < image.getHeight() * 144 / 2338 || i > image.getHeight() * 2172 / 2338
                        && j < image.getHeight() * 2280 / 2338)

                        && (j > image.getWidth() * 44 / 1653 && j < image.getWidth() * 132 / 1653 || j > image
                                .getWidth() * 1474 / 1653 && j < image.getWidth() * 1584 / 1653)
                                && image.getPixel(j, i)) {
                    edges.add(new Point(j, i));
                }
            }
        }
        return edges;
    }

    /**
     * Method initializes hough line detector accumulator.
     */
    public void initializeAccumulator() {
        int height = image.getHeight();
        int width = image.getWidth();

        // Calculate the maximum height the hough array needs to have
        houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;

        // Double the height of the hough array to cope with negative r values
        doubleHeight = 2 * houghHeight;

        // Create the hough array
        accumulator = new int[MAX_THETA][doubleHeight];

        // Find edge points and vote in array
        centerX = width / 2;
        centerY = height / 2;

        int diagLen = (int) Math.ceil(Math.sqrt(width * width + height * height)) + 1;

        accumulator = new int[MAX_THETA][diagLen * 2];

    }

    /**
     * Method initializes lookup tables.
     */
    private void initializeTables() {
        cosTable = new double[MAX_THETA];
        sinTable = new double[MAX_THETA];
        double thetaRadian = 0;
        for (int j = 0; j < MAX_THETA; j++) {
            thetaRadian += THETA_STEP;
            cosTable[j] = Math.cos(thetaRadian);
            sinTable[j] = Math.sin(thetaRadian);
        }

    }

}
