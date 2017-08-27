package hr.fer.zemris.studentforms.classification;

/**
 * Classification panel status.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public enum ClassificationStatus {
    /**
     * There is no neural network loaded.
     */
    NO_NETWORK,
    /**
     * Neural network has been loaded.
     */
    LOADED_NETWORK,
    /**
     * Training examples have been loaded.
     */
    LOADED_EXAMPLES,
    /**
     * Neural network is in training process.
     */
    TRAINING;
}
