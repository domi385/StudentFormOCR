package hr.fer.zemris.studentforms.formdefinition.data;

import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.MarkersUtility;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.edgedetection.SimpleRectangleEdgeDetector;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.RegionEdge;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.MaximalDecompositionAlgorithm;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that models data model for form definition process.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 21.4.2017.
 */
public class FormDefinitionData implements Serializable {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Default number of markers.
     */
    private static final int MARKERS_NUMBER = 3;
    /**
     * Markers region size.
     */
    private static final int MARKER_SIZE = 10;
    /**
     * Point field minimum width.
     */
    private static final int POINT_FIELD_WIDTH_FILTER = 25;
    /**
     * Point field minimum height.
     */
    private static final int POINT_FIELD_HEIGHT_FILTER = 22;

    /**
     * Current definition status.
     */
    private DefinitionStatus currentStatus;
    /**
     * Current form template image.
     */
    private BufferedImage templateImage;

    /**
     * Form definition data listeners.
     */
    private List<FormDefinitionDataListener> listeners;

    /**
     * Form template parameters.
     */
    private FormTemplateParameters formTemplateParameters;
    /**
     * Array of currently defined markers.
     */
    private Point[] markers = new Point[MARKERS_NUMBER];
    /**
     * Number of currently defined markers.
     */
    private int markersNum = 0;
    /**
     * Current mouse position on the form template.
     */
    private Point tempMousePosition = new Point(0, 0);

    /**
     * Algorithm used for binarization.
     */
    private IBinarizationAlgorithm binAlgorithm = new OtsuAlgorithm();

    /**
     * Algorithm used for grayscaling image.
     */
    private IGrayscaleAlgorithm grayAlgorithm = new MaximalDecompositionAlgorithm();

    /**
     * Binary image of template form.
     */
    private BinaryImage binarizedTemplate;

    /**
     * Constructor that initializes form definition data atributes.
     */
    public FormDefinitionData() {
        currentStatus = DefinitionStatus.NO_TEMPLATE;
        listeners = new ArrayList<FormDefinitionDataListener>();
    }

    /**
     * Method adds listener to form definition data model.
     *
     * @param listener
     *            form definition data listener
     * @throws IllegalArgumentException
     *             if given listener is null
     */
    public void addListener(FormDefinitionDataListener listener) throws IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null.");
        }
        listeners.add(listener);
    }

    private void addMarkerPosition(int x, int y, int width, int height) {
        setTempMousePosition(x, y, width, height);
        markers[markersNum] = tempMousePosition.copy();
        markersNum++;
        notifyMousePositionChanged();
        if (markersNum == MARKERS_NUMBER) {
            setStatus(DefinitionStatus.DEFINED_TEMPLATE);
            defineFormTemplateParameters();
        }

    }

    public void addMousePosition(int x, int y, int width, int height) {
        if (currentStatus == DefinitionStatus.MARKER_DEFINITION) {
            addMarkerPosition(x, y, width, height);
        } else if (currentStatus == DefinitionStatus.POINTS_DEFINITION) {
            addPointsPosition(x, y, width, height);
        }
    }

    private void addPointsPosition(int x, int y, int width, int height) {
        Point regionPoint = new Point((x * templateImage.getWidth()) / width, (y * templateImage.getHeight()) / height);

        // RegionEdgeDetector red = new RegionEdgeDetector(
        // formTemplateParameters.getBinarizedTemplate());
        SimpleRectangleEdgeDetector sred = new SimpleRectangleEdgeDetector(binarizedTemplate);
        RegionEdge edge = sred.getRegionsEdge(regionPoint, true);

        // edges = edges
        // .stream()
        // .filter(i -> i.getDim().height > POINT_FIELD_HEIGHT_FILTER
        // && i.getDim().width > POINT_FIELD_WIDTH_FILTER)
        // .collect(Collectors.toList());

        // if (edges.size() == 1) {
        formTemplateParameters.addPointField(edge);
        notifyPointFieldAdded();
        // } else {
        // for (RegionEdge edge : edges) {
        // System.out.println(edge.getBoundingRectange());
        // }
        // System.out.println("Ne znam što da radim s toliko rubova");
        // }

    }

    private void defineFormTemplateParameters() {
        binarizedTemplate = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(templateImage)));
        CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                .findMarkersByPositions(binarizedTemplate, markers, MARKER_SIZE));

        formTemplateParameters = new FormTemplateParameters(coordinateSystem, MARKER_SIZE, markers,
                binarizedTemplate.getWidth(), binarizedTemplate.getHeight());
        templateImage = binarizedTemplate.toImage();
        notifyImageChanged();
    }

    public CoordinateSystem2D getCoordinateSystem() {
        return formTemplateParameters.getCoordinateSystem();
    }

    public FormTemplateParameters getFormTemplate() {
        return formTemplateParameters;
    }

    public Dimension getImageDimension() {
        return new Dimension(templateImage.getWidth(), templateImage.getHeight());
    }

    public int getNumberOfMarkers() {
        return markersNum;
    }

    public int getNumberOfPoints() {
        return formTemplateParameters.getPointsNumber();
    }

    public List<RegionEdge> getPointFields() {
        return formTemplateParameters.getPointFields();
    }

    public DefinitionStatus getStatus() {
        return currentStatus;
    }

    public Point getTempMousePosition() {
        return tempMousePosition;
    }

    public boolean isCoordinateSystemDefined() {
        return formTemplateParameters != null;
    }

    /**
     * Method cheks if the template image has been loaded.
     *
     * @return true if the image is loaded, false otherwise
     */
    public boolean isImageLoaded() {
        return templateImage != null;
    }

    /**
     * Method notifies listeners for image changes.
     */
    private void notifyImageChanged() {
        listeners.forEach(i -> i.imageChanged(templateImage));
    }

    /**
     * Method notifies listeners for mouse point changes.
     */
    private void notifyMousePositionChanged() {
        listeners.forEach(l -> l.mousePositionChanged(tempMousePosition));
    }

    private void notifyPointFieldAdded() {
        listeners.forEach(i -> i.pointFieldAdded());
    }

    private void notifyStatusChanged() {
        listeners.forEach(i -> i.statusChanged(currentStatus));
    }

    public void removeListener(FormDefinitionDataListener listener) {
        listeners.remove(listener);
    }

    public void setFormParams(FormTemplateParameters templateParameters) {
        formTemplateParameters = templateParameters;
        binarizedTemplate = new BinaryImage(
                new boolean[formTemplateParameters.getFormHeight()][formTemplateParameters.getFormWidth()], false);
        templateImage = new BufferedImage(formTemplateParameters.getFormWidth(),
                formTemplateParameters.getFormHeight(), BufferedImage.TYPE_INT_RGB);
        notifyImageChanged();
        setStatus(DefinitionStatus.DEFINED_TEMPLATE);

    }

    public void setImage(BufferedImage image) {
        templateImage = image;
        formTemplateParameters = null;
        currentStatus = DefinitionStatus.UNDEFINED_TEMPLATE;
        notifyImageChanged();
        notifyStatusChanged();
    }

    public void setStatus(DefinitionStatus status) {

        currentStatus = status;
        if (status == DefinitionStatus.MARKER_DEFINITION) {
            markersNum = 0;
        }
        notifyStatusChanged();
    }

    public void setTempMousePosition(int x, int y, int width, int height) {
        tempMousePosition.setLocation((x * templateImage.getWidth()) / width, (y * templateImage.getHeight()) / height);
        notifyMousePositionChanged();
    }
}
