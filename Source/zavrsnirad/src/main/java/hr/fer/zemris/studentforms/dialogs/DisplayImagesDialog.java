package hr.fer.zemris.studentforms.dialogs;

import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * Dialog for displaying list of images.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 26.5.2017.
 */
public class DisplayImagesDialog extends JDialog {
    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * List of images to display.
     */
    private List<BufferedImage> images;

    /**
     * Index of currently displayed image.
     */
    private int currIndex = 0;

    /**
     * Default dialog size.
     */
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 300);

    /**
     * Dialog constructor that accepts list of buffered images to display.
     *
     * @param images
     *            list of buffered images to be displayed
     */
    public DisplayImagesDialog(List<BufferedImage> images) {
        super();
        this.images = images;
        initGUI();
        pack();
        setSize(DEFAULT_SIZE);
    }

    /**
     * Dialog has free component space on bottom of the dialog so the user can add custom button.
     *
     * @param btnComponent
     *            button component
     */
    public void addLowerComponent(JButton btnComponent) {
        add(btnComponent, BorderLayout.PAGE_END);
    }

    /**
     * Method obtains currently selected image index.
     *
     * @return image index
     */
    public int getImageIndex() {
        return currIndex;
    }

    /**
     * Method initializes dialog GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        ImagePanel imgPanel = new ImagePanel();
        add(imgPanel, BorderLayout.CENTER);
        JButton prev = new JButton("<");
        prev.addActionListener(e -> {
            currIndex = (currIndex - 1 + images.size()) % images.size();
            imgPanel.setImage(images.get(currIndex));
        });
        add(prev, BorderLayout.LINE_START);
        JButton next = new JButton(">");
        next.addActionListener(e -> {
            currIndex = (currIndex + 1) % images.size();
            imgPanel.setImage(images.get(currIndex));
        });
        add(next, BorderLayout.LINE_END);
        imgPanel.setImage(images.get(currIndex));

    }
}
