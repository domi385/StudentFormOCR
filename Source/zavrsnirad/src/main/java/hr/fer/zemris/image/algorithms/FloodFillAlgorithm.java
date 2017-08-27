package hr.fer.zemris.image.algorithms;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Class implements image flood fill algorithm that returns points contained in flooded region.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class FloodFillAlgorithm {

    /**
     * Binary image on which to apply algorithm.
     */
    private IBinaryImage image;

    /**
     * Constructor that initializes algorithm with given image.
     *
     * @param image
     *            binary image on which to apply algorithm
     */
    public FloodFillAlgorithm(IBinaryImage image) {
        this.image = image;
    }

    /**
     * Method extracts region with flood from start point and given target color.
     *
     * @param startPoint
     *            starting point
     * @param targetMark
     *            target boolean representation of color
     * @return points contained in flooded region
     */
    public Region extractRegion(Point startPoint, boolean targetMark) {
        if (image.getPixel(startPoint.getX(), startPoint.getY()) == targetMark) {
            throw new IllegalArgumentException("Extractign region cannot start with final point.");
        }
        Set<Point> regionPoints = new HashSet<Point>();
        Queue<Point> openPoints = new LinkedList<>();
        openPoints.add(startPoint);

        while (!openPoints.isEmpty()) {
            Point currPoint = openPoints.poll();
            if (regionPoints.contains(currPoint)) {
                continue;
            }
            List<Point> successors = getSuccessorPoints(currPoint, targetMark);
            regionPoints.add(currPoint);
            for (Point s : successors) {
                if (!regionPoints.contains(s)) {
                    openPoints.add(s);
                }
            }
        }
        return new Region(regionPoints);
    }

    /**
     * Method obtains successor points of current point using 8-connection neighborhood.
     *
     * @param currPoint
     *            current point position
     * @param targetMark
     *            target color
     * @return list of neighbors positions
     */
    private List<Point> getSuccessorPoints(Point currPoint, boolean targetMark) {
        int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, 1, -1 };
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < dx.length; i++) {
            int x = currPoint.getX() + dx[i];
            int y = currPoint.getY() + dy[i];
            if (x < 0 || x >= image.getWidth()) {
                continue;
            }
            if (y < 0 || y >= image.getHeight()) {
                continue;
            }
            if (image.getPixel(currPoint.getX(), currPoint.getY()) == targetMark) {
                continue;
            }
            points.add(new Point(x, y));
        }
        return points;

    }

}
