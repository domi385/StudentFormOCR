package hr.fer.zemris.studentforms.formdetection.data;

import hr.fer.zemris.form.FormNode;
import hr.fer.zemris.form.FormNodeUtility;
import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.MarkersUtility;
import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binary.BinaryImageUtility;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.transformation.RotateImageCenterAlgorithm;
import hr.fer.zemris.studentforms.formdetection.FormDetectionStatus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

/**
 * Data model for form detection.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.5.2017.
 */
public class FormDetectionDataModel {

    /**
     * Field top offset.
     */
    private static final int UP_OFFSET = 8;
    /**
     * Field bottom offset.
     */
    private static final int LOW_OFFSET = 2;
    /**
     * Field left offset.
     */
    private static final int LEFT_OFFSET = 5;
    /**
     * Field right offset.
     */
    private static final int RIGHT_OFFSET = 5;

    /**
     * Original scanned image.
     */
    private BufferedImage originalImage;
    /**
     * Transformed scanned image.
     */
    private IBinaryImage transformedImage;

    /**
     * Form parameters.
     */
    private FormTemplateParameters ftp;

    /**
     * Form detection data listeners.
     */
    private List<FormDetectionDataListener> listeners;

    /**
     * algorithm used for grayscaling.
     */
    private IGrayscaleAlgorithm grayAlgorithm = new AverageAlgorithm();

    /**
     * Algorithm used for binarization.
     */
    private IBinarizationAlgorithm binaryAlgorithm = new GlobalThresholdAlgorithm(250);

    /**
     * Current form node.
     */
    private FormNode formNode;

    /**
     * Form detection status.
     */
    private FormDetectionStatus status;

    /**
     * Listener listens for form change.
     */
    private FormDetectionDataAdapter formNodeListener = new FormDetectionDataAdapter() {
        @Override
        public void stateChanged(FormDetectionDataModel dataModel) {
            formNode.setBinarizedForm(getTransformedBinImage());
        }

    };

    /**
     * List of region images.
     */
    private List<IBinaryImage> regionImages;

    /**
     * Constructor that initializes form detection data model.
     */
    public FormDetectionDataModel() {
        status = FormDetectionStatus.NO_FORM;
        listeners = new ArrayList<FormDetectionDataListener>();
        formNode = new FormNode();
        listeners.add(formNodeListener);
    }

    /**
     * Method adds listener to data model.
     *
     * @param listener
     *            data model listener
     */
    public void addListener(FormDetectionDataListener listener) {
        listeners.add(listener);
    }

    /**
     * Method changes current form.
     *
     * @param formImage
     *            scanned form image
     */
    public void changeForm(BufferedImage formImage) {
        originalImage = formImage;
        transformedImage = ImageUtility.toBinary(formImage, grayAlgorithm, binaryAlgorithm);
        formNode = new FormNode();
        formNode.setFormTemplateParameters(ftp);
        status = FormDetectionStatus.NO_COORD;
        fireStatusChanged();
        fire(true);
    }

    /**
     * Method notifies listeners for detection status change.
     */
    private void fireStatusChanged() {
        listeners.forEach(i -> i.statusChanged(status));
    }

    /**
     * Method crops transformed form.
     */
    public void cropForm() {
        transformedImage = BinaryImageUtility.cropWithThreshold(transformedImage, 0);
        fire(false);
    }

    /**
     * Method defines coordinate system of transformed form.
     */
    public void defineCoordinateSystem() {
        // TODO throw if not loaded
        formNode.defineCoordinateSystem();
        status = FormDetectionStatus.NO_REGIONS;
        fireStatusChanged();
        fire(false);
    }

    /**
     * Method extracts regions from a scanned form.
     *
     * @param regionsExtractionProgress
     *            form listener that checks region extraction progress
     */
    public void extractRegions(FormDetectionDataAdapter regionsExtractionProgress) {
        regionImages = Collections.synchronizedList(new ArrayList<>());

        Thread t = new Thread() {
            private int i;

            @Override
            public void run() {
                for (i = 0; i < formNode.getFormTemplate().getPointsNumber(); i++) {
                    regionImages.add(FormNodeUtility.extractField(formNode, i, UP_OFFSET, LOW_OFFSET, LEFT_OFFSET,
                            RIGHT_OFFSET));

                    SwingUtilities.invokeLater(new Runnable() {
                        private final int j = i;

                        @Override
                        public void run() {
                            regionsExtractionProgress.extractionUpdate(j);

                        }
                    });
                }
                SwingUtilities.invokeLater(() -> changeStatus(FormDetectionStatus.DETECTED_REGIONS));
            }

        };
        t.start();
    }

    /**
     * Method changes current status.
     *
     * @param status
     *            detection status
     */
    private void changeStatus(FormDetectionStatus status) {
        this.status = status;
        fireStatusChanged();
    }

    /**
     * Method notifies all listeners.
     *
     * @param originalChanged
     *            flag if the orginal form has been changed
     */
    private void fire(boolean originalChanged) {
        listeners.forEach(i -> i.stateChanged(this));
        if (originalChanged) {
            listeners.forEach(i -> i.originalChanged(this));
        }
    }

    /**
     * Method obtains binarized point regions.
     *
     * @return images of point regions
     */
    public List<IBinaryImage> getBinarizedPointRegions() {
        return regionImages;
    }

    /**
     * Method obtains currently active form node
     *
     * @return
     */
    public FormNode getFormNode() {
        return formNode;
    }

    /**
     * Method obtains form detection original image.
     *
     * @return original image
     */
    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    /**
     * Method obrains extracted point regions.
     *
     * @return list of point region images
     */
    public List<BufferedImage> getPointRegions() {
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        for (IBinaryImage binImage : regionImages) {
            images.add(binImage.toImage());
        }
        return images;
    }

    /**
     * Method obtains number of student point regions.
     *
     * @return number of point regions
     */
    public int getPointsRegionsNum() {
        return ftp.getPointsNumber();
    }

    /**
     * Internal list of segments.
     */
    private List<List<BufferedImage>> segments;

    /**
     * Method obtains segmented images of given field index.
     *
     * @param imageIndex
     *            index of point field
     * @return list of segment images
     */
    public List<BufferedImage> getSegmentedImages(int imageIndex) {
        Predicate<Region> segmentsFilter = i -> i.getBoundingRectangle().width > 2
                && i.getBoundingRectangle().height > 2 && i.getPoints().size() > 10
                && i.getBoundingRectangle().width * i.getBoundingRectangle().height > 6;

                List<IBinaryImage> segments = FormNodeUtility.segmentField(regionImages.get(imageIndex), segmentsFilter);
                List<BufferedImage> images = new ArrayList<BufferedImage>();
                for (IBinaryImage binImage : segments) {
                    images.add(binImage.toImage());
                }
                return images;
    }

    /**
     * Method obtains transformed binarized image.
     *
     * @return transformed binarized image
     */
    public IBinaryImage getTransformedBinImage() {
        return transformedImage;
    }

    /**
     * Method obtains transformed image.
     *
     * @return transformed image
     */
    public BufferedImage getTransformedImage() {
        return transformedImage.toImage();
    }

    /**
     * Method checks if the coordinate system has been defined.
     *
     * @return true if the coordinate system is defined, false otherwise
     */
    public boolean isCoordinateSystemDefined() {
        if (formNode == null) {
            return false;
        }
        return formNode.isCoordinateSystemDefined();
    }

    /**
     * Method removes form detection data listener.
     *
     * @param listener
     *            form detection data listener
     */
    public void removeListener(FormDetectionDataListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method rotates scanned form.
     */
    public void rotateForm() {

        CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                .findMarkersByPositions(transformedImage, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * 2));

        double angle = GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(), coordinateSystem.getY());
        RotateImageCenterAlgorithm rica = new RotateImageCenterAlgorithm(angle);
        BufferedImage rotatedImage = rica.transform(transformedImage.toImage());
        transformedImage = ImageUtility.toBinary(rotatedImage, grayAlgorithm, binaryAlgorithm);
        fire(false);
    }

    /**
     * Method sets current form template.
     *
     * @param formTemplate
     *            form template parameters
     */
    public void setFormTemplate(FormTemplateParameters formTemplate) {
        ftp = formTemplate;
        formNode.setFormTemplateParameters(ftp);

    }

    /**
     * Method saves extracted regions to directory.
     *
     * @param directory
     *            destination directory
     */
    public void saveRegions(File directory) {
        // TODO Auto-generated method stub

    }

    /**
     * Method saves extracted segments to directory.
     *
     * @param directory
     *            destination directory
     */
    public void saveSegments(File directory) {
        // TODO Auto-generated method stub

    }
}
