package hr.fer.zemris.studentforms.formdefinition.data;

import hr.fer.zemris.image.geometry.Point;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;

import java.awt.image.BufferedImage;

/**
 * Adapter class that offers empty listener implementation.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class FormDefinitionDataAdapter implements FormDefinitionDataListener {

    @Override
    public void imageChanged(BufferedImage image) {

    }

    @Override
    public void mousePositionChanged(Point mousePosition) {

    }

    @Override
    public void pointFieldAdded() {

    }

    @Override
    public void statusChanged(DefinitionStatus status) {

    }

}
