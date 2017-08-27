package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class GrayscaleHistogramDemo {

    /**
     * Demo class private constructor.
     */
    private GrayscaleHistogramDemo() {
    }

    /**
     * Path to image for which is needed to calculate histogram.
     */
    private static String imagePath = "dataset5/200DPI/";

    public static void main(String[] args) throws IOException {

        List<int[]> histograms = new ArrayList<>();
        for (int i = 1; i < 23; i++) {
            File imageFile = new File(imagePath + "p" + String.format("%07d", i) + ".png");
            BufferedImage image = ImageIO.read(imageFile);
            IGrayscaleAlgorithm grayAlgorithm = new AverageAlgorithm();
            BufferedImage grayedImage = grayAlgorithm.toGrayscale(image);

            int[] histogram = ImageUtility.imageHistogram(grayedImage);
            histograms.add(histogram);
        }
        double dim = 0;
        for (int i = 0, end = histograms.get(0).length; i < end; i++) {
            dim += histograms.get(0)[i];
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 256; j++) {
            for (int i = 0, end = histograms.size(); i < end; i++) {
                sb.append(String.format("%f", histograms.get(i)[j] / dim).replace(",", ".") + "\t");

            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}
