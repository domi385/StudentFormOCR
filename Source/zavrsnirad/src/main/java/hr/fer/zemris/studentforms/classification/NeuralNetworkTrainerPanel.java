package hr.fer.zemris.studentforms.classification;

import hr.fer.zemris.studentforms.classification.data.ClassificationDataModel;
import hr.fer.zemris.studentforms.classification.data.ClassificationDataModelAdapter;
import hr.fer.zemris.studentforms.classification.data.ClassificationDataModelListener;
import hr.fer.zemris.studentforms.classification.panels.ControlPanel;
import hr.fer.zemris.studentforms.classification.panels.ErrorChartPanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel for training neural network.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class NeuralNetworkTrainerPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    private ControlPanel controlPanel;

    private ErrorChartPanel chartPanel;
    private ClassificationDataModel dataModel;

    private ClassificationDataModelListener chartModelListener = new ClassificationDataModelAdapter() {
        @Override
        public void newNetworkLoaded() {
            chartPanel.clearGraph();
        }

        @Override
        public void trainingProgressUpdate(double trainingError, double validationError, int epoch) {
            chartPanel.addValue(trainingError, validationError, epoch);

        }
    };

    public NeuralNetworkTrainerPanel(JTabbedPane parent) {
        dataModel = new ClassificationDataModel();
        initGUI();
    }

    /**
     * Method initializes panels GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        controlPanel = new ControlPanel(dataModel);

        chartPanel = new ErrorChartPanel();

        dataModel.addListener(chartModelListener);
        add(controlPanel, BorderLayout.PAGE_END);
        add(chartPanel, BorderLayout.CENTER);
    }

}
