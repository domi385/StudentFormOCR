package hr.fer.zemris.studentforms.classification.data;

import hr.fer.zemris.studentforms.classification.ClassificationStatus;

/**
 * Classification data model listener.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 25.5.2017.
 */
public interface ClassificationDataModelListener {
    /**
     * Method is invoked when data model changes classification setup status.
     *
     * @param status
     *            classification setup status
     */
    void changeStatus(ClassificationStatus status);

    /**
     * Method is invoked when data model changes neural network.
     */
    void newNetworkLoaded();

    /**
     * Method is invoked on neural network progress update.
     *
     * @param trainingError
     *            training set error
     * @param validationError
     *            validation set error
     * @param epoch
     */
    void trainingProgressUpdate(double trainingError, double validationError, int epoch);
}
