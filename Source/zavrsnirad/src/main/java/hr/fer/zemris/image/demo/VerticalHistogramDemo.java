package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class calculates vertical histogram of an image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class VerticalHistogramDemo {

    /**
     * Private utility class constructor.
     */
    private VerticalHistogramDemo() {
    }

    /**
     * Path to image for which is needed to calculate histogram.
     */
    private static String imagePath = "hist/3.png";

    /**
     * Binarization threshold.
     */
    private static final int BINARIZATION_THRESHOLD = 250;

    /**
     * Method is called on program run.
     *
     * @param args
     *            arguments are ignored.
     * @throws IOException
     *             if there was an error while reading an image
     */
    public static void main(String[] args) throws IOException {

        File imageFile = new File(imagePath);
        BufferedImage image = ImageIO.read(imageFile);
        IBinaryImage binImage = ImageUtility.toBinary(image, new AverageAlgorithm(), new GlobalThresholdAlgorithm(
                BINARIZATION_THRESHOLD));
        double[] histogram = new double[binImage.getWidth()];

        for (int i = 0, end = binImage.getWidth(); i < end; i++) {
            int sum = 0;
            for (int j = 0, endHeight = binImage.getHeight(); j < endHeight; j++) {
                sum += binImage.getPixel(i, j) ? 0 : 1;
            }
            histogram[i] = sum / (double) binImage.getHeight();
        }

        for (int i = 0; i < histogram.length; i++) {
            System.out.println(histogram[i]);
        }
    }
}
