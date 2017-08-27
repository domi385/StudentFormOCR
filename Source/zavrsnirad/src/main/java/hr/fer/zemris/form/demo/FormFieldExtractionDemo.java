package hr.fer.zemris.form.demo;

import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.FormTemplateUtility;
import hr.fer.zemris.form.MarkersUtility;
import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.algorithms.ComponentLabelingAlgorithm;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.geometry.RegionEdge;
import hr.fer.zemris.image.geometry.RegionUtility;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.MaximalDecompositionAlgorithm;
import hr.fer.zemris.image.transformation.I2DTransformationAlgorithm;
import hr.fer.zemris.image.transformation.RotateImageCenterAlgorithm;
import hr.fer.zemris.image.transformation.ScaleInSquareAlgorithm;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 * Class that demonstrates field extraction and segmentation from forms.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.5.2017.
 */
public class FormFieldExtractionDemo {

    /**
     * Form template file.
     */
    private static String formTemplatePath = "t4.ftp";

    /**
     * Region top offset when cutting out inside of a region.
     */
    private static final int TOP_OFFSET = 8;
    /**
     * Region bottom offset when cutting out inside of a region.
     */
    private static final int BOTTOM_OFFSET = 2;

    /**
     * Region start width offset when cutting out inside of a region.
     */
    private static final int START_OFFSET = 5;
    /**
     * Region end width offset when cutting out inside of a region.
     */
    private static final int END_OFFSET = 5;

    /**
     * Default segmented digit image square dimension.
     */
    private static final int SCALED_SEGMENT_DIM = 50;

    /**
     * Transformation that centers segment in a square.
     */
    private static I2DTransformationAlgorithm sctrans = new ScaleInSquareAlgorithm(SCALED_SEGMENT_DIM);

    /**
     * Method extracts fields and segments fields from given buffered image using given form template. It transforms
     * given image with given parameters.
     *
     * @param ftp
     *            form template
     * @param formImage
     *            form image
     * @param deltax
     *            translation x coordinate value
     * @param deltay
     *            translation y coordinate value
     * @param scalex
     *            scaling factor for x coordinate
     * @param scaley
     *            scaling factor for y coordinate
     * @param formNumber
     *            form number
     * @param coord
     *            current form coordinate system
     * @throws IOException
     *             if there was an error while saving output fields
     */
    private static void extractFields(FormTemplateParameters ftp, BufferedImage formImage, int deltax, int deltay,
            double scalex, double scaley, int formNumber, CoordinateSystem2D coord) throws IOException {
        // System.out.println(formNumber);
        List<RegionEdge> pointFields = ftp.getPointFields();
        int i = 0;
        for (RegionEdge field : pointFields) {

            try {
                Rectangle fieldRectangle = transformFieldRectangle(field.getBoundingRectange(), ftp, coord);

                BufferedImage fieldImage = formImage.getSubimage(fieldRectangle.x + START_OFFSET, fieldRectangle.y
                        + TOP_OFFSET, (fieldRectangle.width) - START_OFFSET - END_OFFSET, (fieldRectangle.height)
                        - TOP_OFFSET - BOTTOM_OFFSET);

                // BufferedImage fieldImage = formImage.getSubimage(fieldRectangle.x + deltax + WIDTH_OFFSET,
                // fieldRectangle.y + deltay + HEIGHT_OFFSET, (int) (fieldRectangle.width * scalex) - 2
                // * WIDTH_OFFSET, (int) (fieldRectangle.height * scaley) - 2 * HEIGHT_OFFSET);
                File outputFile = new File("extractedFields/200DPI/field_" + String.format("%03d", i) + "/"
                        + String.format("%02d", formNumber) + ".jpg");
                outputFile.getParentFile().mkdirs();
                ImageIO.write(fieldImage, "jpg", outputFile);
                extractRegions(fieldImage, i, formNumber);

            } catch (RasterFormatException e) {
                System.err.println(e.getMessage());
                continue;
            }

            i++;
        }
    }

    /**
     * Method transforms form template rectangle to current form rectangle.
     *
     * @param boundingRectange
     *            form template points bounding rectangle
     * @param ftp
     *            form template parameters
     * @param coord
     *            current form coordinate system
     * @return current form template points bounding rectangle
     */
    private static Rectangle transformFieldRectangle(Rectangle boundingRectange, FormTemplateParameters ftp,
            CoordinateSystem2D coord) {
        double angle = -GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(), coord.getY());
        double scalex = coord.getX().calcLength() / (double) ftp.getCoordinateSystem().getX().calcLength();
        double scaley = coord.getY().calcLength() / (double) ftp.getCoordinateSystem().getY().calcLength();

        double x;
        double y;

        // upper left point
        double x0 = boundingRectange.x * scalex;
        double y0 = boundingRectange.y * scaley;

        x = x0 - ftp.getCoordinateSystem().getOrigin().getX();
        y = y0 - ftp.getCoordinateSystem().getOrigin().getY();

        x0 = x * Math.cos(angle) + y * Math.sin(angle);
        y0 = -x * Math.sin(angle) + y * Math.cos(angle);

        x = x0 + coord.getOrigin().getX();
        y = y0 + coord.getOrigin().getY();

        x0 = x;
        y0 = y;
        // lower left point

        double x1 = (boundingRectange.x + boundingRectange.width) * scalex;
        double y1 = (boundingRectange.y + boundingRectange.height) * scaley;

        x = x1 - ftp.getCoordinateSystem().getOrigin().getX();
        y = y1 - ftp.getCoordinateSystem().getOrigin().getY();

        x1 = x * Math.cos(angle) + y * Math.sin(angle);
        y1 = -x * Math.sin(angle) + y * Math.cos(angle);

        x = x1 + coord.getOrigin().getX();
        y = y1 + coord.getOrigin().getY();

        x1 = x;
        y1 = y;
        // Point p1 = new Point((int) x0, (int) y0);
        // Point p2 = new Point((int) x1, (int) y1);
        // System.out.println(p1 + " " + p2);

        return new Rectangle((int) x0, (int) y0, (int) (x1 - x0), (int) (y1 - y0));
    }

    /**
     * Method extract regions from a field.
     *
     * @param fieldImage
     *            field image
     *
     * @param fieldNumber
     *            field number
     * @param formNumber
     *            form number
     * @throws IOException
     *             if there was an error while writing field to file system
     */
    private static void extractRegions(BufferedImage fieldImage, int fieldNumber, int formNumber) throws IOException {
        List<Region> components = ComponentLabelingAlgorithm.getComponents(ImageUtility.defalutToBinary(fieldImage));

        components = components.stream().filter(createAdvancedFilter(fieldImage)).collect(Collectors.toList());
        // components = mergeVerticalyCloseComponents(components);
        for (int i = 0; i < components.size(); i++) {
            File outputFile = new File("extractedRegions/200DPI/field_" + String.format("%03d", fieldNumber) + "/"
                    + String.format("%03d", formNumber) + "/" + String.format("%02d", i) + ".jpg");

            BufferedImage outputImage = sctrans.transform(RegionUtility.regionToBinaryImage(components.get(i))
                    .toImage());
            outputFile.getParentFile().mkdirs();
            ImageIO.write(outputImage, "jpg", outputFile);
        }

    }

    /**
     * Method creates filter for filtering segments.
     *
     * @param fieldImage
     *            image containing segments
     * @return prdicate for filtering segments
     */
    private static Predicate<Region> createFilter(BufferedImage fieldImage) {
        final int minDim = 3;
        final int minPoints = 15;
        return i -> i.getBoundingRectangle().width > minDim && i.getBoundingRectangle().height > minDim
                && i.getPoints().size() > minPoints;
    }

    private static Predicate<Region> createAdvancedFilter(BufferedImage fieldImage) {

        int[] histogram = ImageUtility.calcVerticalHistogram(fieldImage);
        double[] probHistogram = new double[histogram.length];
        double sum = 0;
        double maxProb = 0;
        int yMax = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > maxProb) {
                maxProb = histogram[i];
                yMax = i;
            }
            sum += histogram[i];
        }
        maxProb = maxProb / sum;

        for (int i = 0; i < histogram.length; i++) {
            probHistogram[i] = (histogram[i] / sum);
        }
        int yup = histogram.length;
        int ylow = 0;

        for (int i = yMax; i < probHistogram.length; i++) {
            if (i < yup && 4 * probHistogram[i] <= maxProb) {
                yup = i;
            }
        }

        for (int i = 0; i < yMax; i++) {
            if (i > ylow && 3 * probHistogram[i] <= maxProb) {
                ylow = i;
            }
        }
        final int ylower = ylow;
        final int yupper = yup;
        Predicate<Region> filter = i -> {

            Rectangle bound = i.getBoundingRectangle();
            Point center = i.getRegionCenter();

            // filter edges
            if (bound.x + bound.width < 5 && bound.height > fieldImage.getHeight() / 3) {
                return false;
            }
            if (bound.x + bound.width >= fieldImage.getWidth() - 5 && bound.height > fieldImage.getHeight() / 3) {
                return false;
            }
            if (bound.y + bound.height < ylower && bound.width > fieldImage.getWidth() / 3) {
                return false;
            }
            if (bound.y + bound.height >= yupper && bound.width > fieldImage.getWidth() / 3) {
                return false;
            }

            if (center.getY() > ylower && center.getY() < yupper) {
                final int minDim = 2;
                if (bound.width < minDim || bound.height < minDim) {
                    return false;
                }
            }

            // ukloni premale
            final int minPoints = 4;
            if (i.getPoints().size() < minPoints) {
                return false;
            }
            return true;
        };

        return filter;

    }

    /**
     * Method checks if two regions are vertically close.
     *
     * @param currentRegion
     *            current region
     * @param component
     *            neighbor region
     * @return true if the regions are close, false otherwise
     */
    private static boolean isRegionVerticalyClose(Region currentRegion, Region component) {
        Rectangle fRect = currentRegion.getBoundingRectangle();
        Rectangle sRect = currentRegion.getBoundingRectangle();

        int xFirstCenter = fRect.x + fRect.width / 2;
        if (xFirstCenter > sRect.x && xFirstCenter <= sRect.x + sRect.width) {
            return true;
        }

        int xSecondCenter = sRect.x + sRect.width / 2;
        if (xSecondCenter > fRect.x && xSecondCenter <= fRect.x + fRect.width) {
            return true;
        }
        return false;
    }

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was an exception while reading file
     * @throws ClassNotFoundException
     *             if there was exception while parsing form template file
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        FormTemplateParameters ftp = FormTemplateUtility.readTemplateFromFile(new File(formTemplatePath));

        IBinarizationAlgorithm binAlgorithm = new GlobalThresholdAlgorithm(250);
        IGrayscaleAlgorithm grayAlgorithm = new MaximalDecompositionAlgorithm();

        for (int i = 1; i < 23; i++) {
            // System.out.println(i);
            BufferedImage formImage = ImageIO.read(new File("dataset5/200DPI/p" + String.format("%07d", i) + ".png")); //
            // System.out.println("LOAD");
            BinaryImage binarizedForm = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(formImage)));
            // System.out.println("Binarize");
            // File outputBinary = new File("dataset5_bin/200DPI/p" + String.format("%07d", i) + ".jpg");
            // outputBinary.getParentFile().mkdirs();
            // ImageIO.write(binarizedForm.toImage(), "jpg", outputBinary);

            CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                    .findMarkersByPositions(binarizedForm, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * 2)); //

            double angle = GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(),
                    coordinateSystem.getY());
            // System.out.println("A");
            RotateImageCenterAlgorithm rica = new RotateImageCenterAlgorithm(angle);
            BufferedImage rotatedImage = rica.transform(formImage);
            binarizedForm = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(rotatedImage)));
            rotatedImage = binarizedForm.toImage();

            // File outputBinary2 = new File("dataset5_bin/200DPI/b" + String.format("%07d", i) + ".jpg");

            // outputBinary.getParentFile().mkdirs();
            // ImageIO.write(binarizedForm.toImage(), "jpg", outputBinary2);
            final int markerSizeFactor = 3;
            coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility.findMarkersByPositions(
                    binarizedForm, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * markerSizeFactor));
            // System.out.println("B");
            int deltax = coordinateSystem.getOrigin().getX() - ftp.getCoordinateSystem().getOrigin().getX();
            int deltay = coordinateSystem.getOrigin().getY() - ftp.getCoordinateSystem().getOrigin().getY();
            double scalex = coordinateSystem.getX().calcLength()
                    / (double) ftp.getCoordinateSystem().getX().calcLength();
            double scaley = coordinateSystem.getY().calcLength()
                    / (double) ftp.getCoordinateSystem().getY().calcLength();
            // System.out.println("C");
            extractFields(ftp, rotatedImage, deltax, deltay, scalex, scaley, i, coordinateSystem);
        }
    }

    /**
     * Method merges given regions to one region.
     *
     * @param currentRegion
     *            original region
     * @param closeRegions
     *            regions that need to be added to original
     * @return merged region
     */
    private static Region mergeComponents(Region currentRegion, List<Region> closeRegions) {
        if (closeRegions.size() == 0) {
            return currentRegion;
        }
        for (Region component : closeRegions) {
            currentRegion = RegionUtility.mergeRegions(currentRegion, component);
        }
        return currentRegion;
    }

    /**
     * Method merges vertically close components.
     *
     * @param components
     *            list of regions
     * @return merged regions
     */
    public static List<Region> mergeVerticalyCloseComponents(List<Region> components) {

        if (components.size() == 1) {
            return components;
        }
        Set<Region> unvisitedRegions = new HashSet<>(components);
        List<Region> newComponentsList = new ArrayList<Region>(components.size());
        for (int i = 0, end = components.size() - 2; i < end; i++) {
            if (!unvisitedRegions.contains(components.get(i))) {
                continue;
            }
            Region currentRegion = components.get(i);
            unvisitedRegions.remove(currentRegion);
            List<Region> closeRegions = new ArrayList<>();
            for (Region component : components) {
                if (isRegionVerticalyClose(currentRegion, component)) {
                    closeRegions.add(component);
                    unvisitedRegions.remove(component);
                }
            }
            Region mergedRegion = mergeComponents(currentRegion, closeRegions);
            newComponentsList.add(mergedRegion);
        }
        return newComponentsList;
    }

    /**
     * Private utility demo class constructor.
     */
    private FormFieldExtractionDemo() {
    }
}
