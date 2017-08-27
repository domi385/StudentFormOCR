package hr.fer.zemris.neural.datastructures;

import java.nio.file.Path;

/**
 * Training record containing network input and expected output for concrete training example with path specified.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public class TrainingRecord {

    /**
     * Path to training example.
     */
    private Path trainingFilePath;

    /**
     * Input array.
     */
    private double[] input;

    /**
     * Expected output.
     */
    private String expectedOutput;

    public TrainingRecord(Path trainingFilePath, double[] input, String expectedOutput) {
        super();
        this.trainingFilePath = trainingFilePath;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public double[] getInput() {
        return input;
    }

    public Path getTrainingFilePath() {
        return trainingFilePath;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public void setTrainingFilePath(Path trainingFilePath) {
        this.trainingFilePath = trainingFilePath;
    }
}
