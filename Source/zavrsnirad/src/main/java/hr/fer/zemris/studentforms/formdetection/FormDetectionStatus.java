package hr.fer.zemris.studentforms.formdetection;

/**
 * Status for detecting form coordinate system and regions.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public enum FormDetectionStatus {
    /**
     * There is no form loaded.
     */
    NO_FORM,
    /**
     * Coordinate system hasn't been defined yet.
     */
    NO_COORD,
    /**
     * There is defined coordinate system but regions haven't been detected yet.
     */
    NO_REGIONS,
    /**
     * Regions have been detected.
     */
    DETECTED_REGIONS;
}
