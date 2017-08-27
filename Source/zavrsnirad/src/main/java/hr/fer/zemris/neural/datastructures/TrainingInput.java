package hr.fer.zemris.neural.datastructures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Training input for neural network containing multiple training records.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class TrainingInput {

    /**
     * Path to training input examples directory.
     */
    private Path trainingInputDirectory;

    /**
     * Training input examples expected output.
     */
    private String expectedOutput;

    /**
     * Method obtains training input expected output.
     *
     * @return expected output
     */
    public String getExpectedOutput() {
        return expectedOutput;
    }

    /**
     * Method obtains path to training input examples directory.
     *
     * @return path to training input examples directory.
     */
    public Path getTrainingInputDirectory() {
        return trainingInputDirectory;
    }

    /**
     * Method sets training input expected output.
     *
     * @param expectedOutput
     *            expected ouput of records contained in current training input
     */
    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    /**
     * Method sets path to training input examples directory.
     *
     * @param trainingInputDirectory
     *            path to training input examples directory
     */
    public void setTrainingInputDirectory(Path trainingInputDirectory) {
        this.trainingInputDirectory = trainingInputDirectory;
    }

    /**
     * Method transforms training input to list of training records.
     *
     * @param dimension
     *            dimension of training record image size
     * @return list of training records
     * @throws IOException
     *             if there was a problem while reading training example
     */
    public List<TrainingRecord> toTrainingRecords(Integer dimension) throws IOException {
        List<TrainingRecord> records = new ArrayList<TrainingRecord>();

        File[] files = getTrainingInputDirectory().toFile().listFiles();
        for (int i = 0; i < files.length; i++) {
            TrainingRecord record = new TrainingRecord(files[i].toPath(), TrainingRecordUtility.loadImageData(files[i],
                    dimension), getExpectedOutput());
            records.add(record);
        }
        return records;
    }

}
