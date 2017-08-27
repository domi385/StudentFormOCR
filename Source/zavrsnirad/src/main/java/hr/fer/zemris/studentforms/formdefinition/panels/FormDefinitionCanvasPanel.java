package hr.fer.zemris.studentforms.formdefinition.panels;

import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.Line;
import hr.fer.zemris.image.geometry.RegionEdge;
import hr.fer.zemris.image.gui.ImagePanel;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionData;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataAdapter;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public class FormDefinitionCanvasPanel extends ImagePanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private FormDefinitionData dataModel;
    private FormDefinitionDataListener dataListener = new FormDefinitionDataAdapter() {

        @Override
        public void imageChanged(BufferedImage image) {
            setImage(image);
            repaint();
        }

        @Override
        public void pointFieldAdded() {
            repaint();
        }

        @Override
        public void statusChanged(DefinitionStatus status) {
            if (status == DefinitionStatus.MARKER_DEFINITION || status == DefinitionStatus.POINTS_DEFINITION) {
                logMousePosition = true;
                repaint();
            } else {
                logMousePosition = false;
                repaint();
            }
        }
    };

    private boolean logMousePosition = false;
    private MouseMotionListener canvasMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            if (logMousePosition) {
                dataModel.setTempMousePosition(e.getX(), e.getY(), getWidth(), getHeight());
            }
        }
    };
    private MouseListener canvasClickListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (logMousePosition) {
                dataModel.addMousePosition(e.getX(), e.getY(), getWidth(), getHeight());
            }
        };
    };

    public FormDefinitionCanvasPanel(FormDefinitionData dataModel) {
        super();
        this.dataModel = dataModel;
        this.dataModel.addListener(dataListener);
        addMouseMotionListener(canvasMotionListener);
        addMouseListener(canvasClickListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!dataModel.isImageLoaded()) {
            return;
        }

        Dimension imgDim = dataModel.getImageDimension();
        double scaleX = getWidth() / imgDim.getWidth();
        double scaleY = getHeight() / imgDim.getHeight();

        if (!dataModel.isCoordinateSystemDefined()) {
            return;
        }

        if (dataModel.getNumberOfPoints() > 0) {

            g.setColor(Color.CYAN);
            List<RegionEdge> pointFieldEdges = dataModel.getPointFields();
            List<Rectangle> pointsRectangles = pointFieldEdges.stream().map(i -> i.getBoundingRectange())
                    .collect(Collectors.toList());
            for (Rectangle rect : pointsRectangles) {
                g.fillRect((int) (rect.x * scaleX), (int) (rect.y * scaleY), (int) (rect.width * scaleX),
                        (int) (rect.height * scaleY));
            }
        }

        g.setColor(Color.RED);
        CoordinateSystem2D coord = dataModel.getCoordinateSystem();
        Line xAxis = coord.getX();
        g.drawLine((int) (xAxis.getP1().getX() * scaleX), (int) (xAxis.getP1().getY() * scaleY), (int) (xAxis.getP2()
                .getX() * scaleX), (int) (xAxis.getP2().getY() * scaleY));

        g.setColor(Color.BLUE);
        Line yAxis = coord.getY();
        g.drawLine((int) (yAxis.getP1().getX() * scaleX), (int) (yAxis.getP1().getY() * scaleY), (int) (yAxis.getP2()
                .getX() * scaleX), (int) (yAxis.getP2().getY() * scaleY));

    }
}
