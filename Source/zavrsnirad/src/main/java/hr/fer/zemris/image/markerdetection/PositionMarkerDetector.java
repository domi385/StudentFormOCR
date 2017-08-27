package hr.fer.zemris.image.markerdetection;

import hr.fer.zemris.image.algorithms.FloodFillAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithm detects marker positions with given expected marker positions.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class PositionMarkerDetector implements IMarkerDetector {

    /**
     * Binary image background.
     */
    private static final boolean BACKGROUND = false;
    /**
     * Binary image foreground.
     */
    private static final boolean FOREGROUND = true;
    /**
     * Image for detecting markers.
     */
    private IBinaryImage image;
    /**
     * Expected markers positions.
     */
    private Point[] positions;

    /**
     * Expected marker dimension.
     */
    private int markerDimension;

    /**
     * List of detected markers.
     */
    private List<Region> markers;

    /**
     * Constructor that initializes marker detector with expected marker positions.
     *
     * @param image
     *            image on which to detect markers
     * @param positions
     *            expected marker positions
     * @param markerDimension
     *            expected marker dimension
     */
    public PositionMarkerDetector(IBinaryImage image, Point[] positions, int markerDimension) {
        super();
        this.image = image;
        this.positions = positions;
        this.markerDimension = markerDimension;
    }

    @Override
    public void detectMarkers() throws MarkerDetectionException {
        markers = new ArrayList<Region>();

        FloodFillAlgorithm flAlgorithm = new FloodFillAlgorithm(image);

        for (int i = 0; i < MARKERS_NUMBER; i++) {

            Point expPos = positions[i];
            if (image.getPixel(expPos) == BACKGROUND) {
                markers.add(flAlgorithm.extractRegion(expPos, FOREGROUND));
                continue;
            }

            int startX = Math.max(0, expPos.getX() - markerDimension / 2);
            int startY = Math.max(0, expPos.getY() - markerDimension / 2);
            int endX = Math.min(image.getWidth(), expPos.getX() + markerDimension / 2);
            int endY = Math.min(image.getHeight(), expPos.getY() + markerDimension / 2);
            boolean endFlag = false;
            for (int y = startY; y < endY; y++) {
                if (endFlag) {
                    break;
                }
                for (int x = startX; x < endX; x++) {
                    if (image.getPixel(x, y) == BACKGROUND) {
                        markers.add(flAlgorithm.extractRegion(new Point(x, y), FOREGROUND));
                        endFlag = true;
                        break;
                    }
                }
            }

        }
    }

    @Override
    public List<Region> getMarkers() throws MarkerDetectionException {
        if (markers == null) {
            throw new MarkerDetectionException("Detect markers method hasn't been called");
        }
        return markers;
    }
}
