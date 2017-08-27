package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.SauvolaMethod;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.MinimalDecompositionAlgorithm;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Demo program that runs binarization algorithm and extracts regions from an image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class BinaryRegionDemo {

    /**
     * Private demo class constructor.
     */
    private BinaryRegionDemo() {
    }

    /**
     * Input directory path.
     */
    private static final String INPUT_DIR_PATH = "C:/Users/dplus/Desktop/binregion/";
    /**
     * Output directory path.
     */
    private static final File OUTPUT_DIR_PATH = new File("C:/Users/dplus/Desktop/binregion/");

    /**
     * Methods starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was an error while reading images or while writing results
     */
    public static void main(String[] args) throws IOException {
        IGrayscaleAlgorithm grayscaleAlgorithm = new MinimalDecompositionAlgorithm();
        File[] basicFiles = OUTPUT_DIR_PATH.listFiles();
        final IBinarizationAlgorithm[] binarizeAlgorithms = { new SauvolaMethod(0.1, 10) };

        final Rectangle[] rectangles = new Rectangle[] { new Rectangle(94, 99, 50, 49),
                new Rectangle(816, 1036, 122, 43), new Rectangle(793, 1092, 150, 52),
                new Rectangle(1218, 1153, 100, 49) };
        for (File file : basicFiles) {
            BufferedImage img = ImageIO.read(file);
            for (int i = 0; i < binarizeAlgorithms.length; i++) {
                IBinarizationAlgorithm alg = binarizeAlgorithms[i];
                BufferedImage binarizedImage = alg.toBinary(grayscaleAlgorithm.toGrayscale(img));
                for (int j = 0; j < rectangles.length; j++) {
                    String newFileName = INPUT_DIR_PATH + file.getName().substring(0, 1) + "_" + i + "_" + j + ".png";
                    Rectangle rect = rectangles[j];
                    ImageIO.write(binarizedImage.getSubimage(rect.x, rect.y, rect.width, rect.height), "png", new File(
                            newFileName));
                }

            }
        }
    }
}
