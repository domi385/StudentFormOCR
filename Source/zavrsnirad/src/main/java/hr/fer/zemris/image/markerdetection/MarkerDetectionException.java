package hr.fer.zemris.image.markerdetection;

/**
 * Marker detection exception.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public class MarkerDetectionException extends RuntimeException {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default exception constructor.
     */
    public MarkerDetectionException() {
    }

    /**
     * Exception constructor that contains exception message.
     *
     * @param message
     *            why has exception been thrown
     */
    public MarkerDetectionException(String message) {
        super(message);
    }

}
