package hr.fer.zemris.image.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Panel for displaying an image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class ImagePanel extends JPanel {
    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Displayed image reference.
     */
    private BufferedImage image;

    /**
     * Image panel constructor.
     */
    public ImagePanel() {
        super();
    }

    /**
     * Constructor that initializes image that needs to be displayed.
     *
     * @param image
     *            image reference
     */
    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {

            Image dimg = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(dimg, 0, 0, null);
        }
    }

    /**
     * Method sets panel's image.
     *
     * @param image
     *            image to be displayed on the panel
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.paint(this.getGraphics());
    }
}
