package hr.fer.zemris.studentforms.formdetection.panels;

import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.Line;
import hr.fer.zemris.image.gui.ImagePanel;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataAdapter;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataListener;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Panel for displaying transformed form while detecting form features and applying transformations.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 11.6.2017.
 */
public class FormDetectionCanvasPanel extends ImagePanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Form detection data model.
     */
    private FormDetectionDataModel dataModel;
    /**
     * Listener listens for image transformations.
     */
    private FormDetectionDataListener transformedImageListener = new FormDetectionDataAdapter() {
        @Override
        public void stateChanged(FormDetectionDataModel dataModel) {
            setImage(dataModel.getTransformedImage());
        };
    };

    /**
     * Constructor initializes panel with form detection data model.
     *
     * @param dataModel
     *            form detection data model
     */
    public FormDetectionCanvasPanel(FormDetectionDataModel dataModel) {
        this.dataModel = dataModel;
        dataModel.addListener(transformedImageListener);
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (dataModel.getTransformedBinImage() == null) {
            return;
        }

        Dimension imgDim = new Dimension(dataModel.getTransformedBinImage().getWidth(), dataModel
                .getTransformedBinImage().getHeight());
        double scaleX = getWidth() / imgDim.getWidth();
        double scaleY = getHeight() / imgDim.getHeight();

        if (!dataModel.isCoordinateSystemDefined()) {
            return;
        }

        g.setColor(Color.RED);
        CoordinateSystem2D coord = dataModel.getFormNode().getCoordinateSystem();
        Line xAxis = coord.getX();
        g.drawLine((int) (xAxis.getP1().getX() * scaleX), (int) (xAxis.getP1().getY() * scaleY), (int) (xAxis.getP2()
                .getX() * scaleX), (int) (xAxis.getP2().getY() * scaleY));

        g.setColor(Color.BLUE);
        Line yAxis = coord.getY();
        g.drawLine((int) (yAxis.getP1().getX() * scaleX), (int) (yAxis.getP1().getY() * scaleY), (int) (yAxis.getP2()
                .getX() * scaleX), (int) (yAxis.getP2().getY() * scaleY));
    }
}
