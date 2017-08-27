package hr.fer.zemris.form;

import hr.fer.zemris.image.algorithms.ComponentLabelingAlgorithm;
import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;
import hr.fer.zemris.image.geometry.GeometryUtility;
import hr.fer.zemris.image.geometry.Region;
import hr.fer.zemris.image.geometry.RegionEdge;
import hr.fer.zemris.image.geometry.RegionUtility;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for form node objects.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class FormNodeUtility {

    /**
     * Method extracts field or subfield (given by offset values) with given index from a form node.
     *
     * @param formNode
     *            form node instance
     * @param pointIndex
     *            point field index
     * @param upOffset
     *            field upper offset to be excluded
     * @param lowOffset
     *            field lower offset to be excluded
     * @param leftOffset
     *            field left offset to be excluded
     * @param rightOffset
     *            field right offset to be excluded
     * @return binary image of extracted field
     * @throws IllegalArgumentException
     *             if given form node is null
     * @throws IndexOutOfBoundsException
     *             if given point index is out of template points range
     */
    public static IBinaryImage extractField(FormNode formNode, int pointIndex, int upOffset, int lowOffset,
            int leftOffset, int rightOffset) throws IllegalArgumentException, IndexOutOfBoundsException {

        if (formNode == null) {
            throw new IllegalArgumentException("Given form node must not be null");
        }

        if (pointIndex < 0 || pointIndex >= formNode.getFormTemplate().getPointsNumber()) {
            throw new IndexOutOfBoundsException("Given point field index is out of form template points index.");
        }

        RegionEdge field = formNode.getFormTemplate().getPointFields().get(pointIndex);
        // Rectangle fieldRectangle = field.getBoundingRectange();
        Rectangle fieldRectangle = transformFieldRectangle(field.getBoundingRectange(), formNode.getFormTemplate(),
                formNode.getCoordinateSystem());
        // FormTemplateParameters ftp = formNode.getFormTemplate();
        // Point originalOrigin = ftp.getCoordinateSystem().getOrigin();
        // Point newOrigin = formNode.getCoordinateSystem().getOrigin();

        // double scalex = formNode.getCoordinateSystem().getX().calcLength()
        // / (double) ftp.getCoordinateSystem().getX().calcLength();
        // double scaley = formNode.getCoordinateSystem().getY().calcLength()
        // / (double) ftp.getCoordinateSystem().getY().calcLength();

        // IBinaryImage fieldImage = formNode.getBinarizedForm().getSubimage(
        // (int) Math.round((fieldRectangle.x - originalOrigin.getX()) * scalex + newOrigin.getX() + leftOffset),
        // (int) Math.round((fieldRectangle.y - originalOrigin.getY()) * scaley + newOrigin.getY() + upOffset),
        // (int) Math.round(fieldRectangle.width * scalex - rightOffset - leftOffset),
        // (int) Math.round(fieldRectangle.height * scaley - upOffset - lowOffset));

        IBinaryImage fieldImage = formNode.getBinarizedForm().getSubimage(fieldRectangle.x + leftOffset,
                fieldRectangle.y + upOffset, (fieldRectangle.width) - leftOffset - rightOffset,
                (fieldRectangle.height) - upOffset - lowOffset);
        return fieldImage;
    }

    /**
     * Method transforms form template rectangle to current form rectangle.
     *
     * @param boundingRectange
     *            form template points bounding rectangle
     * @param ftp
     *            form template parameters
     * @param coord
     *            current form coordinate system
     * @return current form template points bounding rectangle
     */
    private static Rectangle transformFieldRectangle(Rectangle boundingRectange, FormTemplateParameters ftp,
            CoordinateSystem2D coord) {
        double angle = -GeometryUtility.getAngleBetweenLines(ftp.getCoordinateSystem().getY(), coord.getY());
        double scalex = coord.getX().calcLength() / (double) ftp.getCoordinateSystem().getX().calcLength();
        double scaley = coord.getY().calcLength() / (double) ftp.getCoordinateSystem().getY().calcLength();

        double x;
        double y;

        // upper left point
        double x0 = boundingRectange.x * scalex;
        double y0 = boundingRectange.y * scaley;

        x = x0 - ftp.getCoordinateSystem().getOrigin().getX();
        y = y0 - ftp.getCoordinateSystem().getOrigin().getY();

        x0 = x * Math.cos(angle) + y * Math.sin(angle);
        y0 = -x * Math.sin(angle) + y * Math.cos(angle);

        x = x0 + coord.getOrigin().getX();
        y = y0 + coord.getOrigin().getY();

        x0 = x;
        y0 = y;
        // lower left point

        double x1 = (boundingRectange.x + boundingRectange.width) * scalex;
        double y1 = (boundingRectange.y + boundingRectange.height) * scaley;

        x = x1 - ftp.getCoordinateSystem().getOrigin().getX();
        y = y1 - ftp.getCoordinateSystem().getOrigin().getY();

        x1 = x * Math.cos(angle) + y * Math.sin(angle);
        y1 = -x * Math.sin(angle) + y * Math.cos(angle);

        x = x1 + coord.getOrigin().getX();
        y = y1 + coord.getOrigin().getY();

        x1 = x;
        y1 = y;
        // Point p1 = new Point((int) x0, (int) y0);
        // Point p2 = new Point((int) x1, (int) y1);
        // System.out.println(p1 + " " + p2);

        return new Rectangle((int) x0, (int) y0, (int) (x1 - x0), (int) (y1 - y0));
    }

    /**
     * Method segments a binary image of a numeral into a digits.
     *
     * @param fieldImage
     *            binary image containing a handwritten numeral
     * @param segmentsFilter
     *            filter for filtering segments candidates
     * @return list of numeral segments representing digits
     */
    public static List<IBinaryImage> segmentField(IBinaryImage fieldImage, Predicate<Region> segmentsFilter) {
        List<IBinaryImage> segments = new ArrayList<IBinaryImage>();
        List<Region> components = ComponentLabelingAlgorithm.getComponents(fieldImage);
        components = components.stream().filter(segmentsFilter).collect(Collectors.toList());
        for (int i = 0; i < components.size(); i++) {
            segments.add(RegionUtility.regionToBinaryImage(components.get(i)));

        }
        return segments;
    }

    /**
     * Private utility class constructor.
     */
    private FormNodeUtility() {
    }

}
