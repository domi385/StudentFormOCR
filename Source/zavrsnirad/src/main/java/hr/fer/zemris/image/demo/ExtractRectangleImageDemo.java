package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.algorithms.ExtractRectangleImageAlgorithm;
import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ExtractRectangleImageDemo {

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was an error while reading image
     */
    public static void main(String[] args) throws IOException {

        final int startx = 104, starty = 160;
        final int width = 100, height = 108;
        final int cols = 11, rows = 13;
        final int xoffset = 7 + 17, yoffset = 5 + 10;

        List<BufferedImage> cropedImages = new ArrayList<>();
        for (int k = 1; k <= 42; k++) {
            BufferedImage image = ImageIO.read(new File("dataset6/200DPI/p00000" + String.format("%02d", k) + ".png"));

            int currx = startx, curry = starty;

            for (int j = 0; j < rows; j++) {
                currx = startx;
                for (int i = 0; i < cols; i++) {

                    BufferedImage currCroped = ExtractRectangleImageAlgorithm.extract(image, new Rectangle(currx,
                            curry, width, height));
                    File outputfile = new File("dataset6/200DPI-r/" + k + "-" + j + "-" + i + ".png");
                    ImageIO.write(currCroped, "jpg", outputfile);
                    currx += width + xoffset;
                    cropedImages.add(currCroped);
                }

                curry += height + yoffset;

            }
        }

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 0));
        ImagePanel panel = new ImagePanel();
        panel.setImage(cropedImages.get(0));
        frame.addKeyListener(new KeyAdapter() {
            private int index = 0;

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'n') {
                    index = (index + 1 + cropedImages.size()) % cropedImages.size();
                    panel.setImage(cropedImages.get(index));
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
     * Private utility demo class constructor.
     */
    private ExtractRectangleImageDemo() {
    }

}
