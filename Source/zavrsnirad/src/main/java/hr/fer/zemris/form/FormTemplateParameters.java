package hr.fer.zemris.form;

import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.RegionEdge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class models form template parameters used for modeling form template used for scanning student forms.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 11.4.2017.
 *
 */
public class FormTemplateParameters implements Serializable {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 2L;
    /**
     * Initial positions of markers. Used to predict position of markers for future scanned forms.
     */
    private Point[] markerPositions;
    /**
     * Expected marker area side length. It assumes square markers.
     */
    private int expMarkerSize;

    /**
     * Template coordinate system.
     */
    private CoordinateSystem2D coordinateSystem;

    /**
     * Initial form width.
     */
    private int formWidth;
    /**
     * Initial form height.
     */
    private int formHeight;

    /**
     * List of region edges of point fields.
     */
    private List<RegionEdge> pointFields;

    /**
     * Form template parameters constructor that initializes form with determined form parameters.
     *
     * @param coordinateSystem
     *            form coordinate system
     * @param expMarkerSize
     *            expected size of markers
     * @param markerPositions
     *            approximate markers position
     * @param formWidth
     *            form width
     * @param formHeight
     *            form height
     */
    public FormTemplateParameters(CoordinateSystem2D coordinateSystem, int expMarkerSize, Point[] markerPositions,
            int formWidth, int formHeight) {
        this.markerPositions = markerPositions;
        this.expMarkerSize = expMarkerSize;
        this.formWidth = formWidth;
        this.formHeight = formHeight;
        pointFields = new ArrayList<>();
        this.coordinateSystem = coordinateSystem;
    }

    /**
     * Method adds a point field in list of point fields if the field has not already been added.
     *
     * @param pointsFieldEdge
     *            point field region
     */
    public void addPointField(RegionEdge pointsFieldEdge) {
        for (RegionEdge field : pointFields) {
            if (field.contains(pointsFieldEdge.getBoundingRectangleCenter())) {
                return;
            }
        }
        pointFields.add(pointsFieldEdge);
    }

    /**
     * Method obtains form template coordinate system.
     *
     * @return coordinate system
     */
    public CoordinateSystem2D getCoordinateSystem() {
        // TODO unmodifiable view
        return coordinateSystem;
    }

    /**
     * Method returns expected marker size defined on current template.
     *
     * @return expected marker size
     */
    public int getExpMarkerSize() {
        return expMarkerSize;
    }

    /**
     * Method obtains form height.
     *
     * @return form height
     */
    public int getFormHeight() {
        return formHeight;
    }

    /**
     * Method obtains form width.
     *
     * @return form width
     */
    public int getFormWidth() {
        return formWidth;
    }

    /**
     * Method obtains markers positions.
     *
     * @return array containing markers positions
     */
    public Point[] getMarkerPositions() {
        return markerPositions;
    }

    /**
     * Method obtains list of student point fields region edges.
     *
     * @return unmodifiable list of student point fields region edges
     */
    public List<RegionEdge> getPointFields() {
        return Collections.unmodifiableList(pointFields);
    }

    /**
     * Method obtains number of point fields.
     *
     * @return number of student point fields
     */
    public int getPointsNumber() {
        return pointFields.size();
    }

}
