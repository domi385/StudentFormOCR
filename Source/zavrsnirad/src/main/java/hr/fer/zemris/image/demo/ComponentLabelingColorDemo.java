package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.algorithms.ComponentLabelingAlgorithm;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Program demonstrates component labeling algorithm by coloring segments on image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class ComponentLabelingColorDemo {

    /**
     * Private utility class constructor.
     */
    private ComponentLabelingColorDemo() {
    }

    /**
     * Path to image for which is needed to run algorithm.
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

        List<Region> components = ComponentLabelingAlgorithm.getComponents(binImage);
        BufferedImage imageRes = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) imageRes.getGraphics();
        Color[] color = { Color.GRAY, Color.RED, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW };
        for (int i = 0; i < components.size(); i++) {
            g.setColor(color[i]);
            for (Point p : components.get(i).getPoints()) {
                g.drawLine(p.getX(), p.getY(), p.getX(), p.getY());
            }
        }
        g.dispose();
        ImageIO.write(imageRes, "png", new File("hist/hist_res3.png"));
    }
}
