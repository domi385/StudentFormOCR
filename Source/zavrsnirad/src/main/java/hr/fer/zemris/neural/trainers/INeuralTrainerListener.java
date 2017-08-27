package hr.fer.zemris.neural.trainers;

/**
 * Neural network training litener.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public interface INeuralTrainerListener {

    /**
     * Training status changed.
     *
     * @param trainingSetError
     *            training set error
     * @param validationSetError
     *            validation set error
     */
    void stateChanged(double trainingSetError, double validationSetError, int epochs);

}
