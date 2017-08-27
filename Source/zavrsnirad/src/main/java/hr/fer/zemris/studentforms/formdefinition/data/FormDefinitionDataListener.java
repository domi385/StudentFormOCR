package hr.fer.zemris.studentforms.formdefinition.data;

import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;

import java.awt.image.BufferedImage;

/**
 * Interface that defines form definition data listener.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public interface FormDefinitionDataListener {

    /**
     * Method is called when data model changes form image.
     *
     * @param image
     *            new form image
     */
    void imageChanged(BufferedImage image);

    /**
     * Method is called when data model mouse position changes.
     *
     * @param mousePosition
     *            new mouse position
     */
    void mousePositionChanged(Point mousePosition);

    /**
     * Method is called when a student point field is added.
     */
    void pointFieldAdded();

    /**
     * Method is called when data model changes definition status.
     *
     * @param status
     *            current definition status
     */
    void statusChanged(DefinitionStatus status);
}
