package hr.fer.zemris.neural.demo;

import hr.fer.zemris.neural.FeedForwardNetwork;
import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.trainers.ClassicBackPropagationTrainer;
import hr.fer.zemris.neural.trainers.INeuralNetworkTrainer;

import java.util.Arrays;

/**
 * Demo class that shows neural network learining logic functions.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class LearningLogicFunctions {

    /**
     * Neural network learning rate.
     */
    private static final double LEARNING_RATE = 0.5;

    /**
     * Number of training epochs.
     */
    private static final int EPOCH_NUMBER = 500000;

    /**
     * Method initializes neural network.
     *
     * @param network
     *            current neural network
     */
    private static void initializeNetwork(INeuralNetwork network) {
        network.addLayer(2);
        network.addLayer(2);
        network.addLayer(1);
        network.setRandomWeights();
    }

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     */
    public static void main(String[] args) {
        INeuralNetwork network = new FeedForwardNetwork();
        initializeNetwork(network);
        INeuralNetworkTrainer trainer = new ClassicBackPropagationTrainer(network, LEARNING_RATE);

        System.out.println("\n\nLearning AND");
        double[][] andInputs = { { 0., 0. }, { 0., 1. }, { 1., 0. }, { 1., 1. } };
        double[][] andExpectedOutputs = { { 0 }, { 0 }, { 0 }, { 1 } };
        trainer.train(andInputs, andExpectedOutputs, EPOCH_NUMBER);
        printNetworkOutput(network, andInputs);

        network = new FeedForwardNetwork();
        initializeNetwork(network);
        trainer = new ClassicBackPropagationTrainer(network, LEARNING_RATE);
        System.out.println("\n\nLearning OR");
        double[][] orInputs = { { 0., 0. }, { 0., 1. }, { 1., 0. }, { 1., 1. } };
        double[][] orExpectedOutputs = { { 0 }, { 1 }, { 1 }, { 1 } };
        trainer.train(orInputs, orExpectedOutputs, EPOCH_NUMBER);
        printNetworkOutput(network, orInputs);

        network = new FeedForwardNetwork();
        initializeNetwork(network);
        trainer = new ClassicBackPropagationTrainer(network, LEARNING_RATE);
        System.out.println("\n\nLearning XOR");
        double[][] xorInputs = { { 0., 0. }, { 0., 1. }, { 1., 0. }, { 1., 1. } };
        double[][] xorExpectedOutputs = { { 0 }, { 1 }, { 1 }, { 0 } };
        trainer.train(xorInputs, xorExpectedOutputs, EPOCH_NUMBER);
        printNetworkOutput(network, xorInputs);
    }

    /**
     * Method prints output of a neural network for given inputs.
     *
     * @param network
     *            neural network
     * @param inputs
     *            neural network input
     */
    private static void printNetworkOutput(INeuralNetwork network, double[][] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            network.calcOutput(inputs[i]);
            System.out.println("Output " + i + ":");
            System.out.println(Arrays.toString(network.getNetworkOutput()));
        }

    }

    /**
     * Private utility class constructor.
     */
    private LearningLogicFunctions() {
    }
}
