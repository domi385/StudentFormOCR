package hr.fer.zemris.studentforms.classification.data;

import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.NeuralNetworkUtility;
import hr.fer.zemris.neural.datastructures.TrainingRecord;
import hr.fer.zemris.neural.datastructures.TrainingRecordUtility;
import hr.fer.zemris.neural.trainers.ClassicBackPropagationTrainer;
import hr.fer.zemris.neural.trainers.INeuralNetworkTrainer;
import hr.fer.zemris.neural.trainers.INeuralTrainerListener;
import hr.fer.zemris.studentforms.classification.ClassificationStatus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Data model for classification GUI panel.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class ClassificationDataModel {

    /**
     * Swing worker that trains neural network.
     *
     * @author Domagoj Pluscec
     * @version v1.0, 25.5.2017.
     */
    private class TrainingWorker extends SwingWorker<Void, Void> {
        /**
         * Current neural network trainer.
         */
        private INeuralNetworkTrainer trainer;
        /**
         * Number of epochs for training.
         */
        private Integer epochsNumber;
        /**
         * Records used for training.
         */
        private List<TrainingRecord> trainingRecords;
        /**
         * Records used for evaluation.
         */
        private List<TrainingRecord> evaluationRecords;

        private int refreshInterval;

        /**
         * Constructor that initializes training worker with training parameters.
         *
         * @param trainer
         *            neural network trainer
         * @param epochsNumber
         *            number of epochs for training
         * @param trainingRecords
         *            training set
         * @param evaluationRecords
         *            evaluation set
         */
        TrainingWorker(INeuralNetworkTrainer trainer, Integer epochsNumber, List<TrainingRecord> trainingRecords,
                List<TrainingRecord> evaluationRecords, int refreshInterval) {
            super();
            this.trainer = trainer;
            this.epochsNumber = epochsNumber;
            this.trainingRecords = trainingRecords;
            this.evaluationRecords = evaluationRecords;
            this.refreshInterval = refreshInterval;

        }

        @Override
        protected Void doInBackground() throws Exception {
            INeuralTrainerListener trainerListener = new INeuralTrainerListener() {

                private int epochs = epochsNumber;

                @Override
                public void stateChanged(double trainingSetError, double validationSetError, int epoch) {
                    TrainingWorker.this.setProgress(epoch / epochs);

                }
            };

            trainer.addTrainerListener(trainerListener);
            NeuralNetworkUtility.train(trainer, trainingRecords, evaluationRecords, epochsNumber, refreshInterval);

            return null;
        }

        @Override
        protected void done() {
            currentStatus = ClassificationStatus.LOADED_EXAMPLES;
            fireChangeStatus();
            processTestSet();
        }
    }

    /**
     * Neural network used for training.
     */
    private INeuralNetwork network;

    /**
     * Data model listeners.
     */
    private List<ClassificationDataModelListener> listeners;

    /**
     * Training set used for training neural network.
     */
    private List<TrainingRecord> trainingSet;

    /**
     * Evaluation set used for training neural network.
     */
    private List<TrainingRecord> evaluationSet;

    /**
     * Records used for testing.
     */
    private List<TrainingRecord> testSet;

    /**
     * Current status of classification setup.
     */
    private ClassificationStatus currentStatus;

    /**
     * Dimension of image side. It determines number of input neurons.
     */
    private int imageSquareDimension;

    /**
     * Data model constructor that initializes data model.
     */
    public ClassificationDataModel() {
        listeners = new ArrayList<ClassificationDataModelListener>();
    }

    /**
     * Method adds listener to the model.
     *
     * @param listener
     *            data model listener
     */
    public void addListener(ClassificationDataModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Method notifies listeners for a classification setup status change.
     */
    private void fireChangeStatus() {
        listeners.forEach(i -> i.changeStatus(currentStatus));
    }

    /**
     * Method notifies listeners that a new neural network has been set to the data model.
     */
    private void fireNewNetwork() {
        listeners.forEach(i -> i.newNetworkLoaded());
    }

    /**
     * Method notifies listeners for training progress update.
     *
     * @param trainingError
     *            training error
     * @param validationError
     *            validation error
     */
    private void firetrainingProgressUpdate(double trainingError, double validationError, int epoch) {
        listeners.forEach(i -> i.trainingProgressUpdate(trainingError, validationError, epoch));
    }

    /**
     * Method obtains data model neural network.
     *
     * @return current neural network
     */
    public INeuralNetwork getNetwork() {
        return network;
    }

    /**
     * Method loads training file. File loading is done in separate thread.
     *
     * @param trainingFile
     *            file containing evaluation training records details
     */
    public synchronized void loadExamplesFile(File examplesFile) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    List<TrainingRecord> records = TrainingRecordUtility.fromFile(examplesFile, imageSquareDimension);

                    SwingUtilities.invokeLater(() -> setExamples(records));
                } catch (IOException | NullPointerException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Couldn't load file",
                            "Load training set error\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE));
                }

            }

        };
        t.start();

    }

    private synchronized void setExamples(List<TrainingRecord> records) {
        Map<String, List<TrainingRecord>> inputSets = new HashMap<>();

        for (TrainingRecord record : records) {
            List<TrainingRecord> currentList = inputSets.getOrDefault(record.getExpectedOutput(),
                    new ArrayList<TrainingRecord>());
            currentList.add(record);
            inputSets.put(record.getExpectedOutput(), currentList);
        }

        // Divide records

        trainingSet = new ArrayList<TrainingRecord>();
        evaluationSet = new ArrayList<TrainingRecord>();
        testSet = new ArrayList<TrainingRecord>();

        for (String inputSetsKey : inputSets.keySet()) {
            List<TrainingRecord> currentList = inputSets.get(inputSetsKey);
            Collections.shuffle(currentList);

            int size = currentList.size();
            int trainingMax = (int) Math.round(size * 0.6);
            int evaluationMax = (int) (trainingMax + Math.round(size * 0.2));

            for (int i = 0; i < trainingMax && i < size; i++) {
                trainingSet.add(currentList.get(i));
            }
            for (int i = trainingMax; i < evaluationMax && i < size; i++) {
                evaluationSet.add(currentList.get(i));
            }
            for (int i = evaluationMax; i < size; i++) {
                testSet.add(currentList.get(i));
            }
        }

        currentStatus = ClassificationStatus.LOADED_EXAMPLES;
        fireChangeStatus();

    }

    private double testSetError;
    private Map<String, Map<String, Integer>> classificationMap;

    private int missclassifiedExampels = 0;

    public void processTestSet() {
        testSetError = 0;
        missclassifiedExampels = 0;
        classificationMap = new HashMap<String, Map<String, Integer>>();
        for (TrainingRecord record : testSet) {
            String output = record.getExpectedOutput();

            double[] input = record.getInput();
            double[] expectedOutput = NeuralNetworkUtility.decodeOutput(output);
            network.calcOutput(input);
            double[] networkOutput = network.getNetworkOutput();

            double totalError = 0;
            for (int i = 0; i < networkOutput.length; i++) {
                totalError += Math.pow(expectedOutput[i] - networkOutput[i], 2);
            }
            testSetError += totalError / 2;

            String encodedOutput = NeuralNetworkUtility.encodeOutput(networkOutput);

            Map<String, Integer> currMap = classificationMap.getOrDefault(output, new HashMap<String, Integer>());
            currMap.put(encodedOutput, currMap.getOrDefault(encodedOutput, 0) + 1);
            classificationMap.put(output, currMap);
            if (!output.trim().equalsIgnoreCase(encodedOutput.trim())) {
                missclassifiedExampels++;
            }

        }
        testSetError = testSetError / testSet.size();
        System.out.println(testSetError);
        System.out.println(missclassifiedExampels + "/" + testSet.size());
        System.out.println(classificationMap);
        processDataset();
    }

    public void processDataset() {
        double datasetError = 0;
        int missclassifiedExampels = 0;
        Map<String, Map<String, Integer>> classificationMap = new HashMap<String, Map<String, Integer>>();
        List<TrainingRecord> dataset = new ArrayList<TrainingRecord>();
        dataset.addAll(trainingSet);
        dataset.addAll(evaluationSet);
        dataset.addAll(testSet);
        for (TrainingRecord record : dataset) {
            String output = record.getExpectedOutput();

            double[] input = record.getInput();
            double[] expectedOutput = NeuralNetworkUtility.decodeOutput(output);
            network.calcOutput(input);
            double[] networkOutput = network.getNetworkOutput();

            double totalError = 0;
            for (int i = 0; i < networkOutput.length; i++) {
                totalError += Math.pow(expectedOutput[i] - networkOutput[i], 2);
            }
            datasetError += totalError / 2;

            String encodedOutput = NeuralNetworkUtility.encodeOutput(networkOutput);

            Map<String, Integer> currMap = classificationMap.getOrDefault(output, new HashMap<String, Integer>());
            currMap.put(encodedOutput, currMap.getOrDefault(encodedOutput, 0) + 1);
            classificationMap.put(output, currMap);
            if (!output.trim().equalsIgnoreCase(encodedOutput.trim())) {
                missclassifiedExampels++;
            }

        }

        testSetError = testSetError / dataset.size();

        System.out.println(datasetError);
        System.out.println(missclassifiedExampels + "/" + dataset.size());
        System.out.println(classificationMap);

    }

    /**
     * Method removes data model listener.
     *
     * @param listener
     *            data model to be removed
     */
    public void removeListener(ClassificationDataModel listener) {
        listeners.remove(listener);
    }

    /**
     * Method sets current neural network.
     *
     * @param network
     *            neural network.
     */
    public synchronized void setNetwork(INeuralNetwork network) {
        if (network == null) {
            throw new IllegalArgumentException("Neural network cannot be set to null");
        }
        this.network = network;
        currentStatus = ClassificationStatus.LOADED_NETWORK;
        evaluationSet = null;
        trainingSet = null;
        imageSquareDimension = (int) Math.sqrt(network.getLayerNeruonsNumber(0));
        fireNewNetwork();
        fireChangeStatus();
    }

    /**
     * Method trains neural network with given parameters.
     *
     * @param learningRate
     *            training learning rate
     * @param epochsNumber
     *            number of epochs
     * @throws IllegalStateException
     *             if neural netowrk or examples are not defined.
     */
    public void train(Double learningRate, Integer epochsNumber, Integer refreshInterval) throws IllegalStateException {

        if (network == null || trainingSet == null || evaluationSet == null) {
            throw new IllegalStateException(
                    "Current state doesn't have defined all parameters (neural network or training, evaluation sets");
        }

        currentStatus = ClassificationStatus.TRAINING;
        fireChangeStatus();

        INeuralNetworkTrainer trainer = new ClassicBackPropagationTrainer(network, learningRate);
        INeuralTrainerListener trainerListener = new INeuralTrainerListener() {
            @Override
            public void stateChanged(double trainingSetError, double validationSetError, int epoch) {
                firetrainingProgressUpdate(trainingSetError, validationSetError, epoch);
            }
        };

        trainer.addTrainerListener(trainerListener);

        currentStatus = ClassificationStatus.LOADED_EXAMPLES;
        fireChangeStatus();
        TrainingWorker trainingWorker = new TrainingWorker(trainer, epochsNumber, trainingSet, evaluationSet,
                refreshInterval);
        trainingWorker.execute();

    }

    /**
     * Method obtains error on test set.
     *
     * @return test set error
     */
    public double getTestSetError() {
        return testSetError;
    }

    /**
     * Method obtains rate of missclassificated examples on a test set.
     *
     * @return missclassification rate on test set
     */
    public double getTestSetMisclassificationRate() {
        return missclassifiedExampels / testSet.size();
    }

    /**
     * Method obtains test set confusion map.
     *
     * @return test set confusion map
     */
    public Map<String, Map<String, Integer>> getTestSetConfussionMap() {
        return classificationMap;
    }

    /**
     * Method classifies image examples.
     *
     * @param img
     *            image example
     * @return classification string
     */
    public String classify(BufferedImage img) {
        double[] input = TrainingRecordUtility.convertImageData(img, (int) Math.sqrt(network.getLayerNeruonsNumber(0)));
        network.calcOutput(input);
        return NeuralNetworkUtility.encodeOutput(network.getNetworkOutput());
    }

}
