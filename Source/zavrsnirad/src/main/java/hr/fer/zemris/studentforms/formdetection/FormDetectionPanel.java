package hr.fer.zemris.studentforms.formdetection;

import hr.fer.zemris.image.gui.ImagePanel;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataAdapter;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataListener;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataModel;
import hr.fer.zemris.studentforms.formdetection.panels.FormDetectionCanvasPanel;
import hr.fer.zemris.studentforms.formdetection.panels.FormDetectionToolPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Panel used to detect regions and segments on a form.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.5.2017.
 */
public class FormDetectionPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Original form image panel.
     */
    private ImagePanel originalPanel;
    /**
     * Transformed form image panel.
     */
    private FormDetectionCanvasPanel canvasPanel;

    /**
     * Central panel reference.
     */
    private JPanel centralPanel;
    /**
     * Panel with tools for form detection manipulation.
     */
    private JPanel toolPanel;

    /**
     * Form detection data model.
     */
    private FormDetectionDataModel dataModel;

    /**
     * Listener that updates currently loaded original form.
     */
    private FormDetectionDataListener originalImageDataListener = new FormDetectionDataAdapter() {

        @Override
        public void originalChanged(FormDetectionDataModel dataModel) {
            originalPanel.setImage(dataModel.getOriginalImage());
        };
    };

    /**
     * Constructor that initializes data model and gui.
     */
    public FormDetectionPanel() {
        dataModel = new FormDetectionDataModel();
        initGUI();
    }

    /**
     * Method obtains panel's data model.
     *
     * @return form detection data model
     */
    public FormDetectionDataModel getDataModel() {
        return dataModel;
    }

    /**
     * Method initializes panel's gui.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        centralPanel = new JPanel(new GridLayout(1, 2));
        originalPanel = new ImagePanel();
        dataModel.addListener(originalImageDataListener);
        centralPanel.add(originalPanel);

        canvasPanel = new FormDetectionCanvasPanel(dataModel);
        centralPanel.add(canvasPanel);
        add(centralPanel, BorderLayout.CENTER);

        toolPanel = new FormDetectionToolPanel(dataModel);
        add(toolPanel, BorderLayout.LINE_END);

    }
}
