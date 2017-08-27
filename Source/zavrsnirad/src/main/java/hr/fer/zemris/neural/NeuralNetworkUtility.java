package hr.fer.zemris.neural;

import hr.fer.zemris.neural.datastructures.TrainingRecord;
import hr.fer.zemris.neural.trainers.INeuralNetworkTrainer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class NeuralNetworkUtility {

    private static final int DEFAULT_OUTPUT_SIZE = 11;

    public static double[] decodeOutput(String expectedOutput) {
        double[] output = new double[DEFAULT_OUTPUT_SIZE];
        if (expectedOutput.equals(".") || expectedOutput.equals(",")) {
            output[10] = 1;
            return output;
        }
        output[Integer.parseInt(expectedOutput)] = 1;
        return output;
    }

    public static double[][] extractExpectedOutputFromRecords(List<TrainingRecord> records) {
        double[][] outputs = new double[records.size()][DEFAULT_OUTPUT_SIZE];
        for (int i = 0, end = records.size(); i < end; i++) {
            outputs[i] = decodeOutput(records.get(i).getExpectedOutput());
        }
        return outputs;
    }

    public static double[][] extractInputFromRecords(List<TrainingRecord> trainingRecords) {

        double[][] inputs = new double[trainingRecords.size()][trainingRecords.get(0).getInput().length];
        for (int i = 0, end = trainingRecords.size(); i < end; i++) {
            inputs[i] = trainingRecords.get(i).getInput();
        }
        return inputs;
    }

    public static INeuralNetwork initializeNeuralNetwork(List<Integer> neuronsNumber) {
        FeedForwardNetwork network = new FeedForwardNetwork();
        for (Integer layerNeurons : neuronsNumber) {
            network.addLayer(layerNeurons);
        }
        network.setRandomWeights();
        return network;
    }

    public static INeuralNetwork readNeuralNetworkFromFile(File networkFile) {
        try (ObjectInputStream ons = new ObjectInputStream(Files.newInputStream(networkFile.toPath(),
                StandardOpenOption.READ))) {
            INeuralNetwork ftp = (INeuralNetwork) ons.readObject();
            return ftp;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void train(INeuralNetworkTrainer trainer, List<TrainingRecord> trainingRecords,
            List<TrainingRecord> evaluationRecords, Integer epochsNumber, int refreshInterval) {

        trainer.train(extractInputFromRecords(trainingRecords), extractExpectedOutputFromRecords(trainingRecords),
                extractInputFromRecords(evaluationRecords), extractExpectedOutputFromRecords(evaluationRecords),
                epochsNumber, refreshInterval);
    }

    public static void writeNeuralNetworkToFile(File outputFile, INeuralNetwork network) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(outputFile.toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE))) {
            oos.writeObject(network);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Private utility class constructor.
     */
    private NeuralNetworkUtility() {
    }

    public static String encodeOutput(double[] networkOutput) {
        double maxVal = -1;
        int maxInd = -1;
        for (int i = 0; i < networkOutput.length; i++) {
            if (networkOutput[i] > maxVal) {
                maxVal = networkOutput[i];
                maxInd = i;
            }
        }

        if (maxInd < 0) {
            return "-1";
        }
        if (maxInd < 10) {
            return String.valueOf(maxInd);
        }
        return ".";
    }

}
