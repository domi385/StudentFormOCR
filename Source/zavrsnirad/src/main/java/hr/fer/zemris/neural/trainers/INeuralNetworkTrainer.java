package hr.fer.zemris.neural.trainers;

/**
 * Interface defines neural network trainer.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public interface INeuralNetworkTrainer {

    /**
     * Method adds listener to neural network trainer.
     *
     * @param listener
     *            neural network trainer
     */
    void addTrainerListener(INeuralTrainerListener listener);

    /**
     * Method removes trainer listener.
     *
     * @param listener
     *            training listener
     */
    void detachTrainerListener(INeuralTrainerListener listener);

    /**
     * Method trains neural network.
     *
     * @param inputs
     *            neural network inputs
     * @param expectedOutputs
     *            neural network expected outputs
     * @param validationInputs
     *            validation set inputs
     * @param validationExpectedOutputs
     *            validation set expected outputs
     */
    void train(double[][] inputs, double[][] expectedOutputs, double[][] validationInputs,
            double[][] validationExpectedOutputs);

    /**
     * Method trains neural network.
     *
     * @param inputs
     *            neural network inputs
     * @param expectedOutputs
     *            neural network expected outputs
     * @param validationInputs
     *            validation set inputs
     * @param validationExpectedOutputs
     *            validation set expected outputs
     * @param numOfEpochs
     *            number of training epochs
     * @param refreshInterval
     *            each refresh interval epoch all listeners should be notified about training progress
     */
    void train(double[][] inputs, double[][] expectedOutputs, double[][] validationInputs,
            double[][] validationExpectedOutputs, int numOfEpochs, int refreshInterval);

    /**
     * Method trains neural network.
     *
     * @param inputs
     *            neural network inputs
     * @param expectedOutputs
     *            neural network expected outputs
     * @param numOfEpochs
     *            number of training epochs
     */
    void train(double[][] inputs, double[][] expectedOutputs, int numOfEpochs);
}
