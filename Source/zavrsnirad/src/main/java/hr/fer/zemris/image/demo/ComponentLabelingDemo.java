package hr.fer.zemris.image.demo;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.algorithms.ComponentLabelingAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.gui.ImagePanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Domagoj Pluscec
 * @version
 */
public class ComponentLabelingDemo {

    public static void main(String[] args) {
        File imageFile = new File("extractedFields/200DPI/field_007/08.jpg");
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int radius = 1;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = 0.11f;
        }
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        // tbi is BufferedImage
        BufferedImage i = op.filter(image, null);

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        ImagePanel panel = new ImagePanel();
        panel.setImage(image);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(panel);

        ImagePanel panel2 = new ImagePanel();
        panel2.setImage(ImageUtility.defalutToBinary(i).toImage());
        frame.add(panel2);

        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        IBinaryImage binImage = ImageUtility.defalutToBinary(i);

        List<Region> components = ComponentLabelingAlgorithm.getComponents(binImage);
        for (Region component : components) {
            System.out.println(component);
        }
    }

    /**
     * Private utility demo class constructor.
     */
    private ComponentLabelingDemo() {
    }

}
