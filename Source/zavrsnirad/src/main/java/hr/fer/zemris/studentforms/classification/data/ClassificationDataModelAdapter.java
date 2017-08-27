package hr.fer.zemris.studentforms.classification.data;

import hr.fer.zemris.studentforms.classification.ClassificationStatus;

/**
 * Classification data model listener that offers empty implementations.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 25.5.2017.
 */
public class ClassificationDataModelAdapter implements ClassificationDataModelListener {

    @Override
    public void changeStatus(ClassificationStatus status) {

    }

    @Override
    public void newNetworkLoaded() {

    }

    @Override
    public void trainingProgressUpdate(double trainingError, double validationError, int epoch) {

    }

}
