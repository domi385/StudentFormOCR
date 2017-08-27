package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.algorithms.HoughLinesAlgorithm;
import hr.fer.zemris.image.binary.BinaryImage;
import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program demonstrates hough line detecting algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class HoughDemo {

    /**
     * Method start with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was an exception while reading form file
     */
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                File imageFile = new File("form.jpg");
                BufferedImage image = null;
                try {
                    image = ImageIO.read(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BinaryImage binImage = new BinaryImage(image);

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
    private HoughDemo() {
    }
}
