package hr.fer.zemris.studentforms.classification.panels;

import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.NeuralNetworkUtility;
import hr.fer.zemris.studentforms.classification.ClassificationStatus;
import hr.fer.zemris.studentforms.classification.data.ClassificationDataModel;
import hr.fer.zemris.studentforms.classification.data.ClassificationDataModelAdapter;
import hr.fer.zemris.studentforms.classification.data.ClassificationDataModelListener;
import hr.fer.zemris.studentforms.classification.dialogs.CreateNeuralNetworkDialog;
import hr.fer.zemris.studentforms.classification.dialogs.TrainNetworkDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel contains buttons and other control items that control training and classification of a neural network.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 25.5.2017.
 */
public class ControlPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Classification panel data model.
     */
    private ClassificationDataModel dataModel;

    private JButton btnSelectTrainingFile;

    /**
     * Button starts network training.
     */
    private JButton btnTrain;
    /**
     * Button classifies example.
     */
    private JButton btnClassify;

    private JButton btnTestSet;

    /**
     * Save neural network button.
     */
    private JButton btnSave;
    /**
     * Load neural network button.
     */
    private JButton btnLoad;
    /**
     * Button for creating new neural network.
     */
    private JButton btnNew;
    /**
     * Neural network input set file filter.
     */
    private FileNameExtensionFilter setFileFilter = new FileNameExtensionFilter("Neural network set", "xml", "txt");

    private ClassificationDataModelListener btnNewDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {
            case TRAINING:
                btnNew.setEnabled(false);
                break;
            default:
                btnNew.setEnabled(true);
                break;
            }
        }
    };
    private ClassificationDataModelListener btnLoadDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {
            case TRAINING:
                btnLoad.setEnabled(false);
                break;
            default:
                btnLoad.setEnabled(true);
                break;
            }
        }
    };
    private ClassificationDataModelListener btnSaveDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {
            case TRAINING:
                btnSave.setEnabled(false);
                break;
            case NO_NETWORK:
                btnSave.setEnabled(false);
                break;
            default:
                btnSave.setEnabled(true);
                break;
            }
        }
    };
    private ClassificationDataModelListener btnSelectTrainingFileDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {
            case TRAINING:
                btnSelectTrainingFile.setEnabled(false);
                break;
            case NO_NETWORK:
                btnSelectTrainingFile.setEnabled(false);
                break;
            default:
                btnSelectTrainingFile.setEnabled(true);
                break;
            }
        }
    };

    private ClassificationDataModelListener btnTrainDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {
            case LOADED_EXAMPLES:
                btnTrain.setEnabled(true);
                break;
            default:
                btnTrain.setEnabled(false);
                break;
            }
        }
    };

    private ClassificationDataModelListener btnClassifyDataListener = new ClassificationDataModelAdapter() {
        @Override
        public void changeStatus(ClassificationStatus status) {
            switch (status) {

            case NO_NETWORK:
                btnClassify.setEnabled(false);
                break;
            case TRAINING:
                btnClassify.setEnabled(false);
                break;
            default:
                btnClassify.setEnabled(true);
                break;
            }
        }
    };

    public ControlPanel(ClassificationDataModel dataModel) {
        this.dataModel = dataModel;
        dataModel.addListener(btnNewDataListener);
        dataModel.addListener(btnLoadDataListener);
        dataModel.addListener(btnSaveDataListener);
        dataModel.addListener(btnSelectTrainingFileDataListener);
        dataModel.addListener(btnTrainDataListener);
        dataModel.addListener(btnClassifyDataListener);
        initGUI();
    }

    /**
     * Method changes data model neural network.
     *
     * @param network
     *            neural network object
     */
    private void changeNetwork(INeuralNetwork network) {
        dataModel.setNetwork(network);
    }

    /**
     * Method displays dialog for creating neural network.
     */
    private void createNeuralNetworkDialog() {
        CreateNeuralNetworkDialog dialog = new CreateNeuralNetworkDialog();
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!dialog.getReturnStatus()) {
                    return;
                }
                List<Integer> neuronsNumber = dialog.getNeuronsNumber();
                INeuralNetwork network = NeuralNetworkUtility.initializeNeuralNetwork(neuronsNumber);
                dataModel.setNetwork(network);
            }
        });
    }

    /**
     * Method initializes panels GUI.
     */
    private void initGUI() {
        btnSelectTrainingFile = new JButton("Select examples file");
        btnSelectTrainingFile.addActionListener(e -> selectExamplesFile());
        btnSelectTrainingFile.setEnabled(false);
        btnNew = new JButton("New network");
        btnNew.addActionListener(e -> createNeuralNetworkDialog());
        btnLoad = new JButton("Load network");
        btnLoad.addActionListener(e -> loadNeuralNetworkDialog());
        btnSave = new JButton("Save network");
        btnSave.addActionListener(e -> saveNeuralNetworkDialog());
        btnSave.setEnabled(false);
        btnTrain = new JButton("Train network");
        btnTrain.addActionListener(e -> trainDialog());
        btnTrain.setEnabled(false);
        btnClassify = new JButton("Classify example");
        btnClassify.addActionListener(e -> classifyDialog());
        // TODO classify
        btnTestSet = new JButton("Run test set validation");
        btnTestSet.addActionListener(e -> runTestSet());
        add(btnNew);
        add(btnLoad);

        add(btnSelectTrainingFile);

        add(btnSave);
        add(btnTrain);
        add(btnClassify);
        add(btnTestSet);

    }

    private void runTestSet() {
        dataModel.processTestSet();
        dataModel.getTestSetError();
        dataModel.getTestSetMisclassificationRate();
        dataModel.getTestSetConfussionMap();
    }

    private void classifyDialog() {
        File exampleFile = loadFileDialog(new FileNameExtensionFilter("Image", "png", "jpg"));
        if (exampleFile == null) {
            return;
        }
        String classified;
        try {
            classified = dataModel.classify(ImageIO.read(exampleFile));
            JOptionPane.showMessageDialog(getParent(), "Loaded example has been classified to " + classified,
                    "Classification result", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while reading given example file", "Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Method displays dialog for choosing file with given filename filter.
     *
     * @param fileNameFilter
     *            file name filter
     * @return selected file, or null if file hasn't been selected
     */
    private File loadFileDialog(FileNameExtensionFilter fileNameFilter) {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setFileFilter(fileNameFilter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        File selectedFile = chooser.getSelectedFile();
        return selectedFile;
    }

    /**
     * Method displays dialog for selecting a neural network.
     */
    private void loadNeuralNetworkDialog() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Neural network", "ann");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        Thread t = new Thread() {
            private final File file = selectedFile;
            private INeuralNetwork network;

            @Override
            public void run() {
                network = NeuralNetworkUtility.readNeuralNetworkFromFile(file);
                SwingUtilities.invokeLater(() -> changeNetwork(network));

            }

        };
        t.start();
    }

    /**
     * Method displays dialog for saving neural network.
     */
    private void saveNeuralNetworkDialog() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Neural network", "ann");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        Thread t = new Thread() {
            private final File file = selectedFile;
            private final INeuralNetwork network = dataModel.getNetwork();

            @Override
            public void run() {
                NeuralNetworkUtility.writeNeuralNetworkToFile(selectedFile, network);

            }

        };
        t.start();
    }

    /**
     * Method displays dialog for selecting examples set.
     */
    private void selectExamplesFile() {
        File trainingFile = loadFileDialog(setFileFilter);
        if (trainingFile == null) {
            return;
        }
        dataModel.loadExamplesFile(trainingFile);
    }

    /**
     * Method displays dialog for training neural network.
     */
    private void trainDialog() {
        TrainNetworkDialog dialog = new TrainNetworkDialog();
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!dialog.getReturnStatus()) {
                    return;
                }
                dataModel.train(dialog.getLearningRate(), dialog.getEpochsNumber(), dialog.getRefreshInterval());
            }
        });
    }
}
