package hr.fer.zemris.image.demo;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GrayRegionDemo {

    static String dirName = "C:/Users/dplus/Desktop/graregion/";
    static File dirPath = new File("C:/Users/dplus/Desktop/graregion/");

    public static void main(String[] args) throws IOException {
        IGrayscaleAlgorithm[] grayscaleAlgorithms = { new LuminanceAlgorithm(), new AverageAlgorithm(),
                new RedChannelAlgorithm(), new GreenChannelAlgorithm(), new BlueChannelAlgorithm(),
                new DesaturationAlgorithm(), new LinearGrayscaleAlgorithm(), new MaximalDecompositionAlgorithm(),
                new MinimalDecompositionAlgorithm(), new EmptyGrayscaleAlgorithm() };
        File[] basicFiles = dirPath.listFiles();

        for (File file : basicFiles) {
            BufferedImage img = ImageIO.read(file);
            for (IGrayscaleAlgorithm alg : grayscaleAlgorithms) {
                String newFileName = dirName + file.getName().substring(0, 1) + alg.toString() + ".png";
                ImageIO.write(alg.toGrayscale(img), "png", new File(newFileName));
            }
        }
    }
}
