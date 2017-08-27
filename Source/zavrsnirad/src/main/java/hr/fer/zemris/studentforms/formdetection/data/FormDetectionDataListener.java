package hr.fer.zemris.studentforms.formdetection.data;

import hr.fer.zemris.studentforms.formdetection.FormDetectionStatus;

/**
 * Form detection data listener that listens for form detection process change.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public interface FormDetectionDataListener {

    /**
     * Regions extraction update.
     *
     * @param progress
     *            regions exctraction progress
     */
    void extractionUpdate(int progress);

    /**
     * Original form has been changed.
     *
     * @param dataModel
     *            form detection data model
     */
    void originalChanged(FormDetectionDataModel dataModel);

    /**
     * Form detection state changed. Something in detection has changed. Transformed form has been changed.
     *
     * @param dataModel
     *            form detection data model
     */
    void stateChanged(FormDetectionDataModel dataModel);

    /**
     * Form detection status changed.
     *
     * @param status
     *            current detection status changed
     */
    void statusChanged(FormDetectionStatus status);
}
