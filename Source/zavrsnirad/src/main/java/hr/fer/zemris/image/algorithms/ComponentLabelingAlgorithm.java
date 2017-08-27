package hr.fer.zemris.image.algorithms;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class implements connected components labeling algorithm.
 *
 * Algorithm is described in @link{https://en.wikipedia.org/wiki/Connected-component_labeling .
 *
 * @author Domagoj Pluscec
 * @version 9.6.2017.
 */
public class ComponentLabelingAlgorithm {

    /**
     * Binary image background color.
     */
    private static final boolean BACKGROUND = true;

    /**
     * Method searches for minimal equivalent label.
     *
     * @param linked
     *            map of equivalent labels
     * @param label
     *            current label
     * @return minimal equivalent label
     */
    private static int findMinLabel(Map<Integer, Set<Integer>> linked, int label) {
        List<Integer> alternativeLabels = new ArrayList<Integer>();
        for (Integer keyLabel : linked.keySet()) {
            if (linked.get(keyLabel).contains(label)) {
                alternativeLabels.add(keyLabel);
            }
        }
        return alternativeLabels.stream().mapToInt(i -> i).min().getAsInt();

    }

    /**
     * Method obtains detected components.
     *
     * @param image
     *            image on which to search for components
     * @return regions definitions of components
     */
    public static List<Region> getComponents(IBinaryImage image) {

        Map<Integer, Set<Integer>> linked = new HashMap<>();
        int[][] labels = new int[image.getHeight()][image.getWidth()]; // structure
        // with dimensions of data, initialized
        // with the value of Background
        int nextLabel = 1;

        // First pass

        for (int y = 0, height = image.getHeight(), width = image.getWidth(); y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getPixel(x, y) == BACKGROUND) {
                    continue;
                }
                Point currPoint = new Point(x, y);
                List<Point> neighbors = getNeighbours(image, currPoint, labels); // connected
                // elements with the current element's value

                if (neighbors.isEmpty()) {
                    linked.put(nextLabel, new HashSet<>());
                    linked.get(nextLabel).add(nextLabel);
                    labels[y][x] = nextLabel;
                    nextLabel++;
                } else {
                    // Find the smallest label
                    List<Integer> neigboursLabels = getNeighbourLabels(neighbors, labels);
                    labels[y][x] = neigboursLabels.stream().mapToInt(i -> i).min().getAsInt();
                    for (Integer label : neigboursLabels) {
                        linked.get(label).addAll(neigboursLabels);
                    }
                }
            }
        }

        // transitive connection
        for (Integer label : linked.keySet()) {
            for (Integer equivalentLabel : linked.get(label)) {
                linked.get(equivalentLabel).addAll(linked.get(label));
            }
        }

        // Second pass
        for (int y = 0, height = image.getHeight(), width = image.getWidth(); y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getPixel(x, y) == BACKGROUND) {
                    continue;
                }
                labels[y][x] = findMinLabel(linked, labels[y][x]);
            }
        }

        return labelsToRegions(labels);
    }

    /**
     * Method obtains labels for neighborhood labels.
     *
     * @param neighbors
     *            neighbors positions
     * @param labels
     *            labels matrix
     * @return neigborhood labels
     */
    private static List<Integer> getNeighbourLabels(List<Point> neighbors, int[][] labels) {
        List<Integer> resultLabels = new ArrayList<Integer>();
        for (Point neighbour : neighbors) {
            resultLabels.add(labels[neighbour.getY()][neighbour.getX()]);
        }
        return resultLabels;
    }

    /**
     * Method obtains neighbours of current position. It assumes 8-connectivity and looks for labels to the North-East,
     * the North, the North-West and the West of the current pixel
     *
     * @param image
     *            current image
     * @param point
     *            current position
     * @param labels
     *            labels matrix
     * @return neighbour positions
     */
    private static List<Point> getNeighbours(IBinaryImage image, Point point, int[][] labels) {

        List<Point> neighbours = new ArrayList<Point>();
        int[][] delta = { { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 } };
        for (int i = 0; i < delta.length; i++) {
            int x = point.getX() + delta[i][0];
            int y = point.getY() + delta[i][1];
            if (x < 0 || y < 0 || x >= labels[0].length || y >= labels.length) {
                continue;
            }
            if (labels[y][x] == 0) {
                continue;
            }
            neighbours.add(new Point(x, y));
        }
        return neighbours;

    }

    /**
     * Method connects labels to regions.
     *
     * @param labels
     *            labels matrix
     * @return list of connected regions
     */
    private static List<Region> labelsToRegions(int[][] labels) {
        Map<Integer, List<Point>> regionsPoints = new HashMap<>();
        for (int y = 0, height = labels.length, width = labels[0].length; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (labels[y][x] == 0) {
                    continue;
                }
                if (!regionsPoints.containsKey(labels[y][x])) {
                    regionsPoints.put(labels[y][x], new LinkedList<>());
                }
                regionsPoints.get(labels[y][x]).add(new Point(x, y));
            }
        }
        List<Region> regions = new ArrayList<Region>();
        for (Integer label : regionsPoints.keySet()) {
            regions.add(new Region(regionsPoints.get(label)));
        }
        Collections.sort(regions,
                (r1, r2) -> Integer.valueOf(r1.getBoundingRectangle().x).compareTo(r2.getBoundingRectangle().x));
        return regions;
    }

    /**
     * Private utility demo class constructor.
     */
    private ComponentLabelingAlgorithm() {
    }
}
