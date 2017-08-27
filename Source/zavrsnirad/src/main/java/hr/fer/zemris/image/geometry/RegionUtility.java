package hr.fer.zemris.image.geometry;

import hr.fer.zemris.image.algorithms.FloodFillAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.binary.IBinaryImage;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for working with image regions.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class RegionUtility {

    /**
     * Method obtains points contained in a region with given edge.
     *
     * @param image
     *            image from which to extract points
     * @param regionEdge
     *            region edge
     * @return region inside given region edge
     */
    public static Region edgeToRegion(IBinaryImage image, RegionEdge regionEdge) {

        Point edgeCenterPoint = regionEdge.getBoundingRectangleCenter();
        FloodFillAlgorithm floodFillAlgorithm = new FloodFillAlgorithm(image);
        return floodFillAlgorithm.extractRegion(edgeCenterPoint,
                !image.getPixel(edgeCenterPoint.getX(), edgeCenterPoint.getY()));
    }

    /**
     * Method merges two regions.
     *
     * @param firstRegion
     *            first region to merge
     * @param secondRegion
     *            second region to merge
     * @return new merged region
     */
    public static Region mergeRegions(Region firstRegion, Region secondRegion) {
        Set<Point> regionPoints = new HashSet<Point>();
        regionPoints.addAll(firstRegion.getPoints());

        regionPoints.addAll(secondRegion.getPoints());
        return new Region(regionPoints);
    }

    /**
     * Method transforms region to binary image.
     *
     * @param region
     *            region which should be transformed
     * @return binary image of a region
     */
    public static IBinaryImage regionToBinaryImage(Region region) {
        return new BinaryImage(regionToBooleanArray(region, true, false), false);
    }

    /**
     * Method converts region to boolean array.
     *
     * @param region
     *            region which should be transformed
     * @param background
     *            background color boolean value
     * @param foreground
     *            foreground color boolean value
     * @return region converted into boolean matrix
     */
    public static boolean[][] regionToBooleanArray(Region region, boolean background, boolean foreground) {
        Rectangle dim = region.getBoundingRectangle();
        boolean[][] regionMatrix = new boolean[dim.height + 1][dim.width + 1]; // TODO
        // nisam
        // siguran
        // gdje
        // bi
        // se
        // ovaj
        // +1
        // trebao
        // dogoditi
        for (int i = 0; i < regionMatrix.length; i++) {
            Arrays.fill(regionMatrix[i], background);
        }
        Set<Point> points = region.getPoints();
        for (Point point : points) {
            regionMatrix[point.getY() - dim.y][point.getX() - dim.x] = foreground;
        }
        return regionMatrix;
    }

    /**
     * Private utility class constructor.
     */
    private RegionUtility() {
    }
}
