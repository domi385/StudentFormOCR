package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.algorithms.HoughLinesAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class HoughAlgorithmDemo {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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

                HoughLinesAlgorithm hough = new HoughLinesAlgorithm(binImage);
                BufferedImage plot = hough.accumulatorToImage();
                for (int i = 0, end = hough.getLines().size(); i < end; i++) {
                    System.out.println(hough.getLines().get(i));
                }

                JFrame frame = new JFrame();
                frame.setLayout(new GridLayout(1, 0));
                ImagePanel panel = new ImagePanel();
                panel.setImage(plot);

                frame.add(panel);
                frame.setSize(800, 600);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            }
        });
    }

    /**
     * Private utility demo class constructor.
     */
    private HoughAlgorithmDemo() {
    }
}
