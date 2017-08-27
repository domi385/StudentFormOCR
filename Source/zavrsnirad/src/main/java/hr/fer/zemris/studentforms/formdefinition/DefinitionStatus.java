package hr.fer.zemris.studentforms.formdefinition;

/**
 * Enumeration that defines form definition stages.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.4.2017.
 *
 */
public enum DefinitionStatus {
    /**
     * There is no template loaded.
     */
    NO_TEMPLATE("No template"),
    /**
     * Loaded template is not defined.
     */
    UNDEFINED_TEMPLATE("Undefined template"),
    /**
     * Defining form markers.
     */
    MARKER_DEFINITION("Marker definition"),
    /**
     * Defining student points fields.
     */
    POINTS_DEFINITION("Points definition"),
    /**
     * Template is defined.
     */
    DEFINED_TEMPLATE("Defined template");

    /**
     * String representation of definition status.
     */
    private String value;

    /**
     * Constructor that initializes string representation of the form definition status.
     *
     * @param value
     *            string representation of the status
     */
    DefinitionStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
