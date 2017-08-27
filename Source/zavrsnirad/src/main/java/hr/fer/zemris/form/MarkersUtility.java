package hr.fer.zemris.form;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.Line;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.markerdetection.IMarkerDetector;
import hr.fer.zemris.image.markerdetection.MarkerDetectionException;
import hr.fer.zemris.image.markerdetection.PositionMarkerDetector;
import hr.fer.zemris.image.markerdetection.RegionMarkerDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class contains utility methods for working with form markers.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 30.4.2017.
 */
public class MarkersUtility {

    /**
     * Method calculates coordinate system from markers using assumption that x is shortest axis, y is second in length.
     * Also it assumes that there are 3 markers.
     *
     * @param markerPositions
     *            array of three marker positions
     * @return coordinate system defined by markers
     * @throws UnsupportedOperationException
     *             if given array has less or more than three markers
     */
    public static CoordinateSystem2D coordinateSystemFromMarkers(Point[] markerPositions)
            throws UnsupportedOperationException {
        if (markerPositions.length != IMarkerDetector.MARKERS_NUMBER) {
            throw new UnsupportedOperationException("Method expects 3 markers");
        }
        List<Line> lines = new ArrayList<Line>();
        lines.add(new Line(markerPositions[0], markerPositions[1]));
        lines.add(new Line(markerPositions[0], markerPositions[2]));
        lines.add(new Line(markerPositions[1], markerPositions[2]));
        Collections.sort(lines, (l1, l2) -> l1.calcLength().compareTo(l2.calcLength()));
        Point origin;
        Line smallestLine = lines.get(0);
        Line diagonalLine = lines.get(2);
        if (!smallestLine.getP1().equals(diagonalLine.getP1()) && !smallestLine.getP1().equals(diagonalLine.getP2())) {
            origin = smallestLine.getP1();
        } else {
            origin = smallestLine.getP2();
        }

        Line first = new Line(origin, diagonalLine.getP1());
        Line second = new Line(origin, diagonalLine.getP2());

        return new CoordinateSystem2D(origin, first.calcLength() > second.calcLength() ? second : first,
                first.calcLength() > second.calcLength() ? first : second);
    }

    /**
     * Method uses marker detection algorithms to detect markers with given heuristic position.
     *
     * @param image
     *            binary image representing form that contains markers
     * @param expMarkers
     *            expected marker positions
     * @param markerDetectionDimension
     *            dimension or marker square
     * @return array with three markers
     * @throws MarkerDetectionException
     *             markers cannot be detected or there are to many markers
     */
    public static Point[] findMarkersByPositions(IBinaryImage image, Point[] expMarkers, int markerDetectionDimension)
            throws MarkerDetectionException {

        List<Region> markerRegions = null;

        IMarkerDetector[] detectors = { new PositionMarkerDetector(image, expMarkers, markerDetectionDimension),
                new RegionMarkerDetector(image) };

        for (IMarkerDetector detector : detectors) {
            try {
                detector.detectMarkers();
                markerRegions = detector.getMarkers();
                if (markerRegions == null || markerRegions.size() < IMarkerDetector.MARKERS_NUMBER) {
                    throw new MarkerDetectionException();
                }
                break;
            } catch (MarkerDetectionException mde) {
                continue;
            }
        }

        if (markerRegions == null) {
            throw new MarkerDetectionException("Markers couldn't be identified");
        }

        Point[] markerCenters = new Point[IMarkerDetector.MARKERS_NUMBER];
        for (int i = 0; i < IMarkerDetector.MARKERS_NUMBER; i++) {
            markerCenters[i] = markerRegions.get(i).getRegionCenter();
        }
        return markerCenters;

    }

    /**
     * Private utility class constructor.
     */
    private MarkersUtility() {
    }
}
