package hr.fer.zemris.image.markerdetection;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.edgedetection.RegionEdgeDetector;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.geometry.RegionEdge;
import hr.fer.zemris.image.geometry.RegionUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegionMarkerDetector implements IMarkerDetector {
    private IBinaryImage image;
    private List<Region> markers;

    public RegionMarkerDetector(IBinaryImage image) {
        this.image = image;
        markers = new ArrayList<Region>(3);
    }

    @Override
    public void detectMarkers() {
        Point startPoint = getStartPoint();
        RegionEdgeDetector edgeDetector = new RegionEdgeDetector(image);
        List<RegionEdge> listOfRegionsEdges = edgeDetector.getRegionsEdges(startPoint);
        Collections.sort(listOfRegionsEdges);

        // pick three smallest with same area with threshold odstupanje
        List<RegionEdge> potentialMarkersEdges = listOfRegionsEdges
                .stream()
                .filter(i -> i.getDim().height > 10 && i.getDim().height < image.getHeight() / 10
                        || i.getDim().width > 10 && i.getDim().width < image.getWidth() / 10)
                .collect(Collectors.toList());
        if (potentialMarkersEdges.size() == MARKERS_NUMBER) {
            for (RegionEdge edge : potentialMarkersEdges) {
                markers.add(RegionUtility.edgeToRegion(image, edge));
            }
        } else {
            // TODO nađi 3 slična
        }
    }

    @Override
    public List<Region> getMarkers() {
        return markers;
    }

    /**
     * Method finds right upper edge that is filled with foreground color.
     *
     * @return Point of right upper foreground edge
     * @throws RuntimeException
     *             if starting point cannot be find
     */
    private Point getStartPoint() throws RuntimeException {
        // TODO ideally it should find foreground colored point with minimum
        // eucleadean distance from right upper corner

        int widthEnd = image.getWidth() / 2;
        int heightEnd = image.getHeight() / 2;

        for (int y = 0; y < heightEnd; y++) {
            for (int x = 0; x < widthEnd; x++) {
                if (image.getPixel(x, y)) {
                    return new Point(x, y);
                }
            }
        }

        throw new RuntimeException("Unable to find starting point.");
    }

}
