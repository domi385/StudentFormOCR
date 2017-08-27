package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.gui.ImagePanel;
import hr.fer.zemris.image.transformation.RotateImageCenterAlgorithm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Program demonstrates rotation of an image in a frame. Use button r for transformation of 1 degree.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class RotateImageDemo {

    /**
     * Image file input reference.
     */
    private static final File IMAGE_FILE = new File("form.jpg");

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was an error while reading image
     */
    public static void main(String[] args) throws IOException {

        BufferedImage image = ImageIO.read(IMAGE_FILE);
        BufferedImage rotatedImage = (new RotateImageCenterAlgorithm(0)).transform(image);

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 0));
        ImagePanel panel = new ImagePanel();
        panel.setImage(rotatedImage);
        frame.addKeyListener(new KeyAdapter() {
            private double currAngle = 0;

            @Override
            public void keyTyped(KeyEvent e) {
                final double dphi = Math.PI / 180.;
                if (e.getKeyChar() == 'r') {
                    currAngle += dphi;

                    BufferedImage rotatedImage = new RotateImageCenterAlgorithm(currAngle).transform(image);
                    panel.setImage(rotatedImage);
                }
            }
        });
        frame.add(panel);
        final Dimension frameDimension = new Dimension(800, 600);
        frame.setSize(frameDimension);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Private utility class constructor.
     */
    private RotateImageDemo() {
    }

}
