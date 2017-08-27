package hr.fer.zemris.neural.demo;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.binarization.OtsuAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.neural.FeedForwardNetwork;
import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.trainers.ClassicBackPropagationTrainer;
import hr.fer.zemris.neural.trainers.INeuralNetworkTrainer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Program demonstrates learning of a neural network to classify digit images of two digits.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 26.5.2017.
 */
public class DigitClassificationDemo {

    /**
     * Neural network learning rate.
     */
    private static final double LEARNING_RATE = 0.5;

    /**
     * Number of epochs for learning.
     */
    private static final int EPOCH_NUMBER = 1000;

    /**
     * Method centers given points in given square size.
     *
     * @param pixels
     *            black pixels points
     * @param height
     *            square height
     * @param width
     *            square width
     * @return centered points
     */
    private static List<Point> centerPoints(List<Point> pixels, int height, int width) {
        double averageX = (int) pixels.stream().mapToInt(i -> i.getX()).average().getAsDouble();
        double averageY = (int) pixels.stream().mapToInt(i -> i.getY()).average().getAsDouble();

        int deltaX = (int) (width / 2.d - averageX);
        int deltaY = (int) (height / 2.d - averageY);

        pixels.forEach(i -> i.setLocation(i.getX() + deltaX, i.getY() + deltaY));
        return pixels;
    }

    /**
     * Method initializes neural network with one input layer (2500 neurons), one hidden layer (50 neurons), 2 output
     * neurons.
     *
     * @return initialized neural network
     */
    private static INeuralNetwork initializeNetwork() {
        INeuralNetwork network = new FeedForwardNetwork();
        final int inputNeurons = 50 * 50;
        network.addLayer(inputNeurons);
        final int hiddenNeurons = 50;
        network.addLayer(hiddenNeurons);
        final int outputNeurons = 2;
        network.addLayer(outputNeurons);
        network.setRandomWeights();
        return network;
    }

    private static BufferedImage loadImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static double[] loadImageData(File file) {
        BufferedImage image = loadImage(file);
        IBinaryImage binImage = ImageUtility.toBinary(image, new AverageAlgorithm(), new OtsuAlgorithm());
        image.flush();

        int width = 50;
        int height = 50;
        List<Point> points = centerPoints(binImage.getPixels(false), height, width);

        int dim = width * height;
        double[] data = new double[dim];

        points.forEach(i -> data[i.getY() * height + i.getX()] = 1);
        return data;
    }

    private static List<double[]> loadImages(File directory) {
        File[] files = directory.listFiles();
        List<double[]> images = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            images.add(loadImageData(files[i]));
        }
        return images;
    }

    public static void main(String[] args) {
        List<double[]> imagesData0 = loadImages(new File("neural_dataset/training/0"));
        List<double[]> imagesData1 = loadImages(new File("neural_dataset/training/1"));
        double[][] inputs = new double[imagesData0.size() + imagesData1.size()][50 * 50];
        double[][] outputs = new double[imagesData0.size() + imagesData1.size()][2];

        for (int i = 0, end = imagesData0.size(); i < end; i++) {
            inputs[i] = imagesData0.get(i);
            outputs[i][0] = 1;
            outputs[i][1] = 0;
        }
        for (int i = 0, delta = imagesData0.size(), end = imagesData1.size(); i < end; i++) {
            inputs[i + delta] = imagesData1.get(i);
            outputs[i + delta][0] = 0;
            outputs[i + delta][1] = 1;
        }

        INeuralNetwork network = initializeNetwork();
        INeuralNetworkTrainer trainer = new ClassicBackPropagationTrainer(network, LEARNING_RATE);
        trainer.train(inputs, outputs, EPOCH_NUMBER);

        network.calcOutput(inputs[0]);
        System.out.println(Arrays.toString(network.getNetworkOutput()));
        network.calcOutput(inputs[imagesData0.size() + 1]);
        System.out.println(Arrays.toString(network.getNetworkOutput()));

    }

    private static void printImagesData(double[] imagesData, int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print((int) imagesData[i * width + j]);
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }

    /**
     * Private utility class constructor.
     */
    private DigitClassificationDemo() {
    }

}
