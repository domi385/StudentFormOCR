package hr.fer.zemris.neural;

import java.io.Serializable;
import java.util.List;

/**
 * Neural network interface.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public interface INeuralNetwork extends Serializable {

    /**
     * Method adds layer to neural network.
     *
     * @param numberOfNeurons
     *            number of neurons in new layer
     */
    void addLayer(int numberOfNeurons);

    /**
     * Method calculates neural network error.
     *
     * @param expectedOutput
     *            expected neural network output
     */
    void calcError(double[] expectedOutput);

    /**
     * Method calculates output of a neural network.
     *
     * @param input
     *            neural network input
     */
    void calcOutput(double[] input);

    /**
     * Method obtains number of neurons contained in a layer.
     *
     * @param layerIndex
     *            layer index
     * @return number of neurons contained in a layer
     */
    double getLayerNeruonsNumber(int layerIndex);

    // TODO double to int

    /**
     *
     * @return
     */
    double[] getNetworkOutput();

    double[] getThresholdError();

    List<Double> getThresholdWeights();

    List<Double> getWeights();

    double[] getWeightsError();

    void setRandomWeights();

    void setThresholdWeights(List<Double> thresholdWeights);

    void setWeights(List<Double> weights);

}
