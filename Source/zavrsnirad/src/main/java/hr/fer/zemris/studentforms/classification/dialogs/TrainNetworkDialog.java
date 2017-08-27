package hr.fer.zemris.studentforms.classification.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Dialog for initializing parameters for training a neural network.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 */
public class TrainNetworkDialog extends JDialog {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default number of training epochs.
     */
    private static final int DEFAULT_EPOCH_NUMBER = 1000;

    /**
     * Default learning rate.
     */
    private static final double DEFUALT_LEARNING_RATE = 0.1;

    private static final int DEFAULT_REFRESH_INTERVAL = 5;

    /**
     * Window listener that sets dialog status on closing event.
     */
    private WindowListener windowClosingListener = new WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            status = false;
            dispose();
        };
    };

    /**
     * Dialog status. If true training parameters have been set up, false otherwise.
     */
    private boolean status;

    /**
     * Panel with training options.
     */
    private JPanel optionsPanel;
    /**
     * Button for defining parameters.
     */
    private JButton btnOk;
    /**
     * Number of epochs.
     */
    private Integer epochsNumber;
    private Integer refreshInterval;

    /**
     * Learning rate value.
     */
    private Double learningRate;

    /**
     * Document listener that listens to fields change and validates dialog.
     */
    private DocumentListener fieldsListener = new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateDialog();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateDialog();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateDialog();
        }
    };

    /**
     * Text field for entering number of epochs.
     */
    private JTextField tfEpochNumber;

    /**
     * Text field for entering learning rate.
     */
    private JTextField tfLearningRate;
    private JTextField tfRefreshInterval;
    /**
     * Panel with ok and cancel button.
     */
    private JPanel dialogPanel;

    /**
     * Constructor that initializes network training dialog.
     */
    public TrainNetworkDialog() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(windowClosingListener);
        setTitle("Train neural network...");
        status = false;
        initGUI();
        pack();
        final Dimension defaultDimension = new Dimension(500, 200);
        setSize(defaultDimension);

    }

    /**
     * Method obtains number of epochs for training.
     *
     * @return number of epochs
     * @throws IllegalStateException
     *             if training parameters haven't been set
     */
    public Integer getEpochsNumber() throws IllegalStateException {
        if (!status) {
            throw new IllegalStateException("Cannot obtain neurons number when return status is false");
        }
        return epochsNumber;
    }

    /**
     * Method obtains learning rate for training.
     *
     * @return learning rate
     * @throws IllegalStateException
     *             if training parameters haven't been set
     */
    public Double getLearningRate() throws IllegalStateException {
        if (!status) {
            throw new IllegalStateException("Cannot obtain neurons number when return status is false");
        }
        return learningRate;

    }

    /**
     * Method returns dialog definition status.
     *
     * @return true if values have been entered correctly and user has accepted values, false otherwise
     */
    public boolean getReturnStatus() {
        return status;
    }

    public Integer getRefreshInterval() {
        if (!status) {
            throw new IllegalStateException("Cannot obtain refresh interval when return status is false");
        }
        return refreshInterval;
    }

    /**
     * Method initializes panel with dialog realted buttons. (ok and cancel)
     */
    private void initDialogPanel() {
        btnOk = new JButton("Create");
        dialogPanel.add(btnOk);
        btnOk.setEnabled(false);
        btnOk.addActionListener(e -> {
            validateDialog();
            if (status) {
                dispose();
            }
        });
        JButton btnCancel = new JButton("Cancel");
        dialogPanel.add(btnCancel);
        btnCancel.addActionListener(e -> {
            status = false;
            dispose();
        });
    }

    /**
     * Method initializes dialog GUI.
     */
    private void initGUI() {
        optionsPanel = new JPanel();
        initOptionsPanel();
        add(optionsPanel, BorderLayout.CENTER);

        dialogPanel = new JPanel();
        initDialogPanel();
        add(dialogPanel, BorderLayout.PAGE_END);

        tfEpochNumber.setText(String.valueOf(DEFAULT_EPOCH_NUMBER));
        tfLearningRate.setText(String.valueOf(DEFUALT_LEARNING_RATE));
        tfRefreshInterval.setText(String.valueOf(DEFAULT_REFRESH_INTERVAL));
    }

    /**
     * Method initializes network training parameter fields.
     */
    private void initOptionsPanel() {
        final GridLayout layout = new GridLayout(0, 2, 3, 5);
        optionsPanel.setLayout(layout);
        JLabel lLearningRate = new JLabel("Learning rate:", SwingConstants.CENTER);
        optionsPanel.add(lLearningRate);

        tfLearningRate = new JTextField();
        tfLearningRate.getDocument().addDocumentListener(fieldsListener);
        optionsPanel.add(tfLearningRate);

        JLabel lEpochNumber = new JLabel("Number of epochs: ", SwingConstants.CENTER);
        optionsPanel.add(lEpochNumber);

        tfEpochNumber = new JTextField();
        tfEpochNumber.getDocument().addDocumentListener(fieldsListener);
        optionsPanel.add(tfEpochNumber);

        JLabel lRefreshInteval = new JLabel("Refresh interval:", SwingConstants.CENTER);
        optionsPanel.add(lRefreshInteval);
        tfRefreshInterval = new JTextField();
        tfRefreshInterval.getDocument().addDocumentListener(fieldsListener);
        optionsPanel.add(tfRefreshInterval);
    }

    /**
     * Function sets epochs number to inputed value.
     *
     * @throws NumberFormatException
     *             if epoch number cannot be parsed as integer
     */
    private void processEpochNumberField() throws NumberFormatException {
        epochsNumber = Integer.parseInt(tfEpochNumber.getText().trim());

    }

    /**
     * Function sets learning rate to inputed value.
     *
     * @throws NumberFormatException
     *             if inputed value cannot be parsed as double
     */
    private void processLearningRateField() throws NumberFormatException {
        learningRate = Double.parseDouble(tfLearningRate.getText().trim());
    }

    private void processRefreshInterval() throws NumberFormatException {
        refreshInterval = Integer.parseInt(tfRefreshInterval.getText().trim());
    }

    /**
     * Function tries to process and validate all fields in dialog.
     */
    private void validateDialog() {
        try {
            processLearningRateField();
            processEpochNumberField();
            processRefreshInterval();
            if (learningRate < 0 || learningRate > 1 || epochsNumber < 1 || refreshInterval < 0) {
                status = false;
            } else {
                status = true;
            }
        } catch (RuntimeException e) {
            status = false;
        }
        btnOk.setEnabled(status);
    }

}
