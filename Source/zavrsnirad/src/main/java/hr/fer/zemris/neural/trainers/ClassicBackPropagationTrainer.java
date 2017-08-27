package hr.fer.zemris.neural.trainers;

import hr.fer.zemris.neural.INeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class models classic backpropagation training system.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 10.6.2017.
 */
public class ClassicBackPropagationTrainer implements INeuralNetworkTrainer {

    /**
     * Neural network that is trained.
     */
    private INeuralNetwork network;
    /**
     * Training learning rate.
     */
    private double learningRate;

    /**
     * List of training listeners.
     */
    private List<INeuralTrainerListener> listeners;

    /**
     * Training set error.
     */
    private List<Double> trainingError = new ArrayList<Double>();

    /**
     * Validation set error.
     */
    private List<Double> validationError = new ArrayList<Double>();

    /**
     * Constructor that initializes trainer with neural network and learning rate.
     *
     * @param network
     *            neural network
     * @param leariningRate
     *            learning rate
     */
    public ClassicBackPropagationTrainer(INeuralNetwork network, double leariningRate) {
        this.network = network;
        this.learningRate = leariningRate;
        listeners = new ArrayList<INeuralTrainerListener>();
    }

    /**
     * Method adds values from double array to list of doubles.
     *
     * @param list
     *            list of doubles
     * @param array
     *            double array
     */
    private void addArrayValuesToList(List<Double> list, double[] array) {

        for (int i = 0; i < array.length; i++) {
            list.set(i, list.get(i) + array[i]);
        }
    }

    @Override
    public void addTrainerListener(INeuralTrainerListener listener) {
        listeners.add(listener);
    }

    /**
     * Method calculates average network error for n examples.
     *
     * @param inputs
     *            array with input examples
     * @param expectedOutputs
     *            array with expected outputs
     * @return average network error
     */
    private Double calcAverageTotalNetworkError(double[][] inputs, double[][] expectedOutputs) {
        if (inputs.length != expectedOutputs.length || inputs.length < 1 || inputs[0].length < 1) {
            throw new IllegalArgumentException(
                    "Input and output array must be of same size and size must be greater than 0.");
        }

        double sum = 0;

        for (int i = 0; i < inputs.length; i++) {
            network.calcOutput(inputs[i]);
            sum += calculateNetworkTotalError(network.getNetworkOutput(), expectedOutputs[i]);
        }

        return sum / inputs.length;
    }

    /**
     * Method calculates total neural network error.
     *
     * @param givenOutput
     *            network output
     * @param expectedOutput
     *            expected network output
     * @return network error
     */
    private double calculateNetworkTotalError(double[] givenOutput, double[] expectedOutput) {
        if (givenOutput.length != expectedOutput.length) {
            System.out.println(Arrays.toString(givenOutput));
            System.out.println(Arrays.toString(expectedOutput));
            throw new IllegalArgumentException("Given arguments must be of same length.");
        }
        double totalError = 0;
        for (int i = 0; i < givenOutput.length; i++) {
            totalError += Math.pow(expectedOutput[i] - givenOutput[i], 2);
        }
        return totalError / 2;
    }

    @Override
    public void detachTrainerListener(INeuralTrainerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method notifies all training listeners.
     *
     * @param epoch
     *            current epoch
     */
    private void fire(int epoch) {
        listeners.forEach(i -> i.stateChanged(trainingError.get(trainingError.size() - 1),
                validationError.get(validationError.size() - 1), epoch));
    }

    @Override
    public void train(double[][] inputs, double[][] expectedOutputs, double[][] validationInputs,
            double[][] validationExpectedOutputs) {
        final double trainingStopFactor = 2;
        while (true) {
            trainOneEpoch(inputs, expectedOutputs);

            trainingError.add(calcAverageTotalNetworkError(inputs, expectedOutputs));
            validationError.add(calcAverageTotalNetworkError(validationInputs, validationExpectedOutputs));

            if (trainingError.get(trainingError.size() - 1) * trainingStopFactor < validationError.get(trainingError
                    .size() - 1)) {
                return;
            }
        }
    }

    @Override
    public void train(double[][] inputs, double[][] expectedOutputs, double[][] validationInputs,
            double[][] validationExpectedOutputs, int numOfEpochs, int refreshInterval) {

        for (int i = 0; i < numOfEpochs; i++) {

            trainOneEpoch(inputs, expectedOutputs);
            if (i % refreshInterval == 0) {
                trainingError.add(calcAverageTotalNetworkError(inputs, expectedOutputs));
                validationError.add(calcAverageTotalNetworkError(validationInputs, validationExpectedOutputs));
                fire(i);
            }

        }

    }

    @Override
    public void train(double[][] inputs, double[][] expectedOutputs, int numOfEpochs) {
        for (int i = 0; i < numOfEpochs; i++) {
            trainOneEpoch(inputs, expectedOutputs);
        }
    }

    /**
     * MEthod trains neural network for one epoch.
     *
     * @param inputs
     *            network inputs
     * @param expectedOutputs
     *            expected network outputs
     */
    public void trainOneEpoch(double[][] inputs, double[][] expectedOutputs) {

        List<Double> weights = network.getWeights();
        List<Double> thresholdWeights = network.getThresholdWeights();
        List<Double> weightsError = new ArrayList<>(Collections.nCopies(weights.size(), 0.));
        List<Double> thresholdError = new ArrayList<>(Collections.nCopies(thresholdWeights.size(), 0.));
        for (int j = 0; j < inputs.length; j++) {

            network.calcOutput(inputs[j]);
            network.calcError(expectedOutputs[j]);

            addArrayValuesToList(weightsError, network.getWeightsError());
            addArrayValuesToList(thresholdError, network.getThresholdError());
        }

        try {
            updateWeights(weights, thresholdWeights, weightsError, thresholdError, inputs.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        network.setWeights(weights);
        network.setThresholdWeights(thresholdWeights);
    }

    /**
     * Method updates neural network weights.
     *
     * @param weights
     *            weights between neurons
     * @param thresholdWeights
     *            neurons threshold weight
     * @param weightsError
     *            weights error
     * @param thresholdError
     *            neurons threshold error
     * @param numberOfSamples
     *            number of training samples
     */
    private void updateWeights(List<Double> weights, List<Double> thresholdWeights, List<Double> weightsError,
            List<Double> thresholdError, int numberOfSamples) {

        double eta = learningRate / numberOfSamples;
        for (int i = 0, end = weights.size(); i < end; i++) {
            weights.set(i, weights.get(i) + eta * weightsError.get(i));
        }

        for (int i = 0, end = thresholdWeights.size(); i < end; i++) {
            thresholdWeights.set(i, thresholdWeights.get(i) + eta * thresholdError.get(i));

        }

    }
}
