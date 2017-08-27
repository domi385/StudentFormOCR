package hr.fer.zemris.image.edgedetection;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.RegionEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Method searchs for region edges.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class RegionEdgeDetector {

    /**
     * Class that models a edge search node and contains point and edge label.
     *
     * @author Domagoj Pluscec
     * @version v1.0, 29.4.2017.
     */
    private static class EdgeSearchNode {
        /**
         * Node point.
         */
        private Point p;
        /**
         * Node label.
         */
        private int label;

        /**
         * Edge search node constructor.
         *
         * @param p
         *            node point
         * @param label
         *            node label
         */
        EdgeSearchNode(Point p, int label) {
            super();
            this.p = p;
            this.label = label;
        }

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
            EdgeSearchNode other = (EdgeSearchNode) obj;
            if (label != other.label) {
                return false;
            }
            if (p == null) {
                if (other.p != null) {
                    return false;
                }
            } else if (!p.equals(other.p)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + label;
            result = prime * result + ((p == null) ? 0 : p.hashCode());
            return result;
        }

    }

    /**
     * Binary image background.
     */
    private static final boolean BACKGROUND = false;

    /**
     * Binary image foreground.
     */
    @SuppressWarnings("unused")
    private static final boolean FOREGROUND = true;

    /**
     * Number of pixels that a edge region has to minimaly have.
     */
    private static final int EDGE_MIN_PIXELS = 5;

    /**
     * Image on which to detect edges.
     */
    private final IBinaryImage image;

    /**
     * Constructor initializes detector with binary image.
     *
     * @param image
     *            binary image on which to detect edges
     */
    public RegionEdgeDetector(IBinaryImage image) {
        this.image = image;
    }

    /**
     * Method checks if all neighbor pixels belong to foreground.
     *
     * @param p
     *            current position
     * @return true if all neighbor pixels belong to foreground, false otherwise
     */
    private boolean allNeighboursForeground(Point p) {
        final int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
        final int[] dy = { 1, -1, 0, 0, 1, -1, 1, -1 };
        for (int i = 0; i < dx.length; i++) {
            int x = p.getX() + dx[i];
            int y = p.getY() + dy[i];
            if (x < 0 || x >= image.getWidth()) {
                return false;
            }
            if (y < 0 || y >= image.getHeight()) {
                return false;
            }
            if (image.getPixel(p.getX(), p.getY()) == BACKGROUND) {
                return false;
            }
        }
        return true;

    }

    /**
     * Method searches image from starting point to find edges of current foreground region. Edges are combined to
     * region edges if they are connected.
     *
     * @param startPoint
     *            search starting point
     * @return list of regions edges, where region edge is a list of points
     */
    public List<RegionEdge> getRegionsEdges(Point startPoint) {
        // connected component labeling
        int newLabel = 1;
        final int foregroundLabel = 0;

        Map<Integer, List<Point>> edges = new HashMap<>();
        Map<Point, Integer> pointLabel = new HashMap<Point, Integer>();
        Queue<EdgeSearchNode> unvisitedPoints = new LinkedList<EdgeSearchNode>();
        Set<Point> visitedPoints = new HashSet<>();

        EdgeSearchNode startNode = new EdgeSearchNode(startPoint, newLabel);
        unvisitedPoints.add(startNode);
        edges.put(newLabel, new ArrayList<Point>());
        edges.get(newLabel).add(startPoint);
        pointLabel.put(startPoint, newLabel);
        newLabel++;

        while (!unvisitedPoints.isEmpty()) {
            EdgeSearchNode currNode = unvisitedPoints.poll();
            if (visitedPoints.contains(currNode.p)) {
                continue;
            }

            int currLabel = currNode.label;
            List<Point> successors = getSuccessorPoints(currNode.p);
            visitedPoints.add(currNode.p);
            if (currLabel != foregroundLabel) {
                for (Point p : successors) {
                    if (visitedPoints.contains(p)) {
                        continue;
                    }

                    if (allNeighboursForeground(p)) {
                        unvisitedPoints.add(new EdgeSearchNode(p, foregroundLabel));
                        continue;
                    }
                    EdgeSearchNode tempNode = new EdgeSearchNode(p, currLabel);
                    unvisitedPoints.add(tempNode);
                    edges.put(currLabel, edges.getOrDefault(currLabel, new ArrayList<Point>()));
                    edges.get(currLabel).add(p);
                    pointLabel.put(p, currLabel);
                }
            } else {
                for (Point p : successors) {
                    if (visitedPoints.contains(p)) {
                        continue;
                    }

                    if (allNeighboursForeground(p)) {
                        unvisitedPoints.add(new EdgeSearchNode(p, foregroundLabel));
                        continue;
                    }
                    EdgeSearchNode tempNode = new EdgeSearchNode(p, newLabel);
                    unvisitedPoints.add(tempNode);
                    edges.put(newLabel, edges.getOrDefault(newLabel, new ArrayList<Point>()));
                    edges.get(newLabel).add(p);
                    pointLabel.put(p, newLabel);
                    newLabel++;
                }

            }

        }

        // second pass
        // merge conected labels
        int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, 1, -1 };

        for (Point p : pointLabel.keySet()) {
            int currentLabel = pointLabel.get(p);
            for (int i = 0; i < dx.length; i++) {
                int x = p.getX() + dx[i];
                int y = p.getY() + dy[i];
                if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
                    continue;
                }
                Point tempPoint = new Point(x, y);
                if (pointLabel.getOrDefault(tempPoint, currentLabel) == currentLabel) {
                    continue;
                }
                // merge labels
                int oldLabel = pointLabel.get(tempPoint);
                List<Point> oldLabeledPoints = edges.get(oldLabel);
                edges.remove(oldLabel);
                edges.get(currentLabel).addAll(oldLabeledPoints);
                for (Point oldPoint : oldLabeledPoints) {
                    pointLabel.put(oldPoint, currentLabel);
                }
            }
        }

        List<RegionEdge> regionsEdges = new ArrayList<>();
        for (Integer label : edges.keySet()) {
            if (label == foregroundLabel) {
                continue;
            }
            if (edges.get(label).size() < EDGE_MIN_PIXELS) {
                continue;
            }

            regionsEdges.add(new RegionEdge(edges.get(label)));
        }
        return regionsEdges;
    }

    /**
     * Method generates successor points of given point.
     *
     * @param p
     *            parent point
     * @return successor points
     */
    private List<Point> getSuccessorPoints(Point p) {
        final int[] dx = { 0, 0, 1, -1 };
        final int[] dy = { 1, -1, 0, 0 };
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < dx.length; i++) {
            int x = p.getX() + dx[i];
            int y = p.getY() + dy[i];
            if (x < 0 || x >= image.getWidth()) {
                continue;
            }
            if (y < 0 || y >= image.getHeight()) {
                continue;
            }
            if (image.getPixel(p.getX(), p.getY()) == BACKGROUND) {
                continue;
            }
            points.add(new Point(x, y));

        }
        return points;
    }

}
