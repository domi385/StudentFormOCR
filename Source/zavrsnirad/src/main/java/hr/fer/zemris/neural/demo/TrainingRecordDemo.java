package hr.fer.zemris.neural.demo;

import hr.fer.zemris.neural.datastructures.TrainingRecord;
import hr.fer.zemris.neural.datastructures.TrainingRecordUtility;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class shows demo program for loading training set.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class TrainingRecordDemo {

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     * @throws IOException
     *             if there was a problem while reading training set
     */
    public static void main(String[] args) throws IOException {
        File trainingFile = new File("trainingset.xml");
        final int dimension = 50;
        List<TrainingRecord> records = TrainingRecordUtility.fromFile(trainingFile, dimension);
        System.out.println(records.size());
    }

    /**
     * Private utility class constructor.
     */
    private TrainingRecordDemo() {
    }
}
