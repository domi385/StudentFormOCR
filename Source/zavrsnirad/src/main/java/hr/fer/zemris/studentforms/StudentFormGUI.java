package hr.fer.zemris.studentforms;

import hr.fer.zemris.studentforms.classification.NeuralNetworkTrainerPanel;
import hr.fer.zemris.studentforms.formbatch.BatchPanel;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;
import hr.fer.zemris.studentforms.formdefinition.FormDefinitionPanel;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataAdapter;
import hr.fer.zemris.studentforms.formdetection.FormDetectionPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main graphics application for processing student forms.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 29.4.2017.
 */
public class StudentFormGUI extends JFrame {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Default window width.
     */
    private static final int WINDOW_WIDTH = 950;

    /**
     * Default window height.
     */
    private static final int WINDOW_HEIGHT = 700;

    /**
     * Method starts with program run.
     *
     * @param args
     *            command line arguments are ignored
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ignorable) {
        }
        SwingUtilities.invokeLater(StudentFormGUI::new);
    }

    /**
     * Window listener for manipulating state before closing application.
     */
    private WindowListener windowClosingListener = new WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            closeApplication();
        }

    };;

    /**
     * Constructor initializes window properties and calls methods for initializing GUI.
     */
    public StudentFormGUI() {
        addWindowListener(windowClosingListener);
        setTitle("Student forms analysis");

        initGUI();
        pack();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
    }

    /**
     * Method closes application and releases resources.
     */
    private void closeApplication() {
        dispose();
        System.exit(0);
    }

    /**
     * Method initializes GUI.
     */
    private void initGUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        add(tabbedPane);

        FormDefinitionPanel formDefinitionPanel = new FormDefinitionPanel();
        tabbedPane.add("Form definition", formDefinitionPanel);

        FormDetectionPanel formDetectionPanel = new FormDetectionPanel();
        tabbedPane.add("Form detection", formDetectionPanel);
        tabbedPane.setEnabledAt(tabbedPane.indexOfComponent(formDetectionPanel), false);

        formDefinitionPanel.getDataModel().addListener(new FormDefinitionDataAdapter() {
            @Override
            public void statusChanged(DefinitionStatus status) {
                tabbedPane.setEnabledAt(tabbedPane.indexOfComponent(formDetectionPanel),
                        status == DefinitionStatus.DEFINED_TEMPLATE);
                formDetectionPanel.getDataModel().setFormTemplate(formDefinitionPanel.getDataModel().getFormTemplate());

            }
        });
        NeuralNetworkTrainerPanel networkTrainerPanel = new NeuralNetworkTrainerPanel(tabbedPane);
        tabbedPane.add("Classification", networkTrainerPanel);
        BatchPanel batchPanel = new BatchPanel();
        tabbedPane.add("Batch analysis", batchPanel);

    }
}
