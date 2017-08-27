package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.binary.BinaryImageUtility;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Program demonstrates how to crop a binary image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 26.5.2017.
 */
public class CropBinaryImageDemo {

    /**
     * Method starts with program run.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(String[] args) {
        File imageFile = new File("extractedFields/200DPI/field_007/04.jpg");
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IGrayscaleAlgorithm grayAlgorithm = new AverageAlgorithm();
        IBinarizationAlgorithm binAlgorithm = new OtsuAlgorithm();
        BinaryImage binImage = new BinaryImage(binAlgorithm.toBinary(grayAlgorithm.toGrayscale(image)));

        IBinaryImage verticalCroped = BinaryImageUtility.cropWithThreshold(binImage, 0);
        BufferedImage vertCropImg = verticalCroped.toImage();

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        ImagePanel panel = new ImagePanel();
        panel.setImage(image);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(panel);

        ImagePanel panel2 = new ImagePanel();
        panel2.setImage(vertCropImg);
        frame.add(panel2);

        final Dimension frameSize = new Dimension(800, 600);
        frame.setSize(frameSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     * Private utility demo class constructor.
     */
    private CropBinaryImageDemo() {
    }
}
