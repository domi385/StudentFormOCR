package hr.fer.zemris.studentforms.formdefinition;

import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionData;
import hr.fer.zemris.studentforms.formdefinition.panels.FormDefinitionCanvasPanel;
import hr.fer.zemris.studentforms.formdefinition.panels.FormDefinitionStatusPanel;
import hr.fer.zemris.studentforms.formdefinition.panels.FormDefinitionToolsPanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class FormDefinitionPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    private JPanel toolPanel;

    private JPanel canvasPanel;

    private JPanel statusPanel;

    private FormDefinitionData dataModel;

    public FormDefinitionPanel() {

        dataModel = new FormDefinitionData();
        initGUI();

    }

    public FormDefinitionData getDataModel() {
        return dataModel;
    }

    /**
     * Method initializes panel's GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        toolPanel = new FormDefinitionToolsPanel(dataModel);
        add(toolPanel, BorderLayout.LINE_END);

        canvasPanel = new FormDefinitionCanvasPanel(dataModel);
        add(canvasPanel, BorderLayout.CENTER);

        statusPanel = new FormDefinitionStatusPanel(dataModel);
        add(statusPanel, BorderLayout.PAGE_END);
    }

}
