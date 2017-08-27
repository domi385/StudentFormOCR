package hr.fer.zemris.studentforms.classification.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Dialog for creating neural network.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class CreateNeuralNetworkDialog extends JDialog {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default number of input layer neurons.
     */
    private static final int DEFAULT_INPUT_LAYER_NEURONS_NUMBER = 784;

    /**
     * Default number of output layer neurons.
     */
    private static final int DEFAULT_OUTPUT_LAYER_NEURONS_NUMBER = 11;

    /**
     * Listener for closing dialog which ensures that the state is false if the user has closed the window.
     */
    private WindowListener windowClosingListener = new WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            status = false;
            dispose();
        };
    };

    /**
     * Text input field for inputing number of input layer neurons.
     */
    private JTextField tfInputLayerNeurons;

    /**
     * Text input field for inputing number of output layer neurons.
     */
    private JTextField tfOutputLayerNeurons;

    /**
     * Panel with options of creating neural network.
     */
    private JPanel optionsPanel;

    /**
     * Dialog status. False if the network parameters are invalid, true otherwise.
     */
    private boolean status;

    /**
     * Document listener for input and output layer neurons number.
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
     * Document listener for hidden layer neurons number.
     */
    private DocumentListener hiddenLayerFieldListener = new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateField();

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateField();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateField();
        }

        private void validateField() {
            try {
                processHiddenLayerNeuronsField();
                if (tempHiddenLayerNeuronsNumber < 1) {
                    btnAddHiddenNeuronLayer.setEnabled(false);
                } else {
                    btnAddHiddenNeuronLayer.setEnabled(true);
                }
            } catch (RuntimeException e) {
                btnAddHiddenNeuronLayer.setEnabled(false);
            }
        }
    };

    /**
     * Number of input layer neurons.
     */
    private int inputNeuronsNumber;

    /**
     * Number of output layer neurons.
     */
    private int outputNeuronsNumber;

    /**
     * List of hidden layers neurons numbers.
     */
    private List<Integer> hiddenNeuronsNumber;

    /**
     * Dialog ok button.
     */
    private JButton btnOk;

    /**
     * List model for displaying number of inputed hidden neurons layers number.
     */
    private DefaultListModel<String> hiddenLayerNeuronsListModel;

    /**
     * Button for adding hidden layer.
     */
    private JButton btnAddHiddenNeuronLayer;

    /**
     * Text field for inputing number of hidden layer neurons.
     */
    private JTextField tfHiddenLayerNeurons;

    /**
     * Button for removing hidden layer.
     */
    private JButton btnRemoveHiddenNeuronLayer;

    /**
     * Currently inputed number of hidden layer neurons.
     */
    private int tempHiddenLayerNeuronsNumber;

    /**
     * Constructor that initializes neural network creation dialog.
     */
    public CreateNeuralNetworkDialog() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(windowClosingListener);
        setTitle("Create new neural network...");
        status = false;
        hiddenNeuronsNumber = new ArrayList<Integer>();
        initGUI();
        pack();

    }

    /**
     * Method obtains inputed neurons number starting with input layer, then hidden layers and finally output layer
     * neurons number.
     *
     * @return number of neurons for each layer
     * @throws IllegalStateException
     *             if dialog state was negative
     */
    public List<Integer> getNeuronsNumber() throws IllegalStateException {
        if (!status) {
            throw new IllegalStateException("Cannot obtain neurons number when return status is false");
        }
        List<Integer> neuronsNumber = new ArrayList<Integer>();
        neuronsNumber.add(inputNeuronsNumber);
        neuronsNumber.addAll(hiddenNeuronsNumber);
        neuronsNumber.add(outputNeuronsNumber);
        return neuronsNumber;
    }

    /**
     * Method obtains dialog return status.
     *
     * @return true if the status is positive, false otherwise
     */
    public boolean getReturnStatus() {
        return status;
    }

    /**
     * Method initializes dialogs GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        optionsPanel = new JPanel(new GridLayout(0, 2));
        initOptionsPanel();
        add(optionsPanel, BorderLayout.CENTER);

        JPanel dialogPanel = new JPanel();
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

        add(dialogPanel, BorderLayout.PAGE_END);

        tfInputLayerNeurons.setText(String.valueOf(DEFAULT_INPUT_LAYER_NEURONS_NUMBER));
        tfOutputLayerNeurons.setText(String.valueOf(DEFAULT_OUTPUT_LAYER_NEURONS_NUMBER));
        tfOutputLayerNeurons.setEnabled(false);
    }

    /**
     * Dimension of a text field.
     */
    private static final int TEXT_FIELD_DIM = 50;

    /**
     * Method initializes panel with options for creating neural network.
     */
    private void initOptionsPanel() {
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        // FIRST PANEL - input layer
        JPanel firstLayerPanel = new JPanel();
        optionsPanel.add(firstLayerPanel);

        JLabel lInputLayerNeurons = new JLabel("Number of input neurons");
        firstLayerPanel.add(lInputLayerNeurons);

        tfInputLayerNeurons = new JTextField(TEXT_FIELD_DIM);
        tfInputLayerNeurons.getDocument().addDocumentListener(fieldsListener);
        firstLayerPanel.add(tfInputLayerNeurons);

        // SECOND PANEL - hidden layer
        JLabel lHiddenLayers = new JLabel("Hidden layers", SwingConstants.LEFT);
        optionsPanel.add(lHiddenLayers);
        JPanel hiddenLayerPanel = new JPanel();
        optionsPanel.add(hiddenLayerPanel);

        hiddenLayerNeuronsListModel = new DefaultListModel<String>();
        JList<String> hiddenLayerNeurons = new JList<>(hiddenLayerNeuronsListModel);

        JScrollPane hiddenLayerListScrollPane = new JScrollPane(hiddenLayerNeurons);
        hiddenLayerPanel.add(hiddenLayerListScrollPane);

        JPanel addHiddenNeuronLayerPanel = new JPanel();
        optionsPanel.add(addHiddenNeuronLayerPanel);

        tfHiddenLayerNeurons = new JTextField(TEXT_FIELD_DIM);
        addHiddenNeuronLayerPanel.add(tfHiddenLayerNeurons);
        tfHiddenLayerNeurons.getDocument().addDocumentListener(hiddenLayerFieldListener);

        btnAddHiddenNeuronLayer = new JButton("+");
        addHiddenNeuronLayerPanel.add(btnAddHiddenNeuronLayer);
        btnAddHiddenNeuronLayer.setEnabled(false);
        btnAddHiddenNeuronLayer.addActionListener(e -> {
            hiddenNeuronsNumber.add(tempHiddenLayerNeuronsNumber);
            hiddenLayerNeuronsListModel.addElement(hiddenNeuronsNumber.size() + ".  " + tempHiddenLayerNeuronsNumber);
            tfHiddenLayerNeurons.setText("");
            btnRemoveHiddenNeuronLayer.setEnabled(true);
        });

        btnRemoveHiddenNeuronLayer = new JButton("-");
        addHiddenNeuronLayerPanel.add(btnRemoveHiddenNeuronLayer);
        btnRemoveHiddenNeuronLayer.setEnabled(false);
        btnRemoveHiddenNeuronLayer.addActionListener(e -> {
            hiddenNeuronsNumber.remove(hiddenNeuronsNumber.size() - 1);
            hiddenLayerNeuronsListModel.remove(hiddenLayerNeuronsListModel.getSize() - 1);
            if (hiddenNeuronsNumber.size() == 0) {
                btnRemoveHiddenNeuronLayer.setEnabled(false);
            }
        });

        // THIRD PANEL - output layer
        JPanel outputPanel = new JPanel();
        optionsPanel.add(outputPanel);

        JLabel lOutputLayerNeurons = new JLabel("Number of output neurons");
        outputPanel.add(lOutputLayerNeurons);

        tfOutputLayerNeurons = new JTextField(TEXT_FIELD_DIM);
        tfOutputLayerNeurons.getDocument().addDocumentListener(fieldsListener);
        outputPanel.add(tfOutputLayerNeurons);

    }

    /**
     * Method tries to process text inputed in hidden layer text field.
     *
     * @throws NumberFormatException
     *             if given input cannot be parsed as integer
     */
    private void processHiddenLayerNeuronsField() throws NumberFormatException {
        tempHiddenLayerNeuronsNumber = Integer.parseInt(tfHiddenLayerNeurons.getText().trim());
    }

    /**
     * Method tries to process text inputed in input layer text field.
     *
     * @throws NumberFormatException
     *             if given input cannot be parsed as integer
     */
    private void processInputLayerNeuronsField() throws NumberFormatException {
        inputNeuronsNumber = Integer.parseInt(tfInputLayerNeurons.getText().trim());
    }

    /**
     * Method tries to process text inputed in output layer text field.
     *
     * @throws NumberFormatException
     *             if given input cannot be parsed as integer
     */
    private void processOutputLayerNeuronsField() throws NumberFormatException {
        outputNeuronsNumber = Integer.parseInt(tfOutputLayerNeurons.getText().trim());
    }

    /**
     * Method validates input layer input and output layer input.
     */
    private void validateDialog() {
        try {
            processInputLayerNeuronsField();
            processOutputLayerNeuronsField();
            if (outputNeuronsNumber < 1 || inputNeuronsNumber < 1) {
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
