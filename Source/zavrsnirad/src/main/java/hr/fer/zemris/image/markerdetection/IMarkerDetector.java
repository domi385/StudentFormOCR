package hr.fer.zemris.image.markerdetection;

import hr.fer.zemris.image.geometry.Region;

import java.util.List;

/**
 * Interface models marker detector that can detect marker objects on images. Default number of marker is given in the
 * interface.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public interface IMarkerDetector {
    /**
     * Expected number of markers that define form.
     */
    int MARKERS_NUMBER = 3;

    /**
     * Method detects markers.
     *
     * @throws MarkerDetectionException
     *             if there was an error while trying to detect markers
     */
    void detectMarkers() throws MarkerDetectionException;

    /**
     * Method returns list of marker regions.
     *
     * @return list of markers
     * @throws MarkerDetectionException
     *             if detectMarkers method hasn't been called
     */
    List<Region> getMarkers() throws MarkerDetectionException;
}
