package hr.fer.zemris.studentforms.formbatch;

import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.FormTemplateUtility;
import hr.fer.zemris.neural.INeuralNetwork;
import hr.fer.zemris.neural.NeuralNetworkUtility;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel used for batch analysis of multiple forms.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 3.6.2017.
 */
public class BatchPanel extends JPanel {
    /**
     * Panels data model.
     */
    private BatchDataModel dataModel;

    /**
     * Constructor that initializes panel model and gui.
     *
     */
    public BatchPanel() {
        dataModel = new BatchDataModel();
        initGUI();
    }

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Method initializes panels GUI.
     */
    private void initGUI() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        add(centerPanel);
        centerPanel.setAlignmentY(CENTER_ALIGNMENT);

        JButton btnLoadTemplate = new JButton("Load form template");
        btnLoadTemplate.setAlignmentX(CENTER_ALIGNMENT);
        btnLoadTemplate.addActionListener(l -> loadFormTemplate());
        centerPanel.add(btnLoadTemplate);
        centerPanel.add(Box.createVerticalStrut(20));

        JButton btnLoadNetwork = new JButton("Load neural network classifier");
        btnLoadNetwork.setAlignmentX(CENTER_ALIGNMENT);
        btnLoadNetwork.addActionListener(l -> loadNeuralNetworkDialog());
        centerPanel.add(btnLoadNetwork);
        centerPanel.add(Box.createVerticalStrut(20));

        JButton btnSelectFormDirectory = new JButton("Select scanned forms directory");
        btnSelectFormDirectory.setAlignmentX(CENTER_ALIGNMENT);
        btnSelectFormDirectory.addActionListener(l -> selectFormsDirecory());
        centerPanel.add(btnSelectFormDirectory);
        centerPanel.add(Box.createVerticalStrut(20));

        JButton btnSelectOutputDirectory = new JButton("Select results directory");
        btnSelectOutputDirectory.addActionListener(l -> selectOutputDirectory());
        btnSelectOutputDirectory.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(btnSelectOutputDirectory);
        centerPanel.add(Box.createVerticalStrut(20));

        JButton btnProcessForms = new JButton("Process forms");
        btnProcessForms.addActionListener(l -> dataModel.processForms());
        btnProcessForms.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(btnProcessForms);

    }

    /**
     * Method asks user to select result output directory.
     */
    private void selectOutputDirectory() {
        File directory = selectDirectory();
        if (directory == null) {
            return;
        }
        dataModel.setOutputDirectory(directory);
    }

    /**
     * Method asks user to select input directory with forms.
     */
    private void selectFormsDirecory() {
        File directory = selectDirectory();
        if (directory == null) {
            return;
        }
        File[] files = directory.listFiles();
        List<File> formFiles = new ArrayList<>();
        for (File file : files) {
            if (file.canRead()) {
                formFiles.add(file);
            }
        }
        dataModel.setForms(formFiles);
        JOptionPane.showMessageDialog(this, "Detected " + formFiles.size() + " files.", "Forms extraction",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method selects directory file.
     *
     * @return directory file or null if it wasn't successfully selected
     */
    private File selectDirectory() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setDialogTitle("Select directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }

    /**
     * Method asks user to enter form template.
     */
    private void loadFormTemplate() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Templates", "ftp");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File templateFile = chooser.getSelectedFile();
        Thread t = new Thread() {
            private FormTemplateParameters params;

            @Override
            public void run() {

                try {
                    params = FormTemplateUtility.readTemplateFromFile(templateFile);
                    SwingUtilities.invokeLater(() -> dataModel.setFormTemplate(params));
                } catch (ClassNotFoundException e) {
                    SwingUtilities.invokeLater(() -> invalidFileFormatMsg());
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> unsuccesfullIOOperationMsg());
                }

            }

        };
        t.start();
    }

    /**
     * Method displays dialog for unsuccessful IO operation.
     */
    private void unsuccesfullIOOperationMsg() {
        JOptionPane.showMessageDialog(this,
                "There was an error while reading/writing to a file because of file system error.", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method displays dialog for invalid file format.
     */
    private void invalidFileFormatMsg() {
        JOptionPane.showMessageDialog(this, "File that you tried to load had invalid format", "Invalid file format",
                JOptionPane.ERROR_MESSAGE);
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
        if (!selectedFile.canRead()) {
            JOptionPane.showMessageDialog(this, "Selected file cannot be read.", "File read error", ERROR);
        }
        Thread t = new Thread() {
            private final File file = selectedFile;
            private INeuralNetwork network;

            @Override
            public void run() {
                network = NeuralNetworkUtility.readNeuralNetworkFromFile(file);
                SwingUtilities.invokeLater(() -> (dataModel.setNeuralNetwork(network)));

            }

        };
        t.start();
    }

}
