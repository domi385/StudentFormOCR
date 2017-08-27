package hr.fer.zemris.studentforms.formdetection.data;

import hr.fer.zemris.studentforms.formdetection.FormDetectionStatus;

/**
 * Form detection listener that offers empty implementations.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.5.2017.
 */
public class FormDetectionDataAdapter implements FormDetectionDataListener {

    @Override
    public void extractionUpdate(int progress) {

    }

    @Override
    public void originalChanged(FormDetectionDataModel dataModel) {

    }

    @Override
    public void stateChanged(FormDetectionDataModel dataModel) {

    }

    @Override
    public void statusChanged(FormDetectionStatus status) {

    }

}
