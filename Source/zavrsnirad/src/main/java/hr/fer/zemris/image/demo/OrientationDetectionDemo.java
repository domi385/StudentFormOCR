package hr.fer.zemris.image.demo;

import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.FormTemplateUtility;
import hr.fer.zemris.form.MarkersUtility;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.MaximalDecompositionAlgorithm;
import hr.fer.zemris.image.transformation.RotateImageCenterAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OrientationDetectionDemo {

    private static String pathName = "dataset5/200DPI/";
    /**
     * Form template file.
     */
    private static String formTemplatePath = "t4.ftp";

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws ClassNotFoundException
     *             if there was a problem while reading form template
     * @throws IOException
     *             if there was a problem while reading a file
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        FormTemplateParameters ftp = FormTemplateUtility.readTemplateFromFile(new File(formTemplatePath));
        System.out.println(ftp.getPointFields().get(0).getBoundingRectange());
        IBinarizationAlgorithm binAlgorithm = new GlobalThresholdAlgorithm(250);
        IGrayscaleAlgorithm grayAlgorithm = new MaximalDecompositionAlgorithm();

        int i = 1;
        System.out.println(ftp.getCoordinateSystem());
        BufferedImage formImage = ImageIO.read(new File("dataset5/200DPI/p" + String.format("%07d", i) + ".png")); //

        BinaryImage binarizedForm = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(formImage)));
        CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                .findMarkersByPositions(binarizedForm, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * 2)); //
        System.out.println(coordinateSystem);
        double angle = GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(), coordinateSystem.getY());

        RotateImageCenterAlgorithm rica = new RotateImageCenterAlgorithm(angle);
        BufferedImage rotatedImage = rica.transform(formImage);
        binarizedForm = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(rotatedImage)));
        rotatedImage = binarizedForm.toImage();

        final int markersScaleFactor = 3;
        coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility.findMarkersByPositions(
                binarizedForm, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * markersScaleFactor));

        int deltax = coordinateSystem.getOrigin().getX() - ftp.getCoordinateSystem().getOrigin().getX();
        int deltay = coordinateSystem.getOrigin().getY() - ftp.getCoordinateSystem().getOrigin().getY();

        double scalex = coordinateSystem.getX().calcLength() / (double) ftp.getCoordinateSystem().getX().calcLength();
        double scaley = coordinateSystem.getY().calcLength() / (double) ftp.getCoordinateSystem().getY().calcLength();

    }

    private static void calcAngleProperties() throws IOException, ClassNotFoundException {
        FormTemplateParameters ftp = FormTemplateUtility.readTemplateFromFile(new File(formTemplatePath));

        IBinarizationAlgorithm binAlgorithm = new GlobalThresholdAlgorithm(250);
        IGrayscaleAlgorithm grayAlgorithm = new MaximalDecompositionAlgorithm();

        double sumAngle = 0;
        double maxAngle = 0;
        for (int i = 1; i < 23; i++) {
            BufferedImage formImage = ImageIO.read(new File("dataset5/200DPI/p" + String.format("%07d", i) + ".png")); //

            BinaryImage binarizedForm = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(formImage)));
            CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                    .findMarkersByPositions(binarizedForm, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * 2)); //

            double angle = GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(),
                    coordinateSystem.getY());
            sumAngle += angle;
            if (angle < maxAngle) {
                maxAngle = angle;
            }
        }
        System.out.println(sumAngle);
        System.out.println(Math.toDegrees(sumAngle / 22));
        System.out.println("Max: " + Math.toDegrees(maxAngle));
    }
}
