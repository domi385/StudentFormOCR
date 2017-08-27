package hr.fer.zemris.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class models feed forward neural network.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class FeedForwardNetwork implements INeuralNetwork {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Number of neurons in each layer.
     */
    private List<Integer> numberOfNeurons = new ArrayList<>();
    /**
     * Neurons outputs error.
     */
    private double[] outputError;
    /**
     * Start index of a neuron in each layer.
     */
    private List<Integer> outputLayerIndex = new ArrayList<Integer>();

    /**
     * Neurons outputs.
     */
    private List<Double> outputs = new ArrayList<>();
    /**
     * Neurons thresholds error.
     */
    private double[] thresholdError;
    /**
     * Neurons threshold weights.
     */
    private List<Double> thresholdWeights = new ArrayList<>();

    /**
     * Transfer function.
     */
    private ITransitionFunction<Double, Double> transitionFunction = (x) -> {
        return 1. / (1 + Math.exp(-x));

    };

    /**
     * Neurons weights error.
     */
    private double[] weightError;

    /**
     * Neruons weights.
     */
    private List<Double> weights = new ArrayList<>();

    /**
     * Index of starting weight for every layer.
     */
    private List<Integer> weightsLayerIndex = new ArrayList<>();

    @Override
    public void addLayer(int numberOfNeurons) {
        this.numberOfNeurons.add(numberOfNeurons);
        int numOfWeightsPerNeuron = this.numberOfNeurons.size() == 1 ? 0 : this.numberOfNeurons
                .get(this.numberOfNeurons.size() - 2);
        outputLayerIndex.add(outputs.size());

        for (int i = 0; i < numberOfNeurons; i++) {
            outputs.add(0.);
            for (int j = 0; j < numOfWeightsPerNeuron; j++) {
                weights.add(0.);
            }
        }
        if (this.numberOfNeurons.size() > 1) {
            for (int i = 0; i < numberOfNeurons; i++) {
                thresholdWeights.add(0.);
            }
        }
        weightsLayerIndex.add(weights.size());

    }

    @Override
    public void calcError(double[] expectedOutput) {
        weightError = new double[weights.size()];
        thresholdError = new double[thresholdWeights.size()];

        calcOutputError(expectedOutput);
        int layersNum = numberOfNeurons.size();

        for (int layer = 1; layer < layersNum; layer++) {
            for (int currNeuron = 0, endCurrNeuron = numberOfNeurons.get(layer); currNeuron < endCurrNeuron; currNeuron++) {
                for (int prevNeuron = 0, endPrevNeuron = numberOfNeurons.get(layer - 1); prevNeuron < endPrevNeuron; prevNeuron++) {
                    int weightInd = weightsLayerIndex.get(layer - 1) + numberOfNeurons.get(layer) * prevNeuron
                            + currNeuron;

                    weightError[weightInd] = getNeuronOutput(layer - 1, prevNeuron)
                            * (outputError[outputLayerIndex.get(layer) + currNeuron]);
                }
            }
        }
        for (int i = 0, end = outputError.length - numberOfNeurons.get(0); i < end; i++) {
            thresholdError[i] = outputError[numberOfNeurons.get(0) + i];
        }

    }

    @Override
    public void calcOutput(double[] input) {
        if (input.length != numberOfNeurons.get(0)) {
            throw new IllegalArgumentException("Illegal input size");
        }
        for (int i = 0; i < numberOfNeurons.get(0); i++) {
            outputs.set(i, input[i]);
        }
        int neuronsNum = 0;
        int weightsNum = 0;
        for (int i = 1, layersNum = numberOfNeurons.size(); i < layersNum; i++) {
            int outputNum = neuronsNum + numberOfNeurons.get(i - 1);
            for (int j = 0, thisLayerNeurons = numberOfNeurons.get(i); j < thisLayerNeurons; j++) {
                int x = j;
                double sum = 0;
                for (int k = 0, inputNeurons = numberOfNeurons.get(i - 1); k < inputNeurons; k++) {
                    sum += weights.get(weightsNum + x) * outputs.get(neuronsNum + k);
                    x += numberOfNeurons.get(i);
                }
                sum += thresholdWeights.get(outputNum + j - numberOfNeurons.get(0));
                outputs.set(outputNum + j, transitionFunction.apply(sum));
            }
            neuronsNum += numberOfNeurons.get(i - 1);
            weightsNum = weightsLayerIndex.get(i); // +=numberOfNeurons.get(i - 1) + numberOfNeurons.get(i) + 1;
        }

    }

    /**
     * Method calculates output error of a neural network.
     *
     * @param expectedOutput
     *            expected network output
     */
    private void calcOutputError(double[] expectedOutput) {

        int neuronsNum = outputs.size();
        int layersNum = numberOfNeurons.size();

        // calc output error delta of output neuron layer
        outputError = new double[outputs.size()];
        double[] networkOutput = getNetworkOutput();

        for (int currLayer = 0, currLayerEnd = numberOfNeurons.get(layersNum - 1); currLayer < currLayerEnd; currLayer++) {
            int outputInd = outputLayerIndex.get(layersNum - 1) + currLayer;
            outputError[outputInd] = networkOutput[currLayer] * (1 - networkOutput[currLayer])
                    * (expectedOutput[currLayer] - networkOutput[currLayer]);

        }

        for (int layer = numberOfNeurons.size() - 2; layer > 0; layer--) {
            for (int neuronInd = 0, endInd = numberOfNeurons.get(layer); neuronInd < endInd; neuronInd++) {

                double nextLayerError = 0;
                for (int nextNeuronInd = 0, nextNeuronEnd = numberOfNeurons.get(layer + 1); nextNeuronInd < nextNeuronEnd; nextNeuronInd++) {
                    nextLayerError += getConnectionWeight(layer, neuronInd, nextNeuronInd)
                            * outputError[outputLayerIndex.get(layer + 1) + nextNeuronInd];

                }
                int outputInd = outputLayerIndex.get(layer) + neuronInd;
                outputError[outputInd] = getNeuronOutput(layer, neuronInd) * (1 - getNeuronOutput(layer, neuronInd))
                        * nextLayerError;
            }
        }

    }

    /**
     * Method obtains weight of a connection defined with output neuron and input neuron.
     *
     * @param outputLayer
     *            layer which contains neuron from which connection start
     * @param outputNeuron
     *            neuron from which connection start
     * @param inputNeuron
     *            neuron that ends connection
     * @return value of connection weight
     */
    public double getConnectionWeight(int outputLayer, int outputNeuron, int inputNeuron) {
        return weights.get(weightsLayerIndex.get(outputLayer) + numberOfNeurons.get(outputLayer + 1) * outputNeuron
                + inputNeuron);
    }

    @Override
    public double getLayerNeruonsNumber(int layerIndex) {
        return numberOfNeurons.get(layerIndex);
    }

    @Override
    public double[] getNetworkOutput() {
        double[] output = new double[numberOfNeurons.get(numberOfNeurons.size() - 1)];
        for (int i = outputs.size() - output.length, end = outputs.size(), j = 0; i < end; i++, j++) {

            output[j] = outputs.get(i);
        }
        return output;
    }

    /**
     * Method obtains neuron output.
     *
     * @param layer
     *            neuron layer
     * @param neuron
     *            neuron index
     * @return value of neuron output
     */
    public double getNeuronOutput(int layer, int neuron) {
        return outputs.get(outputLayerIndex.get(layer) + neuron);
    }

    /**
     * Method obtains output error.
     *
     * @return output error
     */
    public double[] getOutputsError() {
        return outputError;
    }

    @Override
    public double[] getThresholdError() {
        return thresholdError;
    }

    @Override
    public List<Double> getThresholdWeights() {
        return thresholdWeights;
    }

    @Override
    public List<Double> getWeights() {
        return weights;
    }

    @Override
    public double[] getWeightsError() {
        return weightError;
    }

    @Override
    public void setRandomWeights() {
        Random random = ThreadLocalRandom.current();
        final double factor = 2.4 / (numberOfNeurons.get(0) + 1);
        for (int i = 0, end = weights.size(); i < end; i++) {
            weights.set(i, (random.nextDouble() * 2 - 1) * factor);
        }
        for (int i = 0, end = thresholdWeights.size(); i < end; i++) {
            thresholdWeights.set(i, (random.nextDouble() * 2 - 1) * factor);
        }
    }

    /**
     * Method sets threshold weight.
     *
     * @param neuronIndex
     *            neuron index
     * @param weight
     *            new weight value
     */
    public void setThresholdWeight(int neuronIndex, double weight) {
        thresholdWeights.set(neuronIndex - numberOfNeurons.get(0), weight);
    }

    @Override
    public void setThresholdWeights(List<Double> thresholdWeights) {
        this.thresholdWeights = thresholdWeights;
    }

    /**
     * Method sets connection weight.
     *
     * @param weightIndex
     *            weight index
     * @param weight
     *            new weight value
     */
    public void setWeight(int weightIndex, double weight) {
        weights.set(weightIndex, weight);
    }

    @Override
    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }

}
