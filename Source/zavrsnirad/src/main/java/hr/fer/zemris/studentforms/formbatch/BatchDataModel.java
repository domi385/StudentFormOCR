package hr.fer.zemris.studentforms.formbatch;

import hr.fer.zemris.form.FormNode;
import hr.fer.zemris.form.FormNodeUtility;
import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.MarkersUtility;
import hr.fer.zemris.image.ImageUtility;
import hr.fer.zemris.image.binarization.GlobalThresholdAlgorithm;
import hr.fer.zemris.image.binarization.IBinarizationAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.grayscale.AverageAlgorithm;
import hr.fer.zemris.image.grayscale.IGrayscaleAlgorithm;
import hr.fer.zemris.image.transformation.RotateImageCenterAlgorithm;
import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.NeuralNetworkUtility;
import hr.fer.zemris.neural.datastructures.TrainingRecordUtility;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Data model for batch form processing.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class BatchDataModel {

    /**
     * Loaded forms list.
     */
    private List<FormNode> forms;
    /**
     * Form template parameters.
     */
    private FormTemplateParameters ftp;

    /**
     * Batch result directory.
     */
    private File resultDir;
    /**
     * Neural netowrk for digit classification.
     */
    private INeuralNetwork network;

    /**
     * Algorithm used for image grayscale.
     */
    private IGrayscaleAlgorithm grayAlgorithm = new AverageAlgorithm();

    /**
     * Algorithm used for image binarization.
     */
    private IBinarizationAlgorithm binaryAlgorithm = new GlobalThresholdAlgorithm(250);

    /**
     * Method sets neural network for digit classification.
     *
     * @param network
     *            neural network
     */
    public void setNeuralNetwork(INeuralNetwork network) {
        this.network = network;
    }

    /**
     * Method sets form template.
     *
     * @param ftp
     *            form template parameters
     */
    public void setFormTemplate(FormTemplateParameters ftp) {
        this.ftp = ftp;
    }

    /**
     * Method sets form files to batch model.
     *
     * @param inputForms
     *            list of input form files
     */
    public void setForms(List<File> inputForms) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    List<FormNode> formsList = new ArrayList<FormNode>();

                    for (File form : inputForms) {
                        FormNode formNode = new FormNode();
                        formNode.setFormTemplateParameters(ftp);
                        BufferedImage formImage;

                        formImage = ImageIO.read(form);
                        IBinaryImage binImg = ImageUtility.toBinary(formImage, grayAlgorithm, binaryAlgorithm);

                        CoordinateSystem2D coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility
                                .findMarkersByPositions(binImg, ftp.getMarkerPositions(), ftp.getExpMarkerSize() * 2));

                        double angle = GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(),
                                coordinateSystem.getY());
                        RotateImageCenterAlgorithm rica = new RotateImageCenterAlgorithm(angle);
                        BufferedImage rotatedImage = rica.transform(binImg.toImage());
                        binImg = ImageUtility.toBinary(rotatedImage, grayAlgorithm, binaryAlgorithm);
                        formNode.setBinarizedForm(binImg);
                        formNode.defineCoordinateSystem();
                        String name = form.getName();
                        if (name.contains(".")) {
                            name = name.substring(0, name.lastIndexOf('.'));
                        }
                        formNode.setFormName(name);
                        formsList.add(formNode);
                    }
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Loaded " + formsList.size()
                            + " forms", "Forms loaded", JOptionPane.INFORMATION_MESSAGE));
                    SwingUtilities.invokeLater(() -> (setFormsList(formsList)));
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                            "Error while readining scanned forms", "Error", JOptionPane.ERROR_MESSAGE));
                }

            }

        };
        t.start();
    }

    /**
     * Method sets list of form nodes.
     *
     * @param formsList
     *            list of nodes that represent forms
     */
    private void setFormsList(List<FormNode> formsList) {
        forms = formsList;
        System.out.println(forms);
    }

    /**
     * Field top offset.
     */
    private static final int UP_OFFSET = 8;
    /**
     * Field bottom offset.
     */
    private static final int LOW_OFFSET = 2;
    /**
     * Field left offset.
     */
    private static final int LEFT_OFFSET = 5;
    /**
     * Field right offset.
     */
    private static final int RIGHT_OFFSET = 5;

    /**
     * Method process all forms and saves result to resulting directory.
     */
    public void processForms() {
        List<List<String>> formFields = new ArrayList<>();
        for (FormNode formNode : forms) {
            List<String> fields = processForm(formNode);
            try (BufferedWriter bw = Files.newBufferedWriter(resultDir.toPath()
                    .resolve(formNode.getFormName() + ".txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                bw.write(String.format("%-12s\t%-20s\t%-100s\n", "Field number", "Number of points", "Error message"));
                for (int i = 0, end = fields.size(); i < end; i++) {
                    bw.write(String.format("%12d\t%-20s", i, fields.get(i).replace("..", ".")));

                    String error = checkField(fields.get(i));
                    if (error != null) {
                        bw.write(String.format("\t%-100s", error));
                    }
                    bw.write("\n");
                }
                formFields.add(fields);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while processing form " + formNode.getFormName(),
                        "Processing form error", JOptionPane.ERROR_MESSAGE);
            }

        }
        for (int i = 0, end = formFields.get(0).size(); i < end; i++) {
            for (int j = 0, endj = formFields.size(); j < endj; j++) {
                System.out.print(formFields.get(j).get(i) + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Method creates error message for table field if some error has been detected.
     *
     * @param string
     *            field string
     * @return error or warning message if there is something wrong with the given field, empty string otherwise
     */
    private String checkField(String string) {
        char[] characters = string.toCharArray();
        int dotNum = 0;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == '.') {
                dotNum++;
            }
        }
        if (dotNum > 1) {
            return "ERROR: Multiple decimal separators";
        }
        if (characters.length > 2 && characters[0] == '0' && characters[1] != '.') {
            return "WARNING: Possible missclassification leading zero detected";
        }
        return null;
    }

    /**
     * Method process form given form.
     *
     * @param formNode
     *            form to process.
     * @return string representation of processed form
     */
    private List<String> processForm(FormNode formNode) {
        Predicate<Region> segmentsFilter = i -> {
            return i.getBoundingRectangle().width > 2 && i.getBoundingRectangle().height > 2
                    && i.getPoints().size() > 10
                    && i.getBoundingRectangle().width * i.getBoundingRectangle().height > 6;
        };
        List<String> fieldString = new ArrayList<String>();
        for (int i = 0; i < formNode.getFormTemplate().getPointsNumber(); i++) {
            IBinaryImage fieldImage = FormNodeUtility.extractField(formNode, i, UP_OFFSET, LOW_OFFSET, LEFT_OFFSET,
                    RIGHT_OFFSET);
            List<IBinaryImage> segments = FormNodeUtility.segmentField(fieldImage, segmentsFilter);
            List<String> processedSegments = new ArrayList<String>();
            for (IBinaryImage segment : segments) {
                double[] input = TrainingRecordUtility.convertImageData(segment.toImage(),
                        (int) Math.sqrt(network.getLayerNeruonsNumber(0)));
                network.calcOutput(input);
                processedSegments.add(NeuralNetworkUtility.encodeOutput(network.getNetworkOutput()));
            }
            fieldString.add(String.join("", processedSegments));
        }
        return fieldString;
    }

    /**
     * Method sets output directory for batch process.
     *
     * @param directory
     *            output directory
     */
    public void setOutputDirectory(File directory) {
        resultDir = directory;
    }

}
