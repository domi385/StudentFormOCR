package hr.fer.zemris.image.gui;

import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binarization.NiblackMethod;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binarization.SauvolaMethod;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.BlueChannelAlgorithm;
import hr.fer.zemris.image.grayscale.DesaturationAlgorithm;
import hr.fer.zemris.image.grayscale.EmptyGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.GreenChannelAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.LinearGrayscaleAlgorithm;
import hr.fer.zemris.image.grayscale.LuminanceAlgorithm;
import hr.fer.zemris.image.grayscale.MaximalDecompositionAlgorithm;
import hr.fer.zemris.image.grayscale.MinimalDecompositionAlgorithm;
import hr.fer.zemris.image.grayscale.RedChannelAlgorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Frame for comparing grayscale and binarization algorithms.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class GrayscaleAlgorithmComparisonGUI extends JFrame {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GrayscaleAlgorithmComparisonGUI::new);
    }

    /**
     * Image panels.
     */
    private ImagePanel ipOriginalImage, ipGrayImage, ipBinImage;
    /**
     * Grayscale algorithms.
     */
    private IGrayscaleAlgorithm[] grayscaleAlgorithms = { new LuminanceAlgorithm(), new AverageAlgorithm(),
            new RedChannelAlgorithm(), new GreenChannelAlgorithm(), new BlueChannelAlgorithm(),
            new DesaturationAlgorithm(), new LinearGrayscaleAlgorithm(), new MaximalDecompositionAlgorithm(),
            new MinimalDecompositionAlgorithm(), new EmptyGrayscaleAlgorithm() };
    /**
     * Combo box for choosing grayscale algorithm.
     */
    private JComboBox<IGrayscaleAlgorithm> cbGrayscaleAlgorithm;

    /**
     * Binarizaion algorithms.
     */
    private IBinarizationAlgorithm[] binarizeAlgorithms = { new OtsuAlgorithm(), new GlobalThresholdAlgorithm(1),
            new GlobalThresholdAlgorithm(30), new GlobalThresholdAlgorithm(120), new GlobalThresholdAlgorithm(200),
            new GlobalThresholdAlgorithm(250), new NiblackMethod(-0.2, 1), new NiblackMethod(-0.2, 2),
            new NiblackMethod(-0.2, 3), new NiblackMethod(-0.2, 4), new SauvolaMethod(0.2, 4),
            new SauvolaMethod(0.5, 5), new SauvolaMethod(0.2, 2), new SauvolaMethod(0.2, 1) };
    /**
     * Combo box for choosing binarization algorithm.
     */
    private JComboBox<IBinarizationAlgorithm> cbBinarizationAlgorithm;

    /**
     * Listener that listens for image loading action.
     */
    private ActionListener loadImageListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
            fc.setFileFilter(filter);
            fc.setDialogTitle("Open file");
            if (fc.showOpenDialog(GrayscaleAlgorithmComparisonGUI.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            Path openFilePath = fc.getSelectedFile().toPath();

            if (!Files.isReadable(openFilePath)) {
                JOptionPane.showMessageDialog(GrayscaleAlgorithmComparisonGUI.this, "File doesn't exist", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            displayImage(openFilePath);

        }

    };

    /**
     * Currently loaded image.
     */
    private BufferedImage openedImage;

    /**
     * Grayscaled instance of current image.
     */
    private BufferedImage grayImage;

    /**
     * Constructor that initializes frame.
     */
    public GrayscaleAlgorithmComparisonGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initGUI();
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * Method binarizes current image in new thread and displays it.
     */
    private void displayBinarizedImage() {
        if (openedImage == null) {
            return;
        }
        Thread t = new Thread(() -> {
            BufferedImage img = binarizeAlgorithms[cbBinarizationAlgorithm.getSelectedIndex()].toBinary(grayImage);
            SwingUtilities.invokeLater(() -> ipBinImage.setImage(img));
        });
        t.start();

    }

    /**
     * Method runs grayscale algorithm on an image and then displays it.
     */
    private void displayGrayscaleImage() {
        if (openedImage == null) {
            return;
        }
        Thread t = new Thread(() -> {
            grayImage = grayscaleAlgorithms[cbGrayscaleAlgorithm.getSelectedIndex()].toGrayscale(openedImage);
            SwingUtilities.invokeLater(() -> ipGrayImage.setImage(grayImage));
            displayBinarizedImage();
        });
        t.start();

    }

    /**
     * Method loads and displays choosed image.
     *
     * @param openFilePath
     *            path to the image file
     */
    private void displayImage(Path openFilePath) {
        Thread displayImage = new Thread(() -> {
            File originalFile = new File(openFilePath.toUri());
            try {
                openedImage = ImageIO.read(originalFile);
                SwingUtilities.invokeLater(() -> ipOriginalImage.setImage(openedImage));
                grayImage = grayscaleAlgorithms[cbGrayscaleAlgorithm.getSelectedIndex()].toGrayscale(openedImage);
                SwingUtilities.invokeLater(() -> ipGrayImage.setImage(grayImage));
                SwingUtilities.invokeLater(() -> ipBinImage.setImage(binarizeAlgorithms[cbBinarizationAlgorithm
                                                                                        .getSelectedIndex()].toBinary(grayImage)));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(GrayscaleAlgorithmComparisonGUI.this,
                        "Error while reading file."));
            }

        });
        displayImage.run();

    }

    /**
     * Method initializes frame GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        JPanel toolPanel = new JPanel();
        add(toolPanel, BorderLayout.PAGE_START);
        JButton loadImage = new JButton("Load");
        loadImage.addActionListener(loadImageListener);
        toolPanel.add(loadImage);

        cbGrayscaleAlgorithm = new JComboBox<IGrayscaleAlgorithm>(grayscaleAlgorithms);
        toolPanel.add(cbGrayscaleAlgorithm);
        cbGrayscaleAlgorithm.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                displayGrayscaleImage();
            }
        });

        cbBinarizationAlgorithm = new JComboBox<IBinarizationAlgorithm>(binarizeAlgorithms);
        toolPanel.add(cbBinarizationAlgorithm);
        cbBinarizationAlgorithm.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                displayBinarizedImage();
            }
        });

        JPanel imagesPanel = new JPanel(new GridLayout(1, 3));
        ipOriginalImage = new ImagePanel();
        ipOriginalImage.setBackground(Color.YELLOW);
        ipGrayImage = new ImagePanel();
        ipGrayImage.setBackground(Color.RED);
        ipBinImage = new ImagePanel();
        ipBinImage.setBackground(Color.BLUE);
        imagesPanel.add(ipOriginalImage);
        imagesPanel.add(ipGrayImage);
        imagesPanel.add(ipBinImage);

        add(imagesPanel, BorderLayout.CENTER);

    }

}
