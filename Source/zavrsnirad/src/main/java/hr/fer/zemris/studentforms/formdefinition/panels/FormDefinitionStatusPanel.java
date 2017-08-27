package hr.fer.zemris.studentforms.formdefinition.panels;

import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionData;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataAdapter;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Form definition status panel.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 6.9.2017.
 */
public class FormDefinitionStatusPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Form definition data model.
     */
    private FormDefinitionData dataModel;
    /**
     * Listener listens for data model changes.
     */
    private FormDefinitionDataListener dataListener = new FormDefinitionDataAdapter() {
        @Override
        public void mousePositionChanged(Point mousePosition) {
            updateStatus();
        }

        @Override
        public void statusChanged(DefinitionStatus status) {
            updateStatus();
        }

    };

    /**
     * Status label.
     */
    private JLabel statusLabel;

    /**
     * Constructor that initializes form definition status panel.
     *
     * @param dataModel
     *            form definition data model
     */
    public FormDefinitionStatusPanel(FormDefinitionData dataModel) {
        this.dataModel = dataModel;
        this.dataModel.addListener(dataListener);
        initGUI();
    }

    /**
     * Method initializes panel's GUI.
     */
    private void initGUI() {
        statusLabel = new JLabel();
        add(statusLabel);
        updateStatus();
    }

    /**
     * Method updates current status.
     */
    private void updateStatus() {
        String statusString = dataModel.getStatus().toString();
        if (dataModel.getStatus() == DefinitionStatus.MARKER_DEFINITION) {
            statusString += " Number of markers: " + dataModel.getNumberOfMarkers() + ". Current position: "
                    + dataModel.getTempMousePosition();
        }
        if (dataModel.getStatus() == DefinitionStatus.POINTS_DEFINITION) {
            statusString += " Number of point fields: " + dataModel.getNumberOfPoints() + ". Current position: "
                    + dataModel.getTempMousePosition();
        }
        statusLabel.setText(statusString);
    }

}
