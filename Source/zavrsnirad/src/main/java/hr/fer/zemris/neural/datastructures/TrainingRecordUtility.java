package hr.fer.zemris.neural.datastructures;

import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.transformation.I2DTransformationAlgorithm;
import hr.fer.zemris.image.transformation.ScaleInSquareAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TrainingRecordUtility {

    /**
     * Utility class private constructor.
     */
    private TrainingRecordUtility() {
    }

    public static List<TrainingRecord> fromFile(File trainingSetFile, Integer squareDimension) throws IOException {
        String[] lines = new String(Files.readAllBytes(trainingSetFile.toPath())).replace("\r", "")
                .replaceAll("\n{2,}", "\n").split("\n");
        List<TrainingInput> trainingInputs = new ArrayList<TrainingInput>();
        TrainingInput current = null;
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            if (trimmedLine.startsWith("#")) {
                continue;
            }
            if (trimmedLine.toLowerCase().equals("<training_input>")) {
                current = new TrainingInput();
            }
            if (trimmedLine.toLowerCase().equals("</training_input>")) {
                trainingInputs.add(current);
                current = null;
            }
            if (trimmedLine.toLowerCase().startsWith("<dir>")) {
                if (current == null) {
                    throw new IllegalArgumentException("File in wrong format");
                }
                current.setTrainingInputDirectory(Paths.get(trimmedLine.replaceAll("<[^<>]*>", "").trim()));
            }
            if (trimmedLine.toLowerCase().startsWith("<expected_output>")) {
                if (current == null) {
                    throw new IllegalArgumentException("File in wrong format");
                }
                current.setExpectedOutput(trimmedLine.replaceAll("<[^<>]*>", "").trim());
            }

        }
        List<TrainingRecord> trainingRecords = new ArrayList<TrainingRecord>();
        for (TrainingInput trainingInput : trainingInputs) {
            trainingRecords.addAll(trainingInput.toTrainingRecords(squareDimension));
        }
        return trainingRecords;

    }

    private static BufferedImage loadImage(File file) throws IOException {
        return ImageIO.read(file);

    }

    public static double[] loadImageData(File file, Integer dimension) throws IOException {
        BufferedImage image = loadImage(file);
        I2DTransformationAlgorithm transform = new ScaleInSquareAlgorithm(dimension);
        image = transform.transform(image);
        IBinaryImage binImage = ImageUtility.defalutToBinary(image);
        image.flush();
        return binImage.getPixels();
    }

    public static double[] convertImageData(BufferedImage image, Integer dimension) {
        I2DTransformationAlgorithm transform = new ScaleInSquareAlgorithm(dimension);
        image = transform.transform(image);
        IBinaryImage binImage = ImageUtility.defalutToBinary(image);
        image.flush();
        return binImage.getPixels();
    }

}
