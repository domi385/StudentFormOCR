package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.geometry.Line;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.markerdetection.RegionMarkerDetector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class MarkerDetectorDemo {

    public static void main(String[] args) {
        File imageFile = new File("dataset5/200DPI/p0000001.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        IGrayscaleAlgorithm grayAlgorithm = new AverageAlgorithm();
        IBinarizationAlgorithm binAlgorithm = new OtsuAlgorithm();
        BinaryImage binImage = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(image)));
        RegionMarkerDetector detector = new RegionMarkerDetector(binImage);
        detector.detectMarkers();
        List<Region> markers = detector.getMarkers();

        for (Region r : markers) {
            System.out.println(r.getRegionCenter());
        }

        List<Point> centers = markers.stream().map(i -> i.getRegionCenter()).collect(Collectors.toList());
        Point p1, p2, p3;
        p1 = centers.get(0);
        p2 = centers.get(1);
        p3 = centers.get(2);

        Line l1, l2, l3;
        l1 = new Line(p1, p2);
        System.out.println(Math.toDegrees(GeometryUtility.getLineAngle(l1)));
        l2 = new Line(p1, p3);
        System.out.println(Math.toDegrees(GeometryUtility.getLineAngle(l2)));
        l3 = new Line(p2, p3);
        System.out.println(Math.toDegrees(GeometryUtility.getLineAngle(l3)));

    }

    /**
     * Private utility demo class constructor.
     */
    private MarkerDetectorDemo() {
    }
}
